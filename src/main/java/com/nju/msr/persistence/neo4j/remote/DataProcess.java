package com.nju.msr.persistence.neo4j.remote;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class DataProcess {

    public void deleteOther(){
        String uri = "bolt://localhost:7687";
        String username = "neo4j";//"neo4j"
        String password = "jpetstore";//"test"
        Session session = null;
        try {
            Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
            session = driver.session();
        }catch (Exception e){
            e.printStackTrace();
        }
        String base = "->(:OTHER)-[]";
        String base2 = base;
        for (int i=0; i<20; i++) {
            String str = "match (:FOCUS)-[]->(s:OTHER)-[c:CALL]"+base2+"->(:OTHER)-[:CALL]->(e:OTHER)-[]->(:FOCUS) create (s)-[:OTHER]->(e) delete c;";
//            FileUtil.appendContentToFile("file.txt", str);
            session.runAsync(str);
            base2 += base;
        }
        session.closeAsync();
    }
}
