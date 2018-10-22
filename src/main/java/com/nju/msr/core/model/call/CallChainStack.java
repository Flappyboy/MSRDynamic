package com.nju.msr.core.model.call;

import com.nju.msr.core.Param;
import com.nju.msr.core.model.method.Method;
import com.nju.msr.core.model.method.MethodFactory;
import com.nju.msr.utils.StackUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */

/**
 * 表示一次调用链的完整过程
 * 将{@link CallInfo}作为节点，构成一棵树
 */
public class CallChainStack extends CallChain{

    protected void methodStart(Method method){

        CallChainManagement.strategy.correctCallChain(method, Strategy.ActionType.START, this);
        CallChainManagement.strategy.addCallInfoToCallChain(method, this);
        Stack stack = copyStack(threadCallChain.get());
        stack.push(this.currentCall);
        threadCallChain.set(stack);
    }

    protected void methodEnd(Method method){
        CallChainManagement.strategy.correctCallChain(method, Strategy.ActionType.END, this);
        Stack<CallInfo> stack = copyStack(threadCallChain.get());
        if (!stack.empty()){
            CallInfo callInfo = stack.pop();
            callInfo.setEndTime(System.currentTimeMillis());
            try {
                currentCall = stack.peek();
            }catch (Exception e){
                currentCall = null;
            }
            if (callInfo.getCallee()!=method){
                System.err.println("methodEnd 结束方法不一致 endMethod"+ method.toString() + "   stackMethod:"+callInfo.getCallee());
            }
        }else{
            System.err.println("mehtodEnd stack不应为空");
        }
        threadCallChain.set(stack);
    }

    private Stack copyStack(Stack stack){
        Stack s = new Stack();
        for (Object o: stack){
            s.push(o);
        }
        return s;
    }
}
