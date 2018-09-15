package com.nju.msr.core.model;

import com.nju.msr.core.Param;

import java.util.*;

public class CallChain {

    MethodPool methodPool = MethodPool.getInstance();

    private CallInfo rootCall;

    /** 表示当前处于哪个调用中
     * 由于无法可靠地保证获得方法结束信息，所以目前这个状态变量是不靠谱，
     */
    private CallInfo currentCall;

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
            if (callInfo == null){
                callInfo=rootCall;
                return callInfo;
            }
            if (hasNext()){
                callInfo = callInfo.getChildCall(callInfo.getChildCallListSize()-1);
                return callInfo;
            }
            return null;
        }
    }

    public Iterator<CallInfo> iterator(){
        return new Itr();
    }

    private Thread thread;
    //private String threadIdSign;

    public CallChain(Thread thread) {
        this.thread = thread;
    }

    /**
     * 堆栈信息与当前调用链进行匹配，将调用信息加入合适的地方
     * @param stackTraceElements
     * @param method
     */
    protected void addCallInfoThroughStackTrace(List<StackTraceElement> stackTraceElements, Method method){
        if (rootCall==null){
            rootCall = generateCallChainFromStackTrace(null,stackTraceElements,stackTraceElements.size()-1, method);
            return;
        }

        Iterator<CallInfo> iterator = this.iterator();
        CallInfo parentCallInfo = null;
        for (int i=stackTraceElements.size()-1; i>=0; i--){
            //TODO
            //匹配调用链，来确定从哪个方法开始加入调用链
            CallInfo callInfo = iterator.next();
            if (matchMethod(stackTraceElements, i, callInfo)){
                parentCallInfo = callInfo;
                continue;
            }else {
                parentCallInfo.addChildCall( generateCallChainFromStackTrace(parentCallInfo.getCallee(), stackTraceElements, i, method) );
                return;
            }
        }
    }
    private boolean matchMethod(List<StackTraceElement> stackTraceElements, int index, CallInfo callInfo){
        if (callInfo==null)
            return false;

        Method method = callInfo.getCallee();
        StackTraceElement s = stackTraceElements.get(index);
        Method stackTrackMethod = MethodPool.getInstance().getMethod(s.getClassName(),s.getMethodName());
        if (stackTrackMethod == method)
            return true;

        if (Param.isUnderPackage(stackTrackMethod.getOwner())){
            if(stackTrackMethod.getOwner().equals(method.getOwner()) && stackTrackMethod.getName().equals(method.getName())){
                if(index>0){
                    return true;
                }
            }
            /* 这个方法暂时不靠谱
            if (!callInfo.isFinished()){
                return true;
            }*/
            return false;
        }

        return false;
    }
    /**
     * 通过堆栈信息将从startIndex的方法开始，生成调用链
     * @param preMethod 用于链接两个调用链，不能通过startIndex+1去获得前一个方法，因为可能前一个方法为项目中的方法，而堆栈生成的没有desc
     * @param stackTraceElements
     * @param startIndex
     * @param method
     * @return
     */
    private CallInfo generateCallChainFromStackTrace(Method preMethod, List<StackTraceElement> stackTraceElements, int startIndex, Method method){
        if (startIndex<0){
            throw new RuntimeException("startIndex不应该小于0");
        }
        if (startIndex==0){
            return new CallInfo(preMethod,method);
        }

        StackTraceElement s = stackTraceElements.get(startIndex);
        CallInfo root = new CallInfo(preMethod, methodPool.getMethod(s.getClassName(),s.getMethodName()));

        CallInfo parentTemp = root;
        for (int i=startIndex; i>0; i--){
            CallInfo callInfo = generateCallFromStackTrace(stackTraceElements,i,method);
            parentTemp.addChildCall(callInfo);
            parentTemp = callInfo;
        }

        this.currentCall = parentTemp;
        return root;
    }

    /**
     * 只生成一个
     * @param stackTraceElements
     * @param startIndex
     * @return
     */
    private CallInfo generateCallFromStackTrace(List<StackTraceElement> stackTraceElements, int startIndex, Method method) {
        if (startIndex<=0){
            return null;
        }
        if (startIndex==1){
            StackTraceElement s1 = stackTraceElements.get(startIndex);
            Method method1 = methodPool.getMethod(s1.getClassName(),s1.getMethodName());
            return new CallInfo(method1,method);
        }
        StackTraceElement s1 = stackTraceElements.get(startIndex);
        StackTraceElement s2 = stackTraceElements.get(startIndex-1);
        Method method1 = methodPool.getMethod(s1.getClassName(),s1.getMethodName());
        Method method2 = methodPool.getMethod(s2.getClassName(),s2.getMethodName());
        return new CallInfo(method1,method2);
    }



    public CallInfo getRootCall() {
        return rootCall;
    }

    public Thread getThread() {
        return thread;
    }

    public CallInfo getCurrentCall() {
        return currentCall;
    }
}
