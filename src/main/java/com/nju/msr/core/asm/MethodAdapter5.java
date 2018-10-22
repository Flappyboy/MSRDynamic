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
class MethodAdapter5 extends AdviceAdapter implements Opcodes {

    protected String className = null;
    protected int access = -1;
    protected String name = null;
    protected String desc = null;
    protected String signature = null;
    protected String[] exceptions = null;

    public MethodAdapter5(final MethodVisitor mv, final String className, final int access, final String name,
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
        super.onMethodEnter();
    }

    @Override
    protected void onMethodExit(int opcode) {
            mv.visitLdcInsn(className);
            mv.visitLdcInsn(name);
            mv.visitLdcInsn(desc);
            mv.visitMethodInsn(INVOKESTATIC, Actions.path, "methodEnd", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);


        super.onMethodExit(opcode);
    }
}
