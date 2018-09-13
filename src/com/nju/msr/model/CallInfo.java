package com.nju.msr.model;

public class CallInfo {

    private Method caller;

    private Method callee;

    private Long startTime;

    private Long endTime;

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
