package com.eveb.gateway.game.n2.controller;

import com.eveb.gateway.game.n2.service.N2ServiceImpl;
import com.eveb.gateway.utils.RequestParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/9/3 11:01
 **/
@RestController
@RequestMapping("/n2/callback/")
public class N2Controller {

    @Autowired
    private N2ServiceImpl n2Service;

    @RequestMapping("login")
    public Object playGame(HttpServletRequest request) {
        return n2Service.checklogin(RequestParameter.reader(request));
    }
}
