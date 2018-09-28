package com.nju.msr.persistence.neo4j.remote;

import com.nju.msr.core.model.Method;
import com.nju.msr.core.model.MethodRelation;
import com.nju.msr.utils.StringUtil;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import static org.neo4j.driver.v1.Values.parameters;

public class BasicOperation {

    public static Session getSession(){
        return Neo4jService.driver.session();
    }

    public static void createMethod(Method method){
        Session session = getSession();

        Long id = queryMethod(method);
        if (id!=null){
            NodeIdManagement.addMethodNode(method, id);
            return;
        }

        StatementResult result = session.run( "CREATE (m:Method:"+method.getType()+" {owner: {owner}, name: {name}, descriptor: {descriptor}}) return m",
                parameters( "owner", method.getOwner(), "name", method.getName(), "descriptor", method.getDescriptor() ) );
        session.closeAsync();
        while ( result.hasNext() )
        {
            Record record = result.next();
            NodeIdManagement.addMethodNode(method, record.get( "m" ).asNode().id());
        }
    }
    public static Long queryMethod(Method method){
        Session session = getSession();
        StatementResult result = session.run("match (m:Method) where m.owner={owner} and m.name={name}"+
                        (StringUtil.isBlank(method.getDescriptor()) ? "" : "and m.descriptor={descriptor}") +"  return m",
                parameters( "owner", method.getOwner(), "name", method.getName(), "descriptor", method.getDescriptor() ) );
        session.closeAsync();
        while ( result.hasNext() )
        {
            Record record = result.next();
            return record.get( "m" ).asNode().id();
        }
        return null;
    }

    public static void createMethodRelation(MethodRelation methodRelation){
        Session session = getSession();

        Long id = queryMethodRelation(methodRelation);
        if (id!=null){
            NodeIdManagement.addMethodRelationNode(methodRelation, id);
            return;
        }

        StatementResult result = session.run( " match (caller:Method),(callee:Method) where id(caller)={callerId} and id(callee)={calleeId} CREATE (caller)-[call:CALL {count:0}]->(callee) return call",
                parameters( "callerId",NodeIdManagement.getNodeId(methodRelation.getCaller()),
                        "calleeId",NodeIdManagement.getNodeId(methodRelation.getCallee())));
        session.closeAsync();
        while ( result.hasNext() )
        {
            Record record = result.next();
            NodeIdManagement.addMethodRelationNode(methodRelation, record.get( "call" ).asRelationship().id());

        }
    }
    public static Long queryMethodRelation(MethodRelation methodRelation){
        Session session = getSession();
        StatementResult result = session.run("match (caller:Method)-[call:CALL]->(callee:Method) where id(caller)={callerId} and id(callee)={calleeId} return call",
                parameters( "callerId",NodeIdManagement.getNodeId(methodRelation.getCaller()),
                        "calleeId",NodeIdManagement.getNodeId(methodRelation.getCallee())));
        session.closeAsync();
        while ( result.hasNext() )
        {
            Record record = result.next();
            if (NodeIdManagement.getNodeId(methodRelation)==null){
                methodRelation.setCount(record.get( "call" ).asRelationship().get("count").asInt());
            }
            return record.get( "call" ).asRelationship().id();
        }
        return null;
    }

}
