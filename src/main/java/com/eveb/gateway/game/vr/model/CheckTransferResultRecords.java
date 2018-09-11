package com.eveb.gateway.game.vr.model;

import lombok.Data;

@Data
public class CheckTransferResultRecords {
    private String serialNumber;
    private String playerName;
    private Integer type;
    private Integer amount;
    private String createTime;
    private Integer state;
    private Integer balance;
}
