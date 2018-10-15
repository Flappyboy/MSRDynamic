package com.nju.msr.core.asm;

import com.nju.msr.core.model.Actions;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
class MethodAdapter2 extends AdviceAdapter implements Opcodes {

    protected String className = null;
    protected int access = -1;
    protected String name = null;
    protected String desc = null;
    protected String signature = null;
    protected String[] exceptions = null;

    private Label L0 = new Label();
    private Label L1 = new Label();

    public MethodAdapter2(final MethodVisitor mv, final String className, final int access, final String name,
                          final String desc, final String signature, final String[] exceptions) {
        super(ASM6, mv, access, name, desc);
        this.className = className;
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
    }

    @Override
    protected void onMethodEnter() {
        mv.visitLdcInsn(className);
        mv.visitLdcInsn(name);
        mv.visitLdcInsn(desc);

        mv.visitMethodInsn(INVOKESTATIC, Actions.path,"methodStart","(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",false);

        // set up try-catch block for RuntimeException


        // started the try block
        mv.visitLabel(L0);
        super.onMethodEnter();
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
            mv.visitLdcInsn(className);
            mv.visitLdcInsn(name);
            mv.visitLdcInsn(desc);
            mv.visitMethodInsn(INVOKESTATIC, Actions.path, "methodEnd", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
        }

        super.onMethodExit(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        mv.visitLabel(L1);

        //mv.visitVarInsn(ASTORE, 10000);

        mv.visitLdcInsn(className);
        mv.visitLdcInsn(name);
        mv.visitLdcInsn(desc);
        mv.visitMethodInsn(INVOKESTATIC, Actions.path,"methodEnd","(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",false);


        //mv.visitVarInsn(ALOAD, 10000);
        mv.visitInsn(ATHROW);
        mv.visitTryCatchBlock(L0, L1,
                L1, "java/lang/Throwable");
       /* // here we could for example do e.printStackTrace()
        visitVarInsn(ALOAD, 2); // load it
        visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception",
                "printStackTrace", "()V", false);*/

        // exception handler ends here:
        //visitLabel(lCatchBlockEnd);

        super.visitMaxs(maxStack+3, maxLocals);
    }
    /*@Override
    public void visitMaxs(int maxStack, int maxLocals) {
        visitJumpInsn(GOTO, lTryBlockEnd);
        visitLabel(lCatchBlockStart);
        visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception",
                "printStackTrace", "()V", false);

        visitInsn(RETURN);
        visitLabel(lCatchBlockEnd);

        super.visitMaxs(maxStack, maxLocals);
    }*/
    /*@Override
    public void visitCode() {
        super.visitCode();
        mv.visitLdcInsn(className);
        mv.visitLdcInsn(name);
        mv.visitLdcInsn(desc);

        mv.visitMethodInsn(INVOKESTATIC, Actions.path,"methodStart","(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",false);

        // set up try-catch block for RuntimeException
        visitTryCatchBlock(lTryBlockStart, lTryBlockEnd,
                lCatchBlockStart, "java/lang/Exception");

        // started the try block
        visitLabel(lTryBlockStart);

        *//*mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("CALL classname:"+ className+ " access"+access+" name:" + name +" desc"+desc+" singature:"+signature);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);*//*
    }*/

    /*@Override
    public void visitInsn(int opcode) {
        //TODO 结合ReturnTest内容，对抛出异常情况的退出程序做进一步处理

        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
            mv.visitLdcInsn(className);
            mv.visitLdcInsn(name);
            mv.visitLdcInsn(desc);
            mv.visitMethodInsn(INVOKESTATIC, Actions.path,"methodEnd","(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",false);
        }
        super.visitInsn(opcode);
    }*/

   /* @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        *//* TODO: System.err.println("CALL" + name); *//*
        *//*mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("CALL "+opcode+" " + owner +" "+name+" "+desc);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);*//**//*

        /* do call *//*
        super.visitMethodInsn(opcode, owner, name, desc, itf);

        *//* TODO: System.err.println("RETURN" + name);  *//*
    }*/
}
