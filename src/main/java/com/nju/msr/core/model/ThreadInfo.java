package com.nju.msr.core.model;
/**
 * @Author: jiaqi li
 * @Date: 2018/9
 * @Version 1.0
 */

/**
 * 表示一个调用链所属线程的相关信息，主要是用于确定线程是谁创建，开始等
 */
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
