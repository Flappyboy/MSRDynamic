package com.nju.msr.utils;

import com.nju.msr.core.Param;
import com.nju.msr.core.model.call.CallChain;
import com.nju.msr.core.model.call.CallInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class StackUtil {

    /**
     * 将堆栈中的第一个方法和本项目的类过滤
     */
    public static List<StackTraceElement> processedStackTrace(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        List<StackTraceElement> extractStackTraceElements = new ArrayList<>();
        for (int i=1; i<stackTraceElements.length; i++) {
            StackTraceElement s = stackTraceElements[i];
            if (s.getClassName().startsWith("com.nju.msr"))
                continue;
            extractStackTraceElements.add(s);
        }
        return extractStackTraceElements;
    }

    //判断堆栈中当前方法之前的方法是否全部都是非关注的类
    public static boolean isRootMethod(){
        List<StackTraceElement> stackTraceElements = processedStackTrace();
        //去除当前方法
        stackTraceElements.remove(0);
        for (StackTraceElement s: stackTraceElements) {
            if (Param.isUnderPackage(s.getClassName()))
                return false;
        }
        return true;
    }

    /**
     * 将当前堆栈中的目标项目的最近方法删除，并同时删除其上相连的非目标项目方法直到找到目标项目方法
     * @return
     */
    public static List<StackTraceElement> getStackWithoutCurrentCall(){
        List<StackTraceElement> stackTraceElements = processedStackTrace();
        //去除当前方法
        stackTraceElements.remove(0);
        List delList = new ArrayList();
        Iterator<StackTraceElement> sit = stackTraceElements.iterator();
        while(sit.hasNext()){
            StackTraceElement s = sit.next();
            if (!Param.isUnderPackage(s.getClassName()))
                //stackTraceElements.remove(s);
                delList.add(s);
            else
                break;
        }
        stackTraceElements.removeAll(delList);
        return stackTraceElements;
    }
    /**
     * 将非目标项目方法全部删除
     * @return
     */
    public static List<StackTraceElement> getStackWithoutOther(){
        List<StackTraceElement> stackTraceElements = processedStackTrace();

        Iterator<StackTraceElement> sit = stackTraceElements.iterator();
        while(sit.hasNext()){
            StackTraceElement s = sit.next();
            if (!Param.isUnderPackage(s.getClassName()))
                stackTraceElements.remove(s);
        }
        return stackTraceElements;
    }
}
