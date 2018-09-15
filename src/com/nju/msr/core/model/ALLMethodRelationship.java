package com.nju.msr.core.model;

import java.util.HashSet;
import java.util.Set;

public class ALLMethodRelationship {

    private static final ALLMethodRelationship INSTANCE = new ALLMethodRelationship();

    private ALLMethodRelationship() {}

    public static ALLMethodRelationship getInstance() {
        return INSTANCE;
    }

    public Set<Method> rootMethodSet = new HashSet<>();



}
