package com.nju.msr.core.asm;

import com.nju.msr.core.model.MethodFactory;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class ClassAdapter extends ClassVisitor implements Opcodes {

    private String owner;
    private boolean isInterface;

    public ClassAdapter(final ClassVisitor cv) {
        super(ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;


        /*if (owner.startsWith("org/hsqldb")){
            for (String iface: interfaces) {
                System.out.println(iface);
                if (iface.equals("java/sql/Connection")){
                    System.out.println("----------------------------------------");
                    System.out.println("Find java.sql.Connection    "+name);
                }
            }
        }*/
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        //if (!isInterface && mv != null && !"<init>".equals(name) && !"<clinit>".equals(name)) {
        if (!isInterface && mv != null) {
            mv = new MethodAdapter3(mv, owner, access, name, desc, signature, exceptions);
            //mv = new MethodAdapter2(mv, owner, access, name, desc, signature, exceptions);
            MethodFactory.getInstance().getMethod(owner,name,desc);
            //mv = new MethodAdapter4(mv, owner, access, name, desc, signature, exceptions);
        }
        return mv;
    }
}
