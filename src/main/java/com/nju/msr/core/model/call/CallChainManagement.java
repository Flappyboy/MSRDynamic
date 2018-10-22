package com.nju.msr.core.model.call;

import com.nju.msr.core.model.method.Method;
import com.nju.msr.core.persistence.ServiceManager;
import com.nju.msr.utils.StackUtil;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

//方法结束时的完善，线程之间关系的处理，持久化的完善
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class CallChainManagement {

    private static final CallChainManagement INSTANCE = new CallChainManagement();

    private static Map<Long, CallChain> callChainMap = new ConcurrentHashMap<>();

    //protected static Strategy strategy = new BaseStrategy();
    protected static Strategy strategy = new BaseStrategyWithCurrentCall();

    private CallChainManagement() {
        //startCheckThread();
    }

    public static CallChainManagement getInstance() {
        return INSTANCE;
    }

    private CallChain getCurrentCallChain() {
        CallChain callChain = callChainMap.get(Thread.currentThread().getId());
        if (callChain == null) {
            callChain = new CallChainStack();
            callChainMap.put(Thread.currentThread().getId(),callChain);
        }
        return callChain;
    }

    public void methodStart(Method method){

        //boolean isRootMehtod = StackUtil.isRootMethod();
        boolean existCallChain = callChainMap.get(Thread.currentThread().getId())!=null;


        if (existCallChain){
            CallChain callChain = getCurrentCallChain();

            if (callChain.getPreEndTime()!=null){
                //System.out.println(System.currentTimeMillis()-callChain.getPreEndTime());
                if ((System.currentTimeMillis()-callChain.getPreEndTime())>100)
                    callChainEnd();
                callChain.setPreEndTime(null);
            }
        }
        /*if(isRootMehtod && existCallChain)
            callChainEnd();*/

        getCurrentCallChain().methodStart(method);
    }

    /**
     * 当方法结束时，会调用该方法，但对于构造函数，若调用父类构造器抛出异常退出的情况，无法调用该函数
     * @param method
     */
    public void methodEnd(Method method){
        //TODO 处理method相关内容

        CallChain callChain = getCurrentCallChain();

        callChain.methodEnd(method);

        //判断调用链是否结束
        if (StackUtil.isRootMethod())
            callChainPreEnd();
    }

    public void callChainPreEnd(){
        getCurrentCallChain().setPreEndTime(System.currentTimeMillis());
    }
    public void callChainEnd(){
        callChainEnd(Thread.currentThread().getId());
    }
    public void callChainEnd(long key){
        ServiceManager.CallChainFinish(callChainMap.get(key));
        getCurrentCallChain().setThreadCallChain(new Stack<>());
        callChainMap.remove(key);
    }

    /*private void startCheckThread(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<String, CallChainInfo> entry : callChainInfoMap.entrySet()) {
                    CallChainInfo callChainInfo = entry.getValue();
                    Thread thread = callChainInfo.getThread();

                    if (thread.isAlive() ){
                    }
                    String key = entry.getKey();
                    save(callChainInfo, key);
                }
            }
        });
    }*/
}
