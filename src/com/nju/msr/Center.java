package com.nju.msr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Center {

    Map<Thread, Data> threadListMap = new HashMap<Thread, Data>();

    void addNewThread(Thread thread){
        threadListMap.put(thread, new Data());
    }

    void addData(){

    }

}
