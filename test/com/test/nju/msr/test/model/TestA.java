package com.test.nju.msr.test.model;

public class TestA {
    public void func1(){
        System.out.println("TestA func1");
        TestB.func1();
    }
    public void func2(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TestB.func1();
            }
        }).start();
    }
}
