package com.test.nju.msr.test.model;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class TestD {
    public static int n = 1;
    public static void func1(){
        System.out.println("TestD func1");
        if (n==1){
            n=0;
            TestC.func1();
        }
    }
}
