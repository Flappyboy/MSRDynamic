package com.nju.msr.instrument;

import com.nju.msr.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class AopAgentTransformer implements ClassFileTransformer{

    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        if(!className.startsWith("org/mybatis/jpetstore")&&!className.startsWith("com/demo")){
            return classfileBuffer;
        }
        byte[] transformed = null;
        System.out.println("Transforming " + className);

        try {
            ClassReader cr = new ClassReader(new java.io.ByteArrayInputStream(classfileBuffer));
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassAdapter ca = new ClassAdapter(cw);
            cr.accept(ca, 0);
            transformed = cw.toByteArray();
        } catch (Exception e) {
            System.err.println("Could not instrument  " + className
                    + ",  exception : " + e.getMessage());
        }
        return transformed;
    }

}
