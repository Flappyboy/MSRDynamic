package com.nju.msr.core.model;

public class  ThreadInfo {

    //callInfoId
    String creatorCallInfoId;

    String creatorCallChainId;

    String starterId;

    public String getCreatorCallInfoId() {
        return creatorCallInfoId;
    }

    public void setCreatorCallInfoId(String creatorCallInfoId) {
        this.creatorCallInfoId = creatorCallInfoId;
    }

    public String getCreatorCallChainId() {
        return creatorCallChainId;
    }

    public void setCreatorCallChainId(String creatorCallChainId) {
        this.creatorCallChainId = creatorCallChainId;
    }

    public String getStarterId() {
        return starterId;
    }

    public void setStarterId(String starterId) {
        this.starterId = starterId;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "creatorCallInfoId='" + creatorCallInfoId + '\'' +
                ", creatorCallChainId='" + creatorCallChainId + '\'' +
                '}';
    }
}
