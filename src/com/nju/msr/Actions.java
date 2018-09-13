package com.nju.msr;

public class Actions {

    static public void methodInvoked(){
        System.out.println("Thread: "+Thread.currentThread());
        System.out.println("id: "+Thread.currentThread().getId());
        System.out.println("name: "+Thread.currentThread().getName());
        System.out.println("state: "+Thread.currentThread().getState());
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        System.out.println("stack: ");
        for (StackTraceElement s: stackTraceElements) {
            System.out.println(s.getLineNumber()+ " "+s.getFileName());
            System.out.println(s);
        }
        //System.out.println("方法  " + Thread.currentThread().getStackTrace()[3] + "  调用  " + Thread.currentThread().getStackTrace()[2]);
    }

    static public void startInvoke(int opcode, String owner, String name, String desc, boolean itf){
        System.out.println("方法  " + Thread.currentThread().getStackTrace()[3] + "  调用  " + Thread.currentThread().getStackTrace()[2]);
    }

    static public void endInvoke(){
        System.out.println("方法  " + Thread.currentThread().getStackTrace()[3] + "  调用  " + Thread.currentThread().getStackTrace()[2]);
    }
}