package com.test.nju.msr.test;

import com.nju.msr.utils.FileUtil;
import com.test.nju.msr.test.model.TestA;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

import java.io.File;
import java.io.IOException;

public class MainTest {
    public static void main(String[] args) {
        TestA testA = new TestA();
        testA.func1();
        testA.func2();
        testA.func3();
        testA.func4();
        testA.func5();
        testA.func6();
    }
}
