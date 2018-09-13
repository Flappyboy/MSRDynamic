package com.nju.msr.model;

import java.util.ArrayList;
import java.util.List;

public class CallChainInfo {

    private List<CallInfo> callChain = new ArrayList<>();

    private Thread currentThread;
}
