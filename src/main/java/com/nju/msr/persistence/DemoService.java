package com.nju.msr.persistence;

import com.nju.msr.core.Param;
import com.nju.msr.core.model.CallChain;
import com.nju.msr.core.model.CallInfo;
import com.nju.msr.core.model.Method;
import com.nju.msr.core.model.MethodRelation;
import com.nju.msr.core.persistence.Service;
import com.nju.msr.core.persistence.ServiceManager;
import com.nju.msr.utils.FileUtil;
import com.nju.msr.utils.SerializeUtil;

import java.util.Map;

public class DemoService implements Service {

    static {
        ServiceManager.registerService(new DemoService());
    }

    @Override
    public void MethodCreated(Method method) {

    }

    @Override
    public void MethodRelationCreated(MethodRelation methodRelation) {

    }

    @Override
    public void MethodRelationCount(MethodRelation methodRelation, int count) {

    }

    @Override
    public void MethodRelations(Map<String, MethodRelation> methodRelationMap) {
        String str = ""+SerializeUtil.serializeToString(methodRelationMap);
        FileUtil.addContentToFile(Param.saveMethodRelationInfoFilePath, str, false);
    }

    @Override
    public void CallChainCreated(CallChain callChain) {

    }

    @Override
    public void CallCreated(CallInfo callInfo) {

    }

    @Override
    public void CallChainFinish(CallChain callChain) {
        //try {
            String str = SerializeUtil.serializeToString(callChain).toString();
            FileUtil.appendContentToFile(Param.saveCallChainInfoFilePath, str);
        /*} catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
