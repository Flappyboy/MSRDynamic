package com.nju.msr.core.model;

import com.nju.msr.core.Param;
import com.nju.msr.core.persistence.ServiceManager;
import com.nju.msr.utils.FileUtil;
import com.nju.msr.utils.SerializeUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodRelationManagement {

    long time = System.currentTimeMillis();
    boolean changeFlag = false;

    private static final MethodRelationManagement INSTANCE = new MethodRelationManagement();

    private MethodRelationManagement() {
        startSaveMethodRelationThread();
    }

    private Map<String, MethodRelation> methodRelationMap = new ConcurrentHashMap<String, MethodRelation>();

    public static MethodRelationManagement getInstance() {
        return INSTANCE;
    }

    public MethodRelation callMethodRelation(Method caller, Method callee) {
        MethodRelation methodRelation = getMethodRelation(caller,callee);
        methodRelation.call();
        return methodRelation;
    }

    public MethodRelation getMethodRelation(Method caller, Method callee){
        String id = MethodRelation.generateId(caller,callee);
        MethodRelation methodRelation = methodRelationMap.get(id);
        if (methodRelation==null){
            synchronized (methodRelationMap) {
                methodRelation = methodRelationMap.get(id);
                if (methodRelation==null) {
                    methodRelation = new MethodRelation(caller, callee);
                    methodRelationMap.put(methodRelation.getId(), methodRelation);
                    time = System.currentTimeMillis();
                    changeFlag = true;
                }
            }
        }
        return methodRelation;
    }

    private void startSaveMethodRelationThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(Param.MethodRelationSaveThreadSleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ((System.currentTimeMillis() - time)>Param.MethodRelationSaveIntervalTime){
                        if (changeFlag){
                            changeFlag = false;
                            ServiceManager.MethodRelations(methodRelationMap);
                        }
                    }
                }
            }
        }).start();
    }
}
