package com.test.nju.msr.test;

import org.objectweb.asm.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.*;

/**
 * @Author: jiaqi li
 * @Date: 2018/10/2 0002 18:23
 * @Version 1.0
 */
public class InstrumentExample implements Opcodes {

    /**
     * Our custom method modifier method visitor class. It delegate all calls to
     * the super class. Do our logic of adding try/catch block
     *
     */
    public static class ModifierMethodWriter extends MethodVisitor implements Opcodes {

        // methodName to make sure adding try catch block for the specific
        // method.
        private String methodName;

        // below label variables are for adding try/catch blocks in instrumented
        // code.
        private Label lTryBlockStart;
        private Label lTryBlockEnd;
        private Label lCatchBlockStart;
        private Label lCatchBlockEnd;

        /**
         * constructor for accepting methodVisitor object and methodName
         *
         * @param api: the ASM API version implemented by this visitor
         * @param mv: MethodVisitor obj
         * @param methodName : methodName to make sure adding try catch block for the specific method.
         */
        public ModifierMethodWriter(int api, MethodVisitor mv, String methodName) {
            super(api, mv);
            this.methodName = methodName;
        }

        // We want to add try/catch block for the entire code in the method
        // so adding the try/catch when the method is started visiting the code.
        @Override
        public void visitCode() {
            super.visitCode();

            // adding try/catch block only if the method is hello()
            if (methodName.equals("hello")) {
                lTryBlockStart = new Label();
                lTryBlockEnd = new Label();
                lCatchBlockStart = new Label();
                lCatchBlockEnd = new Label();

                // set up try-catch block for RuntimeException
                visitTryCatchBlock(lTryBlockStart, lTryBlockEnd,
                        lCatchBlockStart, "java/lang/Exception");

                // started the try block
                visitLabel(lTryBlockStart);
            }

        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {

            // closing the try block and opening the catch block if the method
            // is hello()
            if (methodName.equals("hello")) {
                // closing the try block
                visitLabel(lTryBlockEnd);

                // when here, no exception was thrown, so skip exception handler
                visitJumpInsn(GOTO, lCatchBlockEnd);

                // exception handler starts here, with RuntimeException stored
                // on stack
                visitLabel(lCatchBlockStart);

                // store the RuntimeException in local variable
                visitVarInsn(ASTORE, 2);

                // here we could for example do e.printStackTrace()
                visitVarInsn(ALOAD, 2); // load it
                visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception",
                        "printStackTrace", "()V", false);

                // exception handler ends here:
                visitLabel(lCatchBlockEnd);
            }

            super.visitMaxs(maxStack, maxLocals);
        }

    }

    /**
     * Our class modifier class visitor. It delegate all calls to the super
     * class Only makes sure that it returns our MethodVisitor for every method
     *
     */
    public static class ModifierClassWriter extends ClassVisitor {
        private int api;

        public ModifierClassWriter(int api, ClassWriter cv) {
            super(api, cv);
            this.api = api;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                                         String signature, String[] exceptions) {

            MethodVisitor mv = super.visitMethod(access, name, desc, signature,
                    exceptions);

            // Our custom MethodWriter
            ModifierMethodWriter mvw = new ModifierMethodWriter(api, mv, name);
            return mvw;
        }

    }

    public static void main(String[] args) throws IOException {

        DataOutputStream dout = null;
        try {
            // loading the class
            InputStream in = InstrumentExample.class
                    .getResourceAsStream("Example.class");
            ClassReader classReader = new ClassReader(in);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

            // Wrap the ClassWriter with our custom ClassVisitor
            ModifierClassWriter mcw = new ModifierClassWriter(ASM4, cw);
            ClassVisitor cv = new CheckClassAdapter(mcw);

            classReader.accept(cv, 0);

            byte[] byteArray = cw.toByteArray();
            dout = new DataOutputStream(new FileOutputStream(new File("Example.class")));
            dout.write(byteArray);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (dout != null)
                dout.close();
        }

    }
}
