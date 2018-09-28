package com.nju.msr.core.model;

import com.nju.msr.core.Param;
import com.nju.msr.core.persistence.Service;
import com.nju.msr.core.persistence.ServiceManager;
import com.nju.msr.utils.FileUtil;
import com.nju.msr.utils.SerializeUtil;
import com.nju.msr.utils.StackUtil;

import java.util.ArrayList;
import java.util.List;
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

    private CallChainManagement() {
        //startCheckThread();
    }

    public static CallChainManagement getInstance() {
        return INSTANCE;
    }

    private CallChain getCurrentCallChain() {
        CallChain callChain = callChainMap.get(Thread.currentThread().getId());
        if (callChain == null) {
            callChain = new CallChain();
            callChainMap.put(Thread.currentThread().getId(),callChain);
        }
        return callChain;
    }

    public void methodStart(Method method){

        boolean isRootMehtod = StackUtil.isRootMethod();
        if (isRootMehtod && callChainMap.get(Thread.currentThread().getId())!=null){
            callChainEnd();
        }

        getCurrentCallChain().methodStart(method);
    }

    /**
     * 由于无法可靠地保证获得方法结束信息，所以当前这个方法在方法结束时并不一定会调用，
     * 目前通过methodStart方法开头检查和另开线程检查线程存活来解决
     */
    public void methodEnd(Method method){
        //TODO 处理method相关内容

        CallChain callChain = getCurrentCallChain();
        //save(callChain,Thread.currentThread().getId());

        callChain.methodEnd(method);

        //判断调用链是否结束
        if (StackUtil.isRootMethod())
            callChainEnd();
    }
    public void callChainEnd(){
        callChainEnd(Thread.currentThread().getId());
    }
    public void callChainEnd(long key){
        ServiceManager.CallChainFinish(callChainMap.get(key));
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
