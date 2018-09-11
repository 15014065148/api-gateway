package com.eveb.gateway.game.bbin.controller;

import com.eveb.gateway.game.bbin.processor.BbinProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/app/WebService/")
public class BbinController {

    @Autowired
    private BbinProcessor bbinProcessor;

    @RequestMapping("JSON/display.php/**")
    public String jsonRequest(HttpServletRequest request, HttpServletResponse response) {
        return bbinProcessor.jsonProcessor(request);
    }

    @RequestMapping("XML/display.php/**")
    public String xmlRequest(HttpServletRequest request, HttpServletResponse response)throws Exception {
        return bbinProcessor.xmlProcessor(request);
    }

}
