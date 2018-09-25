package com.nju.msr.core.persistence;

import com.nju.msr.core.model.CallChain;
import com.nju.msr.core.model.CallInfo;
import com.nju.msr.core.model.Method;
import com.nju.msr.core.model.MethodRelation;

public interface Service {

    void MethodCreated(Method method);

    void MethodRelationCreated(MethodRelation methodRelation);

    void MethodRelationCount(MethodRelation methodRelation, int count);

    void CallChainCreated(CallChain callChain);

    void CallCreated(CallInfo callInfo);
}
