package com.nju.msr.utils;

import com.nju.msr.core.Param;

import java.util.ArrayList;
import java.util.List;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class StackUtil {

    /**
     * 将堆栈中的第一个方法和本项目的类过滤调
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
}
