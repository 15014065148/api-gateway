package com.eveb.gateway.game.png.controller;

import com.eveb.gateway.game.png.processor.PngProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class PngController {

    @Autowired
    private PngProcessor pngProcessor;

    @RequestMapping(value={"/CasinoGameService/**"})
    public String request(HttpServletRequest request) {
        return pngProcessor.processor(request);
    }
}
