package com.eveb.gateway.game.cq9.model;

import com.eveb.gateway.game.cq9.constants.Cq9Constants;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MbrTransferRecordModel extends RsMbrTransferRecordModel{

    private String processStatus=Cq9Constants.Status.SUCCESS;
}
