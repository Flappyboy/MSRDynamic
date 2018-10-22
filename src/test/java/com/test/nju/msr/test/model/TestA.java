package com.test.nju.msr.test.model;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
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
    public void func3(){
        FutureTask<String> futureTask1 = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                TestD.func1();
                return null;
            }
        });
        ExecutorService executor = Executors.newFixedThreadPool(2);        // 创建线程池并返回ExecutorService实例
        executor.execute(futureTask1);  // 执行任务
    }
    public void func4(){
        try {
            throw new Exception();
        } catch (Exception e) {
            System.out.println("func4 e.print");
            e.printStackTrace();
        }
    }
    public void func5(){
        try {
            func5_1();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    private void func5_1() throws IllegalAccessException {
        try {
            throw new Exception();
        } catch (Exception e) {
            throw new IllegalAccessException();
        }
    }
    public void func6(){
        try {
            func6_1();
        }catch (Exception e){

        }
    }
    private void func6_1() {
        func6_2();
    }
    private void func6_2(){
        throw new RuntimeException();
    }
}
