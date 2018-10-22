package com.nju.msr.persistence.Sqlite;

import com.nju.msr.core.model.call.CallChain;
import com.nju.msr.core.model.call.CallInfo;
import com.nju.msr.core.model.method.Method;
import com.nju.msr.core.model.method.MethodRelation;
import com.nju.msr.core.persistence.Service;
import com.nju.msr.core.persistence.ServiceManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class SqliteService implements Service {

    static {
        ServiceManager.registerService(new SqliteService());
    }

    public SqliteService() {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened sqlite database successfully");
    }

    @Override
    public void MethodCreated(Method method) {

    }

    @Override
    public void MethodRelationCreated(MethodRelation methodRelation) {

    }

    @Override
    public void MethodRelationCount(MethodRelation methodRelation, int count) {

    }

    @Override
    public void MethodRelations(Map<String, MethodRelation> methodRelationMap) {

    }

    @Override
    public void CallChainCreated(CallChain callChain) {

    }

    @Override
    public void CallCreated(CallInfo callInfo) {

    }

    @Override
    public void CallChainFinish(CallChain callChain) {

    }
}
