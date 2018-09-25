package com.nju.msr.core.persistence;

import com.nju.msr.core.model.CallChain;
import com.nju.msr.core.model.CallInfo;
import com.nju.msr.core.model.Method;
import com.nju.msr.core.model.MethodRelation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServiceManager {

    private static final List<Service> serviceList = new ArrayList<>();

    public static void registerService(Service service){
        serviceList.add(service);
    }

    public static void MethodCreated(Method method) {
        for (Service service: serviceList){
            service.MethodCreated(method);
        }
    }

    public static void MethodRelationCreated(MethodRelation methodRelation) {

    }

    public static void MethodRelationCount(MethodRelation methodRelation, int count) {

    }

    public static void CallChainCreated(CallChain callChain) {

    }

    public static void CallCreated(CallInfo callInfo) {

    }
}
