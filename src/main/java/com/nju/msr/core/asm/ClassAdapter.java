package com.nju.msr.core.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
                                     final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        //if (!isInterface && mv != null && !"<init>".equals(name) && !"<clinit>".equals(name)) {
        if (!isInterface && mv != null) {
            mv = new MethodAdapter(mv, owner, access, name, desc, signature, exceptions);
        }
        return mv;
    }
}
