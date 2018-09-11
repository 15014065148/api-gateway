package com.eveb.gateway.game.pt2.model;

import lombok.Data;

public class Pt2ParameterModel {

    @Data
    public class UserPtNewDto {
        private String secretKey;
        private String username;
        private String password;
    }
}
