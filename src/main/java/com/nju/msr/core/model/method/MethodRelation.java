package com.nju.msr.core.model.method;

import com.nju.msr.core.Constant;
import com.nju.msr.core.persistence.ServiceManager;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */

/**
 * 表示两个方法之间的关系
 */
public class MethodRelation implements Constant {
    private Method caller;

    private Method callee;

    private String id;

    private AtomicInteger count = new AtomicInteger(0);

    public MethodRelation(Method caller, Method callee) {
        if (caller==null){
            caller = MethodFactory.getInstance().getMethod("*START*","*START*");
        }
        this.caller = caller;
        this.callee = callee;
        this.id = generateId(caller,callee);

        ServiceManager.MethodRelationCreated(this);
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

    public int getCount() {
        return count.get();
    }

    public void call(){
        ServiceManager.MethodRelationCount(this, this.count.incrementAndGet());
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    @Override
    public String toString() {
        return "MethodRelation{" +
                "caller=" + caller +
                ", callee=" + callee +
                '}';
    }
}
