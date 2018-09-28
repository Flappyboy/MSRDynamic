package com.nju.msr.core.persistence;

import com.nju.msr.core.model.CallChain;
import com.nju.msr.core.model.CallInfo;
import com.nju.msr.core.model.Method;
import com.nju.msr.core.model.MethodRelation;

import java.util.Map;

public interface Service {

    void MethodCreated(Method method);

    void MethodRelationCreated(MethodRelation methodRelation);

    void MethodRelationCount(MethodRelation methodRelation, int count);

    void MethodRelations(Map<String, MethodRelation> methodRelationMap);

    void CallChainCreated(CallChain callChain);

    void CallCreated(CallInfo callInfo);

    void CallChainFinish(CallChain callChain);
}
