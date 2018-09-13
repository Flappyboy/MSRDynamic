package com.nju.msr;

public class Data {

    String msg;

    String owner;
    String name;
    String desc;

    private void vb(){
        Actions.methodInvoked(owner,name,desc);
    }
}
