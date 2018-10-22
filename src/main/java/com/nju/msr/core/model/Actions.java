package com.nju.msr.core.model;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */

import com.nju.msr.core.model.call.CallChainManagement;
import com.nju.msr.core.model.method.MethodFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用ASM在目标类的方法的开头结尾以及相关位置调用这些方法
 */
public class Actions {
    static AtomicInteger ai = new AtomicInteger(0);
    static public final String path = Actions.class.getName().replace('.','/');

    static public void methodStart(String owner, String name, String desc){

        System.out.println(ai.incrementAndGet()+"                 methodstart: "+owner+name);
        CallChainManagement.getInstance().methodStart(MethodFactory.getInstance().getMethod(owner,name,desc));
    }

    static public void methodEnd(String owner, String name, String desc){
        System.out.println("                 methodEnd: "+owner+name);
        CallChainManagement.getInstance().methodEnd(MethodFactory.getInstance().getMethod(owner,name,desc));
    }

    static public void nativeSqlStart(String sql){

    }
    static public void nativeSqlEnd(String sql){

    }

    static public void startInvoke(int opcode, String owner, String name, String desc, boolean itf){
        System.out.println("方法  " + Thread.currentThread().getStackTrace()[3] + "  调用  " + Thread.currentThread().getStackTrace()[2]);
    }

    static public void endInvoke(){
        System.out.println("方法  " + Thread.currentThread().getStackTrace()[3] + "  调用  " + Thread.currentThread().getStackTrace()[2]);
    }
}
