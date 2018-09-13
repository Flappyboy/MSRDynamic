package com.nju.msr.model;

import java.util.HashMap;
import java.util.Map;

public class MethodPool {

    private static final MethodPool INSTANCE = new MethodPool();

    private MethodPool() {}

    public static MethodPool getInstance() {
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
}
