package com.eveb.gateway.game.unity.model;

import lombok.Data;
@Data
public class UnityResultModel {

    private Boolean code;
    private Object message;

    public UnityResultModel() {
    }

    public UnityResultModel(Boolean code, Object message) {
        this.code = code;
        this.message = message;
    }

    public interface ErrMsg{
        String ERROR_API_NULL="线路不存在";
        String ERROR_PLATFORM_NULL="平台不存在";
    }
}
