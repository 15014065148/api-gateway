package com.eveb.gateway.game.unity.model;

import lombok.Data;

@Data
public class PlayGameModel extends UnityParameterModel{
    private String gameType;
    private String gameId;
    /**设备类型：PC、H5、APP**/
    private String origin;
}
