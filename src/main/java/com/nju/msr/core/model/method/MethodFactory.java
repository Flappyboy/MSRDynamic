package com.nju.msr.core.model.method;

import java.util.HashMap;
import java.util.Map;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */

/**
 * 使用享元模式创建{@link Method}
 */
public class MethodFactory {

    private static final MethodFactory INSTANCE = new MethodFactory();

    private MethodFactory() {}

    public static MethodFactory getInstance() {
        return INSTANCE;
    }

    private Map<String, Method> methodMap = new HashMap<>();

    /**
     * 通过id标识获取method,若无则创建一个。
     * @param id
     * @return
     */
    public Method getMethod(String id){
        if (id==null||id.length()==0)
            return null;
        Method method = methodMap.get(id);
        if (method==null){
            synchronized (methodMap){
                method = methodMap.get(id);
                if (method==null) {
                    method = new Method(id);
                    methodMap.put(id, method);
                }
            }
        }
        return method;
    }

    public Method getMethod(String owner, String name){
        return getMethod(Method.generateId(owner,name, ""));
    }

    public Method getMethod(String owner, String name, String descriptor){
        return getMethod(Method.generateId(owner,name, descriptor));
    }
}
