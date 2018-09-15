package com.nju.msr.core.model;

import com.nju.msr.core.Constant;

public class MethodRelation implements Constant {
    private Method caller;

    private Method callee;

    private String id;

    public MethodRelation(Method caller, Method callee) {
        this.caller = caller;
        this.callee = callee;
        this.id = generateId(caller,callee);
    }

    public static String generateId(Method caller, Method callee){
        String callerId = caller==null? "":caller.getId();
        if (callee==null)
            throw new RuntimeException("被调函数不能为空");
        return callerId+methodRelationIdSplitSign+callee.getId();
    }

    public Method getCaller() {
        return caller;
    }

    public Method getCallee() {
        return callee;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MethodRelation{" +
                "caller=" + caller +
                ", callee=" + callee +
                '}';
    }
}
