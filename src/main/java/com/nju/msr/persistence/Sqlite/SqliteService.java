package com.nju.msr.persistence.Sqlite;

import com.nju.msr.core.model.CallChain;
import com.nju.msr.core.model.CallInfo;
import com.nju.msr.core.model.Method;
import com.nju.msr.core.model.MethodRelation;
import com.nju.msr.core.persistence.Service;
import com.nju.msr.core.persistence.ServiceManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

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
        System.out.println("Opened database successfully");
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
