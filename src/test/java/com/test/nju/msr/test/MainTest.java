package com.test.nju.msr.test;

import com.test.nju.msr.test.model.TestA;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class MainTest {

    public MainTest() {
        try {
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TestA testA = new TestA();
        testA.func1();
        testA.func2();
        testA.func3();
        try {
            testA.func4();
        }catch (Exception e){
            System.out.println("haha");
            e.printStackTrace();
        }
        testA.func5();
        testA.func6();
    }
}
