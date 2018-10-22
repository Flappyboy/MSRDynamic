package com.nju.msr.core.model.call;

import com.nju.msr.core.model.method.Method;

/**
 * @Author: jiaqi li
 * @Date: 2018/10/16 0016 15:06
 * @Version 1.0
 */
public interface Strategy {

    enum ActionType {START, END}

    void addCallInfoToCallChain(Method method, CallChain callChain);

    void correctCallChain(Method method, ActionType actionType, CallChain callChain);
}
