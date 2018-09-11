package com.eveb.gateway.game.unity.model;

import com.eveb.gateway.constants.ApplicationConstants;
import lombok.Data;

@Data
public class LoginModel extends UnityParameterModel {

    private String password;


    /***使用默认密码***/
    public String getPassword() {
        if (password != null && !password.isEmpty()) {
            return password;
        }
        return ApplicationConstants.DEFAULT_USER_PASSWORD;
    }
}
