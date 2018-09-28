package com.nju.msr.persistence.neo4j.remote;

import com.nju.msr.core.Param;
import com.nju.msr.core.model.CallChain;
import com.nju.msr.core.model.CallInfo;
import com.nju.msr.core.model.Method;
import com.nju.msr.core.model.MethodRelation;
import com.nju.msr.core.persistence.Service;
import com.nju.msr.core.persistence.ServiceManager;

import com.nju.msr.utils.StringUtil;
import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

import java.util.Map;

public class Neo4jService implements Service {

    public static final String neo4j="neo4j.remote";
    public static final String uriName = "neo4j.remote.uri";
    public static final String neo4jUsername = "neo4j.remote.username";
    public static final String neo4jPassword = "neo4j.remote.password";

    static {
        String open = Param.PROPERTIES.getProperty(neo4j);
        String uri = Param.PROPERTIES.getProperty(uriName);
        String username = Param.PROPERTIES.getProperty(neo4jUsername);
        String password = Param.PROPERTIES.getProperty(neo4jPassword);
        if (open!=null&&"true".equals(open.toLowerCase())&&!StringUtil.isAnyBlank(uri,username,password)) {
            try {
                driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
                ServiceManager.registerService(new Neo4jService());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static Driver driver;

    public Neo4jService() {
        Session session = driver.session();
        //session.run("MATCH (n) DETACH DELETE n");
        session.close();
    }

    @Override
    public void MethodCreated(Method method) {
        BasicOperation.createMethod(method);
    }

    @Override
    public void MethodRelationCreated(MethodRelation methodRelation) {
        BasicOperation.createMethodRelation(methodRelation);
    }

    @Override
    public void MethodRelationCount(MethodRelation methodRelation, int count) {

        if (!Param.isUnderPackage(methodRelation.getCaller().getOwner())&& !Param.isUnderPackage(methodRelation.getCallee().getOwner()))
            return;

        Session session = driver.session();
        session.runAsync( " match (a)-[call:CALL]-(b) where id(call)={relationId} set call.count="+count,
                parameters( "relationId",NodeIdManagement.getNodeId(methodRelation)));
        session.closeAsync();
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
