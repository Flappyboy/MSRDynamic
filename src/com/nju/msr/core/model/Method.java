package com.nju.msr.core.model;

import com.nju.msr.core.Constant;
import com.nju.msr.utils.StringUtil;

import java.io.Serializable;
import java.util.*;

/**
 * 表示一个方法，通过asm可以获得方法的类名、方法名、签名等可以唯一确定一个方法，但通过堆栈只能获得类名方法名，无法确定一个方法。
 */
public class Method implements Serializable, Constant {

    //类名
    private String owner;

    //方法名
    private String name;

    //方法的输入输出类型
    private String descriptor;

    //构造出的唯一标识
    private String id;

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

    static public String generateId(String owner, String name, String descriptor){
        if (StringUtil.isAnyBlank(owner,name))
            throw new RuntimeException("输入值不能为空");
        owner = owner.replace('.','/');
        return owner+methodIdSplitSign+name+methodIdSplitSign+descriptor;
    }
    static public Map<String, String> resolveId(String id){
        String[] value = id.split(methodIdSplitSign);
        if (value.length<2||StringUtil.isAnyBlank(value[0],value[1]))
            throw new RuntimeException("id值错误 "+id);
        Map<String, String> result = new HashMap<>();
        result.put("owner",value[0]);
        result.put("name",value[1]);
        if(value.length>=3)
            result.put("descriptor",value[2]);
        return result;
    }

    @Override
    public String toString() {
        return "Method{" +
                "owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", descriptor='" + descriptor + '\'' +
                '}';
    }
}
