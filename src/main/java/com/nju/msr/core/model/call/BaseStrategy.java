package com.nju.msr.core.model.call;

import com.nju.msr.core.Param;
import com.nju.msr.core.model.exception.CallChainZeroException;
import com.nju.msr.core.model.method.Method;
import com.nju.msr.core.model.method.MethodFactory;
import com.nju.msr.utils.StackUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @Author: jiaqi li
 * @Date: 2018/10/16 0016 15:09
 * @Version 1.0
 */
public class BaseStrategy implements Strategy {
    MethodFactory methodFactory = MethodFactory.getInstance();

    @Override
    public void addCallInfoToCallChain(Method method, CallChain callChain) {
        new AddCallInfoFunction(callChain).addCallInfoThroughStackTrace(StackUtil.processedStackTrace(),method);
    }
    private class AddCallInfoFunction{
        CallChain callChain;

        public AddCallInfoFunction(CallChain callChain) {
            this.callChain = callChain;
        }

        /**
         * 堆栈信息与当前调用链进行匹配，将调用信息加入合适的地方
         * @param stackTraceElements
         * @param method
         */
        void addCallInfoThroughStackTrace(List<StackTraceElement> stackTraceElements, Method method){
            if (callChain.rootCall==null){
                callChain.rootCall = generateCallChainFromStackTrace(null,stackTraceElements,stackTraceElements.size()-1, method);
                return;
            }

            Iterator<CallInfo> iterator = callChain.iterator();
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
            //这一步会导致MethodFactory中有很多额外的不参与调用的方法
            Method stackTrackMethod = MethodFactory.getInstance().getMethod(s.getClassName(),s.getMethodName());
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
                return createTargetCallInfo(preMethod,method);
            }

            StackTraceElement s = stackTraceElements.get(startIndex);
            CallInfo root = createCallInfo(preMethod, methodFactory.getMethod(s.getClassName(),s.getMethodName()));

            CallInfo parentTemp = root;
            for (int i=startIndex; i>0; i--){
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
            if (startIndex<=0){
                return null;
            }
            if (startIndex==1){
                StackTraceElement s1 = stackTraceElements.get(startIndex);
                Method method1 = methodFactory.getMethod(s1.getClassName(),s1.getMethodName());
                return createTargetCallInfo(method1,method);
            }
            StackTraceElement s1 = stackTraceElements.get(startIndex);
            StackTraceElement s2 = stackTraceElements.get(startIndex-1);
            Method method1 = methodFactory.getMethod(s1.getClassName(),s1.getMethodName());
            Method method2 = methodFactory.getMethod(s2.getClassName(),s2.getMethodName());
            return createCallInfo(method1,method2);
        }

        private CallInfo createTargetCallInfo(Method caller, Method callee){
            callChain.currentCall = createCallInfo(caller,callee);
            return callChain.currentCall;
        }
        private CallInfo createCallInfo(Method caller, Method callee){
            return new CallInfo(caller,callee,String.valueOf(callChain.callInfoIdCount.getAndIncrement()),callChain);
        }
    }

    @Override
    public void correctCallChain(Method method, ActionType actionType, CallChain callChain) {
        new CorrectCallChain(method, actionType,callChain).correctCallChain();
    }

    private class CorrectCallChain{
        CallChain callChain;
        ActionType actionType;
        Method method;

        public CorrectCallChain(Method method, ActionType actionType, CallChain callChain) {
            this.actionType = actionType;
            this.callChain = callChain;
            this.method = method;
        }
        /**
         * 当父类构造器抛出异常时无法调用methodEnd
         * S.s -> A.a -> B.b -> C.c -> D.init -> E.init -> F.init
         * F.init抛出异常
         *
         * 进入methodstart或end时
         * 当 currentcall不是init可以通过currentcall去直接操作
         * 当 currentcall是init时需要对比当前callchain和堆栈信息，来判断currentcall是否正确，
         * 若不正确需要修正currentcal和threadCallChain
         */
        void correctCallChain(){

            if (callChain.currentCall!=null && "<init>".equals(callChain.currentCall.getCallee().getName()) ){
                int distance = 0;
                try {
                    distance = matchCurrentCall(callChain);
                } catch (CallChainZeroException e) {
                    e.printStackTrace();
                    callChain.currentCall=null;
                    Stack<CallInfo> stack = callChain.getThreadCallChain();
                    stack.removeAllElements();
                    return;
                }
                if (distance!=0) {
                    if (distance<0) {
                        System.err.println("correctCallChain 异常： distance: " + distance);
                        return;
                    }
                    Stack<CallInfo> stack = callChain.getThreadCallChain();
                    if (callChain.currentCall != stack.peek()) {
                        System.err.println("correctCallChain 异常：" + callChain.currentCall + "  stack.peek: " + stack.peek());
                        return;
                    }
                    //修正currentcall和threadCallChain
                    for (; distance!=0; distance--){
                        if (callChain.currentCall == stack.peek()) {
                            stack.pop();
                        }
                        CallInfo callInfo = callChain.currentCall.getParentCall();
                        if (callInfo==null) {
                            System.err.println("correctCallChain 异常：callInfo 为 NULL");
                        }
                        callChain.currentCall = callInfo;
                    }
                }
            }
        }
        private int matchCurrentCall(CallChain callChain) throws CallChainZeroException {
            int callChainMethodNum = callChain.getCurrentDepth();
            /*int actualNum = StackUtil.getStackWithoutOther().size();
            if (actionType == ActionType.START)
                actualNum--;*/
            int actualNum = 0;
            if (actionType == ActionType.START) {
                actualNum = StackUtil.getStackWithoutCurrentCall().size();
                if (actualNum==0)
                    throw new CallChainZeroException();
            }
            if (actionType == ActionType.END)
                actualNum = StackUtil.processedStackTrace().size();
            return callChainMethodNum-actualNum;
        }

    }

}
