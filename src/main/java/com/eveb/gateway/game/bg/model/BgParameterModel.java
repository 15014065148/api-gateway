package com.eveb.gateway.game.bg.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BgParameterModel {
    //公共
    private String url;
    private String id;
    private String jsonrpc;
    private String random;
    private String sn;
    private String loginId;
    private String method;//每个接口都不一样

    //有的有，有的没有
    private String sign;
    private String digest;
    private String locale;
    private Integer isMobileUrl;
    private Integer isHttpsUrl;
    private String amount;
    private Long bizId;
    private Date startTime;
    private Date endTime;
    private Integer pageIndex;
    private Integer pageSize;
    private String nickname;
    private String agentLoginId;
    private String gameType;

}
