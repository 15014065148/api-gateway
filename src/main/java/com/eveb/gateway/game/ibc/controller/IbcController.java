package com.eveb.gateway.game.ibc.controller;

import com.eveb.gateway.game.ibc.processor.IbcProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class IbcController {

    @Autowired
    private IbcProcessor ibcProcessor;

    @RequestMapping(value={"/**"})
    public String request(HttpServletRequest request) {
        return ibcProcessor.processor(request);
    }
}
