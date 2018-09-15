package com.nju.msr.core.model;

import com.nju.msr.core.Param;
import com.nju.msr.utils.FileUtil;
import com.nju.msr.utils.SerializeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//方法结束时的完善，线程之间关系的处理，持久化的完善

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
            callChain = new CallChain(Thread.currentThread());
            callChainMap.put(Thread.currentThread().getId(),callChain);
        }
        return callChain;
    }

    public void methodStart(Method method){


        if (isRootMethod() && callChainMap.get(Thread.currentThread().getId())!=null){
            callChainEnd();
        }


        CallChain callChain = getCurrentCallChain();
        callChain.addCallInfoThroughStackTrace(processedStackTrace(),method);
    }

    /**
     * 由于无法可靠地保证获得方法结束信息，所以当前这个方法在方法结束时并不一定会调用，
     * 目前通过methodStart方法开头检查和另开线程检查线程存活来解决
     */
    public void methodEnd(Method method){
        //TODO 处理method相关内容

        CallChain callChain = getCurrentCallChain();
        //save(callChain,Thread.currentThread().getId());
        if (callChain.getCurrentCall()!=null)
            callChain.getCurrentCall().setEndTime(System.currentTimeMillis());
        if (isRootMethod())
            callChainEnd();
    }

    //判断当前方法之前的方法是否全部都是非关注的类
    private boolean isRootMethod(){
        List<StackTraceElement> stackTraceElements = processedStackTrace();
        //去除当前方法
        stackTraceElements.remove(0);
        for (StackTraceElement s: stackTraceElements) {
            if (Param.isUnderPackage(s.getClassName()))
                return false;
        }
        return true;
    }
    public void callChainEnd(){
        callChainEnd(Thread.currentThread().getId());
    }
    public void callChainEnd(long key){
        save(callChainMap.get(key),key);
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

    //TODO 目前只是简化的
    private synchronized void save(CallChain callChain, long key) {
//        try {
            String str = key + " " + SerializeUtil.serializeToString(callChain.getRootCall(),0);
            FileUtil.appendContentToFile(Param.saveCallChainInfoFilePath, str);
        /*} catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 将堆栈中的第一个方法和本项目的类过滤调
     */
    public static List<StackTraceElement> processedStackTrace(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        List<StackTraceElement> extractStackTraceElements = new ArrayList<>();
        for (int i=1; i<stackTraceElements.length; i++) {
            StackTraceElement s = stackTraceElements[i];
            if (s.getClassName().startsWith("com.nju.msr"))
                continue;
            extractStackTraceElements.add(s);
        }
        return extractStackTraceElements;
//        CallChainManagement.getCurrentCallChain().processStackTrace(extractStackTraceElements, method);
    }
}
