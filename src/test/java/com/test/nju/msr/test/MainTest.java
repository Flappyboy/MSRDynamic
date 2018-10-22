package com.test.nju.msr.test;

import com.test.nju.msr.test.model.TestA;
import com.test.nju.msr.test.model.TestE;
import com.test.nju.msr.test.model.TestF;

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

    public static void main(String[] args) throws Exception {
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
        try {
            TestF testF = new TestF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*try {
            TestE testE = new TestF();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //TestE test = new TestF();
        //test.fun();
       /* try {
            TestF testF = new TestF();
            testF.ttt();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //TestE testE = new TestF();
        /*TestA testA = null;
        try {
            testA = new TestA();
        } catch (Exception e) {
            e.printStackTrace();
        }
        testA.func5();*/
    }
}
