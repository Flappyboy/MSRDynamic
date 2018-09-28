package com.nju.msr.core.model;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */

/**
 * 使用ASM在目标类的方法的开头结尾以及相关位置调用这些方法
 */
public class Actions {
    static public final String path = Actions.class.getName().replace('.','/');

    static public void methodStart(String owner, String name, String desc){
        CallChainManagement.getInstance().methodStart(MethodFactory.getInstance().getMethod(owner,name,desc));
    }

    static public void methodEnd(String owner, String name, String desc){
        CallChainManagement.getInstance().methodEnd(MethodFactory.getInstance().getMethod(owner,name,desc));
    }

    static public void startInvoke(int opcode, String owner, String name, String desc, boolean itf){
        System.out.println("方法  " + Thread.currentThread().getStackTrace()[3] + "  调用  " + Thread.currentThread().getStackTrace()[2]);
    }

    static public void endInvoke(){
        System.out.println("方法  " + Thread.currentThread().getStackTrace()[3] + "  调用  " + Thread.currentThread().getStackTrace()[2]);
    }
}
