package com.nju.msr.core.instrument;

import com.nju.msr.core.Param;
import com.nju.msr.core.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class AopAgentTransformer implements ClassFileTransformer{

    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {


        /*if(!Param.isUnderPackage(className)&&!className.startsWith("org/hsqldb")){
            return classfileBuffer;
        }*/
        //System.out.println("all  " + className);
        if(!Param.isUnderPackage(className)){
            return classfileBuffer;
        }

        System.out.println("Transforming " + className);

        byte[] transformed = null;
        try {
            ClassReader cr = new ClassReader(new java.io.ByteArrayInputStream(classfileBuffer));
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassAdapter ca = new ClassAdapter(cw);
            //cr.accept(new CheckClassAdapter(ca), ClassReader.EXPAND_FRAMES);
            cr.accept(ca, ClassReader.EXPAND_FRAMES);
            transformed = cw.toByteArray();
        }catch (RuntimeException re){
            System.err.println("can't transform "+ className+"  "+re);
            //re.printStackTrace();
        }catch (IOException e) {
            System.err.println("can't transform "+ className+"  "+e);
            //e.printStackTrace();
        }
        return transformed;
    }

}
