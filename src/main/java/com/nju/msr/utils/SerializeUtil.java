package com.nju.msr.utils;

import com.nju.msr.core.model.CallChain;
import com.nju.msr.core.model.CallInfo;
import com.nju.msr.core.model.MethodRelation;

import java.io.*;
import java.util.Map;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class SerializeUtil{
    public static StringBuilder serializeToString(Map<String, MethodRelation> methodRelationMap){
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, MethodRelation> methodRelationEntry : methodRelationMap.entrySet()) {
            MethodRelation methodRelation = methodRelationEntry.getValue();
            stringBuilder.append(methodRelation+"\n");
        }
        return stringBuilder;
    }

    public static StringBuilder serializeToString(CallChain callChain){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(callChain);
        stringBuilder.append("\n");
        stringBuilder.append(serializeToString(callChain.getRootCall(),0));
        return stringBuilder;
    }

    public static StringBuilder serializeToString(CallInfo callInfo, int level){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mutiChar("    ",level)).append(callInfo.toString() + "\n");
        for (int i=0; i<callInfo.getChildCallListSize(); i++) {
            CallInfo tmp = callInfo.getChildCall(i);
            stringBuilder.append(serializeToString(tmp,level+1));
        }
        return stringBuilder;
    }
    public static StringBuilder mutiChar(String str, int count){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<count; i++){
            stringBuilder.append(str);
        }
        return stringBuilder;
    }

    public static String serializeToString(Object obj) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(obj);
        String str = byteOut.toString("ISO-8859-1");//此处只能是ISO-8859-1,但是不会影响中文使用
        return str;
    }
    //反序列化
    public static Object deserializeToObject(String str) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        Object obj =objIn.readObject();
        return obj;
    }
}
