package com.eveb.gateway.game.cq9.controller;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.game.cq9.service.Cq9Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class TokenController {

    @Autowired
    private Cq9Service cq9Service;


    @RequestMapping(value = {"/transaction/check/:token"})
    public String getToken(HttpServletRequest request) {
        return JSON.toJSONString(cq9Service.generateToken());
    }
}
