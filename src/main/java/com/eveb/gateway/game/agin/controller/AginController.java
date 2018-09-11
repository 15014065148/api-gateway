package com.eveb.gateway.game.agin.controller;

import com.eveb.gateway.game.agin.processor.AginProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class AginController {

    @Autowired
    private AginProcessor aginProcessor;

    @RequestMapping(value={"/doBusiness.do","/forwardGame.do"})
    public String request(HttpServletRequest request, HttpServletResponse response) {
        return aginProcessor.processor(request);
    }

}
