package com.nju.msr.core;

import com.nju.msr.core.model.MethodRelation;

public class Param {
    public static String[] packageName = {};

    public static long MethodRelationSaveThreadSleepTime = 1000;
    public static long MethodRelationSaveIntervalTime= 10000;

    public static String saveCallChainInfoFilePath = "E:/workspace/test/data/CallChain.txt";
    public static String saveMethodRelationInfoFilePath = "E:/workspace/test/data/MethodRelation.txt";

    public static boolean isUnderPackage(String name){
        name = name.replaceAll("\\.","/");
        boolean flag=false;
        for (String s: Param.packageName){
            if (name.startsWith(s)){
                flag=true;
                break;
            }
        }
        return flag;
    }
}
