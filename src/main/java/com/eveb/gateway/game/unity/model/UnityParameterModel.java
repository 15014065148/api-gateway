package com.eveb.gateway.game.unity.model;

import com.eveb.gateway.game.model.TGmApi;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/9 10:39
 **/
@Data
public class UnityParameterModel {
    @NotNull
    private String siteCode;
    private String userName;
    private TGmApi tGmApi;
}
