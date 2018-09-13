package com.nju.msr.model;

import java.util.ArrayList;
import java.util.List;

public class CallInfo {

    private Method caller;

    private Method callee;

    private Long startTime;

    private Long endTime;

    private List<CallInfo> childCallList = new ArrayList<>();

    private CallInfo parentCall;

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

    public boolean isFinished(){
        if (endTime!=null)
            return true;
        return false;
    }

    public CallInfo(Method caller, Method callee) {
        this.caller = caller;
        this.callee = callee;
    }

    public CallInfo(Method caller, Method callee, long startTime) {
        this.caller = caller;
        this.callee = callee;
        this.startTime = startTime;
    }

    public CallInfo(Method caller, Method callee, long startTime, long endTime) {
        this.caller = caller;
        this.callee = callee;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
