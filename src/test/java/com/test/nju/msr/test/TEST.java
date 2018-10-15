package com.test.nju.msr.test;

import com.nju.msr.core.model.Actions;
import org.junit.Test;

/**
 * @Author: jiaqi li
 * @Date: 2018/9/30 0030 19:17
 * @Version 1.0
 */
public class TEST {

    public static void main(String[] args) {
        String i = "5";
        try{
            func1();
        }catch (Exception e){
            System.err.println(e.getClass());
        }
        System.out.println(i);
    }

    public static void func1(){
        TEST test = new TEST();
        try {
            test.func2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void func2() throws Exception {
        try {
            TEST.fun3();
            try {
                throw new Exception();
            }catch (Exception e){
                throw e;
            }
        }finally {
            System.out.println();
        }
    }

    public static void fun3(){
        try {
            TEST.fun4();
            return;
        }finally {
            System.out.println();
        }
    }
    public static void fun4(){

    }
}
