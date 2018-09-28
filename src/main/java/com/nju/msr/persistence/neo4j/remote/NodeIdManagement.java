package com.nju.msr.persistence.neo4j.remote;

import com.nju.msr.core.model.Method;
import com.nju.msr.core.model.MethodRelation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeIdManagement {

    private static final Map<Method, Long> methodIdMap = new ConcurrentHashMap<>();

    public static void addMethodNode(Method method, long id){
        methodIdMap.put(method, id);
    }
    public static long getNodeId(Method method){
        return methodIdMap.get(method);
    }

    private static final Map<MethodRelation, Long> methodRelationIdMap = new ConcurrentHashMap<>();

    public static void addMethodRelationNode(MethodRelation methodRelation, long id){
        methodRelationIdMap.put(methodRelation, id);
    }
    public static Long getNodeId(MethodRelation methodRelation){
        return methodRelationIdMap.get(methodRelation);
    }
}
