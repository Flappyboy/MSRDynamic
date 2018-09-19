package com.test.nju.msr.test;

import com.test.nju.msr.test.model.TestA;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class MainTest {
    static class MyClassLoader extends ClassLoader {
        private String classPath;

        public MyClassLoader(String classPath) {
            this.classPath = classPath;
        }

        private byte[] loadByte(String name) throws Exception {
            name = name.replaceAll("\\.", "/");
            FileInputStream fis = new FileInputStream(classPath + "/" + name
                    + ".class");
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            return data;

        }

        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] data = loadByte(name);
                return defineClass(name, data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassNotFoundException();
            }
        }

    };
    public static void main(String[] args) {
        /*TestA testA = new TestA();
        testA.func1();
        testA.func2();*/
        System.out.println(System.getProperty("sun.boot.class.path"));
    }
}
