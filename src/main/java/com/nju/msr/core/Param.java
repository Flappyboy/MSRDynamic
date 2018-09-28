package com.nju.msr.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Param {
    private Param() {}

    public static String[] packageName = {};

    public static long MethodRelationSaveThreadSleepTime = 1000;
    public static long MethodRelationSaveIntervalTime= 10000;

    public static String saveCallChainInfoFilePath = "E:/workspace/test/data/CallChain.txt";
    public static String saveMethodRelationInfoFilePath = "E:/workspace/test/data/MethodRelation.txt";

    public static final String PersistenceServiceName="com.nju.msr.persistence.DemoService&#com.nju.msr.persistence.Sqlite.SqliteService&#com.nju.msr.persistence.neo4j.remote.Neo4jService";

    public static final Properties PROPERTIES = new Properties();


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
        generatePackageName(PROPERTIES.getProperty("packageName"));
    }

    private static void generatePackageName(String str){
        Param.packageName = str.replaceAll("\\.","/").split(Constant.ParamsPackageNameSplitSign);
    }

    public static void readProperties(String filePath){
        try {
            PROPERTIES.load(new FileInputStream(new File(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
