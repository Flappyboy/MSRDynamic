package com.nju.msr.model;

import com.nju.msr.utils.StringUtils;

import java.util.*;

public class Method {

    private String owner;

    private String name;

    private String descriptor;

    private String id;

    private Set<Method> childMethodSet = new HashSet<>();

    private Set<Method> parentMethodSet = new HashSet<>();

    public Method(String owner, String name, String descriptor) {
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
        this.id = generateId(owner,name,descriptor);
    }
    public Method(String id){
        this.id = id;
        Map<String, String> map = resolveId(id);
        this.owner= map.get("owner");
        this.name= map.get("name");
        this.descriptor= map.get("descriptor");
    }

    public Set<Method> getChildMethodSet() {
        return childMethodSet;
    }

    public Set<Method> getParentMethodSet() {
        return parentMethodSet;
    }

    public void addChildMethod(Method method){
        if (method != null)
            childMethodSet.add(method);
    }
    public void addParentMethod(Method method){
        if (method != null)
            parentMethodSet.add(method);
    }


    public String getDescriptor() {
        return descriptor;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getId(){
        return id;
    }

    private static final String splitSign = "%!#";
    static public String generateId(String owner, String name, String descriptor){
        if (StringUtils.isAnyBlank(owner,name,descriptor))
            throw new RuntimeException("输入值不能为空");
        owner.replace('.','/');
        return owner+splitSign+name+splitSign+descriptor;
    }
    static public Map<String, String> resolveId(String id){
        String[] value = id.split(splitSign);
        if (StringUtils.isAnyBlank(value)||value.length<3)
            throw new RuntimeException("id值错误 "+id);
        Map<String, String> result = new HashMap<>();
        result.put("owner",value[0]);
        result.put("name",value[1]);
        result.put("descriptor",value[2]);
        return result;
    }
}
