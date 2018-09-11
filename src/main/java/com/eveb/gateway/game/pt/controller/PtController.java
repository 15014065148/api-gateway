package com.eveb.gateway.game.pt.controller;

import com.eveb.gateway.game.pt.processor.PtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class PtController {

    @Autowired
    private PtProcessor ptProcessor;

    @RequestMapping(value={"/player/**","/customreport/**"})
    public String request(HttpServletRequest request, HttpServletResponse response) {
        return ptProcessor.processor(request);
    }
}
