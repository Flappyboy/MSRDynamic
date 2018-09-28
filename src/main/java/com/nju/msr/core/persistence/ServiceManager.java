package com.nju.msr.core.persistence;

import com.nju.msr.core.Param;
import com.nju.msr.core.model.CallChain;
import com.nju.msr.core.model.CallInfo;
import com.nju.msr.core.model.Method;
import com.nju.msr.core.model.MethodRelation;
import com.nju.msr.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceManager {

    private static final List<Service> serviceList = new ArrayList<>();

    public static void registerService(Service service){
        serviceList.add(service);
    }

    public static void MethodCreated(Method method) {
        if (Param.isUnderPackage(method.getOwner())&& method.getType()== Method.TYPE.OTHER)
            return;
        for (Service service: serviceList){
            service.MethodCreated(method);
        }
    }

    public static void MethodRelationCreated(MethodRelation methodRelation) {
        for (Service service: serviceList){
            service.MethodRelationCreated(methodRelation);
        }
    }

    public static void MethodRelationCount(MethodRelation methodRelation, int count) {
        for (Service service: serviceList){
            service.MethodRelationCount(methodRelation, count);
        }
    }

    public static void MethodRelations(Map<String, MethodRelation> methodRelationMap) {
        for (Service service: serviceList){
            service.MethodRelations(methodRelationMap);
        }
    }

    public static void CallChainCreated(CallChain callChain) {
        for (Service service: serviceList){
            service.CallChainCreated(callChain);
        }
    }

    public static void CallCreated(CallInfo callInfo) {
        for (Service service: serviceList){
            service.CallCreated(callInfo);
        }
    }

    public static void CallChainFinish(CallChain callChain) {
        for (Service service: serviceList){
            service.CallChainFinish(callChain);
        }
    }
}
