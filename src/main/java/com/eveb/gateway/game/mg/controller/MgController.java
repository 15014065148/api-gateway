package com.eveb.gateway.game.mg.controller;

import com.eveb.gateway.game.mg.processor.MgProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class MgController {

    @Autowired
    private MgProcessor mgProcessor;

    @RequestMapping(value={"/member-api-web/**"})
    public String request(HttpServletRequest request) {
        return mgProcessor.processor(request);
    }

    @RequestMapping(value={"/lps/j_spring_security_check"})
    public String requestToken(HttpServletRequest request) {
        return mgProcessor.processorToken(request);
    }

    @RequestMapping(value={"/lps/secure/**"})
    public String requestSecure(HttpServletRequest request) {
        return mgProcessor.processorSecure(request);
    }

}
