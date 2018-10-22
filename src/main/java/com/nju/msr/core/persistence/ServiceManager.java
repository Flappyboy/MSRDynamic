package com.nju.msr.core.persistence;

import com.nju.msr.core.Param;
import com.nju.msr.core.model.call.CallChain;
import com.nju.msr.core.model.call.CallInfo;
import com.nju.msr.core.model.method.Method;
import com.nju.msr.core.model.method.MethodRelation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */

/**
 * 在core包下的一些值得记录的方法处调用了该类的相关方法。
 */
public class ServiceManager {

    private static final List<Service> serviceList = new ArrayList<>();

    public static void registerService(Service service){
        serviceList.add(service);
    }

    public static void MethodCreated(Method method) {
        if(!Param.persistenceServiceOpen) return;

        /*if (Param.isUnderPackage(method.getOwner())&& method.getType()== Method.TYPE.OTHER)
            return;*/
        for (Service service: serviceList){
            service.MethodCreated(method);
        }
    }

    public static void MethodRelationCreated(MethodRelation methodRelation) {
        if(!Param.persistenceServiceOpen) return;

        for (Service service: serviceList){
            service.MethodRelationCreated(methodRelation);
        }
    }

    public static void MethodRelationCount(MethodRelation methodRelation, int count) {
        if(!Param.persistenceServiceOpen) return;

        for (Service service: serviceList){
            service.MethodRelationCount(methodRelation, count);
        }
    }

    public static void MethodRelations(Map<String, MethodRelation> methodRelationMap) {
        if(!Param.persistenceServiceOpen) return;

        for (Service service: serviceList){
            service.MethodRelations(methodRelationMap);
        }
    }

    public static void CallChainCreated(CallChain callChain) {
        if(!Param.persistenceServiceOpen) return;

        for (Service service: serviceList){
            service.CallChainCreated(callChain);
        }
    }

    public static void CallCreated(CallInfo callInfo) {
        if(!Param.persistenceServiceOpen) return;

        for (Service service: serviceList){
            service.CallCreated(callInfo);
        }
    }

    public static void CallChainFinish(CallChain callChain) {
        if(!Param.persistenceServiceOpen) return;

        for (Service service: serviceList){
            service.CallChainFinish(callChain);
        }
    }
}
