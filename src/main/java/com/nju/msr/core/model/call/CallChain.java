package com.nju.msr.core.model.call;

import com.nju.msr.core.Constant;
import com.nju.msr.core.model.method.Method;
import com.nju.msr.core.model.method.MethodFactory;
import com.nju.msr.core.model.ThreadInfo;
import com.nju.msr.core.persistence.ServiceManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */

/**
 * 表示一次调用链的完整过程
 * 将{@link CallInfo}作为节点，构成一棵树
 */
public abstract class CallChain {

    protected static ThreadLocal<Stack<CallInfo>> threadCallChain = new InheritableThreadLocal<Stack<CallInfo>>();

    /**
     * 计数callinfo 可用作id
     */
    protected AtomicInteger callInfoIdCount = new AtomicInteger(0);

    /**
     * 根callinfo
     */
    protected CallInfo rootCall;

    public Iterator<CallInfo> iterator(){
        return new Itr();
    }

    /** 表示当前处于哪个调用中
     * 由于无法可靠地保证获得方法结束信息，所以目前这个状态变量是不靠谱，
     */
    protected CallInfo currentCall;

    //全局唯一标识
    private String callChainId;

    private ThreadInfo threadInfo = new ThreadInfo();

    private Long preEndTime = null;
    /**
     * 用于后序遍历到当前最近一次调用
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


    public CallChain() {
        callChainId = System.currentTimeMillis()+ Constant.callCahinIdSplitSign +Thread.currentThread().getId();

        //初始获得的应该为从父线程继承而来的，可以获得是谁创建了该线程
        if(threadCallChain.get()!=null && !threadCallChain.get().empty()) {
            CallInfo currentCallInfo = threadCallChain.get().peek();
            if (currentCallInfo != null) {
                threadInfo.setCreatorCallChainId(currentCallInfo.getCallChain().getCallChainId());
                threadInfo.setCreatorCallInfoId(currentCallInfo.getCallInfoid());
            }
        }

        threadCallChain.set(new Stack<>());

        ServiceManager.CallChainCreated(this);
    }

    protected abstract void methodStart(Method method);

    protected abstract void methodEnd(Method method);

    /**
     * 获得后序遍历到最近一次调用的长度（callinfo的个数）
     * @return
     */
    public int getLastChainLength(){
        int result=0;
        Iterator<CallInfo> iterator = iterator();
        while (iterator.hasNext()){
            iterator.next();
            result++;
        }
        return result;
    }
    /**
     * 获得后序遍历到currentcall的长度（callinfo的个数）
     * @return
     */
    public int getCurrentDepth(){
        int result=0;
        Iterator<CallInfo> iterator = iterator();
        while (iterator.hasNext()){
            CallInfo callInfo = iterator.next();
            result++;
            if (callInfo == currentCall)
                return result;
        }
        return result;
    }

    public CallInfo getRootCall() {
        return rootCall;
    }

    public CallInfo getCurrentCall() {
        return currentCall;
    }

    public String getCallChainId() {
        return callChainId;
    }

    public Stack<CallInfo> getThreadCallChain() {
        return threadCallChain.get();
    }

    public void setThreadCallChain(Stack<CallInfo> stack) {
        threadCallChain.set(stack);
    }

    public Long getPreEndTime() {
        return preEndTime;
    }

    public void setPreEndTime(Long preEndTime) {
        this.preEndTime = preEndTime;
    }

    @Override
    public String toString() {
        return "CallChain{" +
                "callChainId='" + callChainId + '\'' +
                "threadInfo=" + threadInfo +
                '}';
    }
}
