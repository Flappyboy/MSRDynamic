package com.test.nju.msr.test;

import org.junit.Test;

import java.io.IOException;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class ReturnTest {

    @Test
    public void test01(){
        System.out.println("content");
    }

    @Test
    public void test02(){
        try {
            test02fun1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test02fun1() throws Exception {

        System.out.println("throw content");
        throw new Exception();

    }


    @Test
    public void test03(){
        try {
            test03fun1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test03fun1() {
        try{
            System.out.println("test03fun1 try exception");
            throw new Exception();
        }catch (Exception e){
            System.out.println("test03fun1 catch exception");
        }
    }

    @Test
    public void test04(){

        try {
            test04fun1();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public void test04fun1() throws IOException, IllegalAccessException {
        try {
            test04fun2();
        } catch (IOException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        }
    }

    public void test04fun2() throws IOException, IllegalAccessException {
        if(true){
            throw new IOException();
        }else{
            throw new IllegalAccessException();
        }

    }
}
