package com.test.nju.msr.test.model;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class TestB {
    public static void func1(){
        System.out.println("TestB func1");
        TestC.func1();
    }
}
