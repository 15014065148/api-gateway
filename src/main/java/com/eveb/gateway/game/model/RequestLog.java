package com.eveb.gateway.game.model;

import lombok.Data;

import java.util.Date;

@Data
public class RequestLog {

    private String ip;
    /**
     * 站点前缀
     */
    private String sitePrefix;
    private String platform;
    private String apiName;
    private String parameter;
    /**
     * 操作时间开始
     **/
    private Date startTime;
    /**
     * 操作时间结束
     **/
    private Date endTime;
    /**
     * 操作状态
     **/
    private Boolean status;
    private Long times;
    private String requestUrl;
    private String requestData;
    private String responseMsg;

    public RequestLog() {
    }
}
