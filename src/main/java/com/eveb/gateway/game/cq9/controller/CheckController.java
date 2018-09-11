package com.eveb.gateway.game.cq9.controller;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.game.cq9.service.Cq9Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

@RestController
@RequestMapping("/")
public class CheckController {

    @Autowired
    private Cq9Service cq9Service;

    @RequestMapping(value = {"/transaction/record/*"}, produces="application/json")
    public Object getTransferRecord(HttpServletRequest request) {
        return cq9Service.getTransferRecord(request);
    }

    @GetMapping(value = {"/transaction/balance/*"}, produces="application/json")
    public String getBalance(HttpServletRequest request) {
        return JSON.toJSONString(cq9Service.getBalance(request));
    }

    @GetMapping(value = {"/player/check/*"}, produces="application/json")
    public String getCheckPlayer(HttpServletRequest request) {
        return JSON.toJSONString(cq9Service.checkPlayer(request));
    }
}
