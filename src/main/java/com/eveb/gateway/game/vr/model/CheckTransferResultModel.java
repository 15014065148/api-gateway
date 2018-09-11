package com.eveb.gateway.game.vr.model;

import lombok.Data;

import java.util.List;

@Data
public class CheckTransferResultModel {
    private Integer totalRecords;
    private Integer recordPage;
    private Integer recordCountPerPage;
    private List<CheckTransferResultRecords> records;
}
