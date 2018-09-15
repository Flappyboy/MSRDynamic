package com.nju.msr.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CallInfo implements Serializable {

    private Method caller;

    private Method callee;

    private Long startTime;

    /**
     * 由于无法可靠地保证获得方法结束信息，所以目前这个状态变量是不靠谱，
     */
    private Long endTime;

    private List<CallInfo> childCallList = new ArrayList<>();

    private transient CallInfo parentCall;

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

    public CallInfo(Method caller, Method callee) {
        this.caller = caller;
        this.callee = callee;
        this.startTime = System.currentTimeMillis();
    }

    public long getTime(){
        if(startTime==null||endTime==null){
            return -1;
        }else {
            return endTime-startTime;
        }
    }

    public Method getCaller() {
        return caller;
    }

    public Method getCallee() {
        return callee;
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

    @Override
    public String toString() {
        return "CallInfo{" +
                "caller=" + caller +
                ", callee=" + callee +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", childSize=" + childCallList.size() +
                '}';
    }
}
