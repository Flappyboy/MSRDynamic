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
import com.nju.msr.utils.StringUtil;

import java.util.Map;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class DemoService implements Service {

    public static String saveCallChainInfoFilePath = "CallChain.txt";
    public static String saveMethodRelationInfoFilePath = "MethodRelation.txt";

    static {
        String path1= Param.PROPERTIES.getProperty("demo.saveCallChainInfoFilePath");
        if (StringUtil.isNotBlank(path1))
            saveCallChainInfoFilePath = path1;
        String path2 = Param.PROPERTIES.getProperty("demo.saveMethodRelationInfoFilePath");
        if (StringUtil.isNotBlank(path2))
            saveMethodRelationInfoFilePath = path2;

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
        FileUtil.addContentToFile(saveMethodRelationInfoFilePath, str, false);
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
            FileUtil.appendContentToFile(saveCallChainInfoFilePath, str);
        /*} catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
