package com.nju.msr.core.model;

import com.nju.msr.core.persistence.ServiceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CallInfo implements Serializable {
    private MethodRelationManagement methodRelationManagement = MethodRelationManagement.getInstance();

    //用于在CallChain中的唯一标识
    private String callInfoid;

    private CallChain callChain;

    //在这个类中更关注的应该是其中的callee
    private MethodRelation methodRelation;

    private Long startTime;

    /**
     * 由于无法可靠地保证获得方法结束信息，所以目前这个状态变量是不靠谱，
     */
    private Long endTime;

    private List<CallInfo> childCallList = new ArrayList<>();

    private transient CallInfo parentCall;

    public CallInfo(Method caller, Method callee, String callInfoid, CallChain callChain) {
        this.methodRelation = methodRelationManagement.callMethodRelation(caller,callee);
        this.startTime = System.currentTimeMillis();

        this.callInfoid = callInfoid;
        this.callChain = callChain;

        ServiceManager.CallCreated(this);
    }

    public void addChildCall(CallInfo callInfo){
        if (callInfo!=null) {
            childCallList.add(callInfo);
            callInfo.parentCall = this;
        }
    }

    public CallInfo getParentCall() {
        return parentCall;
    }

    public int getChildCallListSize(){
        return childCallList.size();
    }

    public CallInfo getChildCall(int index) {
        return childCallList.get(index);
    }

    public boolean hasChildCall(){
        if (childCallList.size()==0)
            return false;
        return true;
    }

    /**
     * 由于无法可靠地保证获得方法结束信息，所以endTime是不靠谱，所以这方法是不靠谱的
     */
    public boolean isFinished(){
        if (endTime!=null)
            return true;
        return false;
    }

    public long getTime(){
        if(startTime==null||endTime==null){
            return -1;
        }else {
            return endTime-startTime;
        }
    }

    public Method getCaller() {
        return methodRelation.getCaller();
    }

    public Method getCallee() {
        return methodRelation.getCallee();
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getCallInfoid() {
        return callInfoid;
    }

    public CallChain getCallChain() {
        return callChain;
    }

    @Override
    public String toString() {
        return "CallInfo{" +
                "callInfoId"+ getCallInfoid() +
                "caller=" + getCallInfoid() +
                "caller=" + getCaller() +
                ", callee=" + getCallee() +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", childSize=" + childCallList.size() +
                '}';
    }
}
