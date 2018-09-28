package com.nju.msr.core.asm;

import com.nju.msr.core.model.Actions;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
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
        mv.visitLdcInsn(className);
        mv.visitLdcInsn(name);
        mv.visitLdcInsn(desc);

        mv.visitMethodInsn(INVOKESTATIC, Actions.path,"methodStart","(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",false);

        /*mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("CALL classname:"+ className+ " access"+access+" name:" + name +" desc"+desc+" singature:"+signature);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);*/

        mv.visitCode();
    }

    @Override
    public void visitInsn(int opcode) {
        //TODO 结合ReturnTest内容，对抛出异常情况的退出程序做进一步处理

        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
            mv.visitLdcInsn(className);
            mv.visitLdcInsn(name);
            mv.visitLdcInsn(desc);
            mv.visitMethodInsn(INVOKESTATIC, Actions.path,"methodEnd","(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",false);
        }
        mv.visitInsn(opcode);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        /* TODO: System.err.println("CALL" + name); */
        /*mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("CALL "+opcode+" " + owner +" "+name+" "+desc);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);*/

        /* do call */
        mv.visitMethodInsn(opcode, owner, name, desc, itf);

        /* TODO: System.err.println("RETURN" + name);  */
    }

    @Override
    public void visitEnd() {
        mv.visitEnd();
    }
}
