package com.nju.msr.core.asm;

import com.nju.msr.core.model.Actions;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
class MethodAdapter3 extends MyAdviceAdapter implements Opcodes {

    protected String className = null;
    protected int access = -1;
    protected String name = null;
    protected String desc = null;
    protected String signature = null;
    protected String[] exceptions = null;

    private Label L0 = new Label();
    private Label L1 = new Label();

    public MethodAdapter3(final MethodVisitor mv, final String className, final int access, final String name,
                          final String desc, final String signature, final String[] exceptions) {
        super(ASM6, mv, access, name, desc, className);
        this.className = className;
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
    }



    @Override
    protected void onMethodEnter() {


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
}
