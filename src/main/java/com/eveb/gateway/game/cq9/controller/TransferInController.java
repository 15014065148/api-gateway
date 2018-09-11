package com.eveb.gateway.game.cq9.controller;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.game.cq9.service.Cq9Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/")
public class TransferInController {

    @Autowired
    private Cq9Service cq9Service;

    @RequestMapping(value = {"/transaction/game/rollin","/transaction/game/credit","/transaction/game/payoff"}, produces="application/json")
    public Object transferIn(HttpServletRequest request) {
        return cq9Service.transferIn(request);
    }

    @RequestMapping(value = {"/transaction/game/endround"}, produces="application/json")
    public Object endRound(HttpServletRequest request) {
        return cq9Service.endRound(request);
    }

    @RequestMapping(value = {"/transaction/game/refund"}, produces="application/json")
    public Object refund(HttpServletRequest request) {
        return cq9Service.refund(request);
    }
}
