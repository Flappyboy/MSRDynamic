package com.nju.msr.core;

public class Param {
    public static String[] packageName = {};

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
