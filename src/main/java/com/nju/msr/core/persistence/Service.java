package com.nju.msr.core.persistence;

import com.nju.msr.core.model.call.CallChain;
import com.nju.msr.core.model.call.CallInfo;
import com.nju.msr.core.model.method.Method;
import com.nju.msr.core.model.method.MethodRelation;

import java.util.Map;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */

/**
 * 持久化服务所需要实现的方法
 */
public interface Service {

    void MethodCreated(Method method);

    void MethodRelationCreated(MethodRelation methodRelation);

    void MethodRelationCount(MethodRelation methodRelation, int count);

    void MethodRelations(Map<String, MethodRelation> methodRelationMap);

    void CallChainCreated(CallChain callChain);

    void CallCreated(CallInfo callInfo);

    void CallChainFinish(CallChain callChain);
}
