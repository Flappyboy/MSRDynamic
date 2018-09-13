package com.nju.msr.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class MethodAdapter extends MethodVisitor implements Opcodes {

    protected String className = null;
    protected int access = -1;
    protected String name = null;
    protected String desc = null;
    protected String signature = null;
    protected String[] exceptions = null;

    public MethodAdapter(final MethodVisitor mv, final String className, final int access, final String name,
                         final String desc, final String signature, final String[] exceptions) {
        super(ASM5, mv);
        this.className = className;
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
    }

    @Override
    public void visitCode() {
        mv.visitMethodInsn(INVOKESTATIC, "com/nju/msr/Actions","methodInvoked","()V",false);

        /*mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("CALL classname:"+ className+ " access"+access+" name:" + name +" desc"+desc+" singature:"+signature);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);*/

        mv.visitCode();
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        /* TODO: System.err.println("CALL" + name); */
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("CALL "+opcode+" " + owner +" "+name+" "+desc);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        /* do call */
        mv.visitMethodInsn(opcode, owner, name, desc, itf);

        /* TODO: System.err.println("RETURN" + name);  */
    }
    static void printStack(){

    }
}
