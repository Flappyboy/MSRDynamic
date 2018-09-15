package com.nju.msr.core.instrument;

import com.nju.msr.core.Param;
import com.nju.msr.core.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class AopAgentTransformer implements ClassFileTransformer{

    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] transformed = null;

        if(!Param.isUnderPackage(className)){
            return classfileBuffer;
        }

        System.out.println("Transforming " + className);

        try {
            ClassReader cr = new ClassReader(new java.io.ByteArrayInputStream(classfileBuffer));
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassAdapter ca = new ClassAdapter(cw);
            cr.accept(ca, 0);
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
