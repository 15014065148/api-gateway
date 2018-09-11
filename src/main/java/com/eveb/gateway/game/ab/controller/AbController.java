package com.eveb.gateway.game.ab.controller;

import com.eveb.gateway.utils.RequestParameter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/")
public class AbController {

    @RequestMapping(value={"/get_balance"})
    public String request(HttpServletRequest request, HttpServletResponse response)throws Exception {
        Map map=RequestParameter.getPara(request);
        String str=RequestParameter.reader(request);
        return "";
    }
}
