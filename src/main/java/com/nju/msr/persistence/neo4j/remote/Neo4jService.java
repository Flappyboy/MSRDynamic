package com.nju.msr.persistence.neo4j.remote;

import com.nju.msr.core.Param;
import com.nju.msr.core.model.call.CallChain;
import com.nju.msr.core.model.call.CallInfo;
import com.nju.msr.core.model.method.Method;
import com.nju.msr.core.model.method.MethodRelation;
import com.nju.msr.core.persistence.Service;
import com.nju.msr.core.persistence.ServiceManager;

import com.nju.msr.utils.StringUtil;
import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

import java.util.Map;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */
public class Neo4jService implements Service {

    public static final String neo4j="neo4j.remote";
    public static final String uriName = "neo4j.remote.uri";
    public static final String neo4jUsername = "neo4j.remote.username";
    public static final String neo4jPassword = "neo4j.remote.password";

    static {
        boolean open = Boolean.parseBoolean(Param.PROPERTIES.getProperty(neo4j,"false"));
        String uri = Param.PROPERTIES.getProperty(uriName);
        String username = Param.PROPERTIES.getProperty(neo4jUsername);
        String password = Param.PROPERTIES.getProperty(neo4jPassword);
        if (open&&!StringUtil.isAnyBlank(uri,username,password)) {
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

        /*Session session = driver.session();
        session.runAsync( " match (a)-[call:CALL]->(b) where id(call)={relationId} set call.count="+count,
                parameters( "relationId",NodeIdManagement.getNodeId(methodRelation)));
        session.closeAsync();*/
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
        new Thread(new CallChainRunnable(callChain)).start();

    }
    class CallChainRunnable implements Runnable{

        CallChain callChain;

        public CallChainRunnable(CallChain callChain) {
            this.callChain = callChain;
        }

        @Override
        public void run() {
            System.out.println();
            System.out.println(callChain.getCallChainId()+"   START ---------------------------------");
            processCallInfo(callChain.getRootCall(),0);
            System.out.println();
            System.out.println(callChain.getCallChainId()+"   END ---------------------------------");
        }
    }

    private void processCallInfo(CallInfo callInfo, int level){
        setValue(callInfo);
        for (int i=0; i<callInfo.getChildCallListSize(); i++) {
            CallInfo tmp = callInfo.getChildCall(i);
            processCallInfo(tmp,level+1);
        }
    }

    private void setValue(CallInfo callInfo){
        Session session = driver.session();
        session.runAsync( " match (a)-[call:CALL]->(b) where id(call)={relationId} set call.callInfo=call.callInfo+[{callChainId}]",
                parameters( "relationId",NodeIdManagement.getNodeId(callInfo.getMethodRelation()),
                        "callChainId",callInfo.getCallChain().getCallChainId()));
        session.closeAsync();
        System.out.print("*");
    }
}
