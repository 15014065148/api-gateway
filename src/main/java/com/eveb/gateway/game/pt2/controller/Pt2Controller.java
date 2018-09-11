package com.eveb.gateway.game.pt2.controller;

import com.eveb.gateway.game.pt2.processor.Pt2Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/v1")
public class Pt2Controller {

    @Autowired
    private Pt2Processor pt2Processor;

    @RequestMapping(value={"/**"})
    public String request(HttpServletRequest request, HttpServletResponse response) {
        return pt2Processor.processor(request);
    }
}
