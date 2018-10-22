package com.nju.msr.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */

/**
 * 一些初始化的参数
 */
public class Param {
    private Param() {}

    public static String[] packageName = {};
    public static String[] packageNameExclude = {};

    public static long MethodRelationSaveThreadSleepTime;
    public static long MethodRelationSaveIntervalTime;

    public static boolean persistenceServiceOpen = true;
    public static String PersistenceServiceName;

    public static final Properties PROPERTIES = new Properties();

    private static void generateParamsFromProperties(Properties properties){
        Param.packageName = properties.getProperty("packageName").replaceAll("\\.","/").split(Constant.ParamsPackageNameSplitSign);
        Param.packageNameExclude = properties.getProperty("packageNameExclude").replaceAll("\\.","/").split(Constant.ParamsPackageNameSplitSign);

        MethodRelationSaveThreadSleepTime = Long.parseLong(properties.getProperty("MethodRelationSaveThreadSleepTime","1000"));
        MethodRelationSaveIntervalTime = Long.parseLong(properties.getProperty("MethodRelationSaveIntervalTime","10000"));

        persistenceServiceOpen = Boolean.parseBoolean(properties.getProperty("persistenceServiceOpen","true"));
        //PersistenceServiceName = properties.getProperty("PersistenceServiceName","com.nju.msr.persistence.DemoService&#com.nju.msr.persistence.Sqlite.SqliteService&#com.nju.msr.persistence.neo4j.remote.Neo4jService");
        PersistenceServiceName = properties.getProperty("PersistenceServiceName","com.nju.msr.persistence.neo4j.remote.Neo4jService");
    }


    public static boolean isUnderPackage(String name){
        if (name.contains("$$"))
            return false;
        name = name.replaceAll("\\.","/");
        boolean flag=false;
        for (String s: Param.packageName){
            if (name.startsWith(s)){
                flag=true;
                break;
            }
        }
        if (flag){
            for (String s: Param.packageNameExclude){
                if (name.startsWith(s)){
                    flag=false;
                    break;
                }
            }
        }
        return flag;
    }

    public static void generatePARAMS(String argStr){
        String[] args=argStr.split(Constant.ParamsSplitSign);
        for (String arg: args){
            String[] str = arg.split(Constant.ParamsArgEqualSplitSign);
            if (str.length==2){
                if (Constant.ParamsPathName.equals(str[0])){
                    PROPERTIES.clear();
                    readProperties(str[1]);
                    for (String arg2: args){
                        String[] str2 = arg2.split(Constant.ParamsArgEqualSplitSign);
                        if (str2.length==2){
                            if (Constant.ParamsPathName.equals(str2[0]))
                                continue;
                            PROPERTIES.setProperty(str2[0],str2[1]);
                        }
                    }
                    break;
                }
                PROPERTIES.setProperty(str[0],str[1]);
            }
        }
        generateParamsFromProperties(PROPERTIES);
    }

    public static void readProperties(String filePath){
        try {
            PROPERTIES.load(new FileInputStream(new File(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
