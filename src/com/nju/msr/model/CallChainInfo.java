package com.nju.msr.model;

import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.*;

public class CallChainInfo {

    MethodPool methodPool = MethodPool.getInstance();

    private static ThreadLocal<String> currentThreadID = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return Thread.currentThread().getId()+"&&"+System.currentTimeMillis();
        }
    };

    private static Map<String, CallChainInfo> callChainInfoMap = new HashMap<>();

    private CallInfo rootCall;

    /**
     * 从rootCall开始遍历调用链
     */
    private class Itr implements Iterator<CallInfo>{
        private CallInfo callInfo;

        public Itr() {}

        @Override
        public boolean hasNext() {
            if (callInfo==null){
                if (rootCall==null)
                    return false;
                return true;
            }
            return callInfo.hasChildCall();
        }

        @Override
        public CallInfo next() {
            if (hasNext()){
                if (callInfo == null){
                    callInfo=rootCall;
                }
                return callInfo.getChildCall(callInfo.getChildCallListSize()-1);
            }
            return null;
        }
    }

    public Iterator<CallInfo> iterator(){
        return new Itr();
    }

    private String currentThreadIDStr;

    public CallChainInfo(String currentThreadIDStr) {
        this.currentThreadIDStr = currentThreadIDStr;
    }

    /**
     * 堆栈信息与当前调用链进行匹配，将调用信息加入合适的地方
     * @param stackTraceElements
     * @param method
     */
    private void processStackTrace(List<StackTraceElement> stackTraceElements, Method method){
        if (rootCall==null){
            rootCall = generateCallChainFromStackTrace(stackTraceElements,0, method);
        }

        Iterator<CallInfo> iterator = this.iterator();
        for (int i=0; i<stackTraceElements.size(); i++){
            StackTraceElement s = stackTraceElements.get(i);
            Method stackTraceMethod = methodPool.getMethod(s.getClassName(),s.getMethodName(), s.getLineNumber()+"");
            //TODO
            //匹配调用链，来确定从哪个方法开始加入调用链
            CallInfo callInfo = iterator.next();
            if (matchMethod(stackTraceMethod, callInfo)){
                continue;
            }else {
                callInfo.getParentCall().addChildCall(generateCallChainFromStackTrace(stackTraceElements, i, method));
            }
        }
    }
    private boolean matchMethod(Method stackTrackMethod, CallInfo callInfo){
        if (stackTrackMethod == callInfo.getCallee())
            return true;

        if (Integer.parseInt(stackTrackMethod.getDescriptor())<0){
            if (!callInfo.isFinished()){
                return true;
            }
            return false;
        }

        return false;
    }

    /**
     * 通过堆栈信息将从startIndex的方法开始，生成调用链
     * @param stackTraceElements
     * @param startIndex
     * @return
     */
    private CallInfo generateCallChainFromStackTrace(List<StackTraceElement> stackTraceElements, int startIndex, Method method){
        if (startIndex>=stackTraceElements.size()-1){
            return null;
        }

        CallInfo root = generateCallFromStackTrace(stackTraceElements,startIndex,method);

        CallInfo parentTemp = root;
        for (int i=startIndex+1; i<stackTraceElements.size()-1; i++){
            StackTraceElement s = stackTraceElements.get(i);
            CallInfo callInfo = generateCallFromStackTrace(stackTraceElements,i,method);
            parentTemp.addChildCall(callInfo);
            parentTemp = callInfo;
        }
        return root;
    }

    /**
     * 只生成一个
     * @param stackTraceElements
     * @param startIndex
     * @return
     */
    private CallInfo generateCallFromStackTrace(List<StackTraceElement> stackTraceElements, int startIndex, Method method) {
        if (startIndex>=stackTraceElements.size()-1){
            return null;
        }
        if (startIndex==stackTraceElements.size()-2){
            StackTraceElement s1 = stackTraceElements.get(startIndex);
            Method method1 = methodPool.getMethod(s1.getClassName(),s1.getMethodName(), s1.getLineNumber()+"");
            return new CallInfo(method1,method);
        }
        StackTraceElement s1 = stackTraceElements.get(startIndex);
        StackTraceElement s2 = stackTraceElements.get(startIndex+1);
        Method method1 = methodPool.getMethod(s1.getClassName(),s1.getMethodName(), s1.getLineNumber()+"");
        Method method2 = methodPool.getMethod(s2.getClassName(),s2.getMethodName(), s2.getLineNumber()+"");
        return new CallInfo(method1,method2);
    }


    /**
     * 传入调用本次流程的方法的相关信息
     * @param owner
     * @param name
     * @param desc
     */
    public static void processCurrentThread(String owner, String name, String desc){
        Thread currentThread = Thread.currentThread();
        CallChainInfo callChainInfo = getCurrentCallChain();
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        List<StackTraceElement> extractStackTraceElements = new ArrayList<>();
        for (StackTraceElement s: stackTraceElements) {
            if (s.getClassName().startsWith("com.nju.msr"))
                continue;
            extractStackTraceElements.add(s);
        }
        Method method = MethodPool.getInstance().getMethod(owner,name,desc);
        callChainInfo.processStackTrace(extractStackTraceElements, method);
    }

    private static CallChainInfo getCurrentCallChain(){
        CallChainInfo callChainInfo = callChainInfoMap.get(currentThreadID.get());
        if (callChainInfo==null){
            callChainInfo = new CallChainInfo(currentThreadID.get());
        }
        return callChainInfo;
    }


}
