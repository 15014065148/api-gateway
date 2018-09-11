package com.eveb.gateway.game.cq9.controller;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.game.cq9.service.Cq9Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transaction/game/")
public class TransferOutController {

    @Autowired
    private Cq9Service cq9Service;

    @RequestMapping(value = {"/bet", "/rollout", "/debit"}, produces="application/json")
    public Object request(HttpServletRequest request) {
        return cq9Service.transferOut(request);
    }

    @RequestMapping(value = {"/takeall"}, produces="application/json")
    public Object takeAll(HttpServletRequest request) {
        return cq9Service.takeAll(request);
    }
}
