package com.eveb.gateway.game.avia.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.game.avia.model.AviaConstants;
import com.eveb.gateway.game.avia.model.AviaParameterModel;
import com.eveb.gateway.game.avia.model.AviaResultModel;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/17 9:58
 **/
@Slf4j
@Service
public class AviaServiceImpl implements DepotService {

    @Autowired
    public OkHttpService okHttpService;

    @Override
    public Object tryPlayGame(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object playGame(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object openHall(PlayGameModel playGame) {
        AviaParameterModel.LoginModel lg=new AviaParameterModel.LoginModel();
        lg.setUsername(playGame.getTGmApi().getPrefix()+playGame.getUserName());
        return AviaResultModel.loginUnityResult(request(playGame.getTGmApi(),AviaConstants.MethodConstatns.METHOD_LOGIN,lg));
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        AviaParameterModel.RegisterModel rg=new AviaParameterModel.RegisterModel();
        rg.setUserName(register.getTGmApi().getPrefix()+register.getUserName());
        rg.setPassword(register.getPassword());
        return AviaResultModel.registerUnityResult(request(register.getTGmApi(),AviaConstants.MethodConstatns.METHOD_REGISTER,rg));
    }

    @Override
    public Object login(LoginModel login) {
        AviaParameterModel.LoginModel lg=new AviaParameterModel.LoginModel();
        lg.setUsername(login.getTGmApi().getPrefix()+login.getUserName());
        return AviaResultModel.loginUnityResult(request(login.getTGmApi(),AviaConstants.MethodConstatns.METHOD_LOGIN,lg));
    }

    @Override
    public Object logout(LoginModel login) {
        return null;
    }

    @Override
    public Object deposit(TransferModel transfer) {
        return transfer(transfer,AviaConstants.TransferType.IN);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        return transfer(transfer,AviaConstants.TransferType.OUT);
    }

    @Override
    public Object queryBalance(LoginModel login) {
        AviaParameterModel.LoginModel lg=new AviaParameterModel.LoginModel();
        lg.setUsername(login.getTGmApi().getPrefix()+login.getUserName());
        return AviaResultModel.balanceUnityResult(request(login.getTGmApi(),AviaConstants.MethodConstatns.METHOD_BALANCE,lg));
    }

    @Override
    public Object checkTransfer(TransferModel transfer) {
        AviaParameterModel.TransferModel tf=new AviaParameterModel.TransferModel();
        tf.setID(transfer.getOrderNo());
        return AviaResultModel.transferStatusUnityResult(request(transfer.getTGmApi(),AviaConstants.MethodConstatns.METHOD_TRANSFER_STATUS,tf));
    }

    @Override
    public Object getToken() {
        return null;
    }

    private UnityResultModel transfer(TransferModel transfer,String tfType){
        AviaParameterModel.TransferModel tf=new AviaParameterModel.TransferModel();
        tf.setUserName(transfer.getTGmApi().getPrefix()+transfer.getUserName());
        tf.setMoney(transfer.getAmount().intValue());
        tf.setID(transfer.getOrderNo());
        tf.setType(tfType);
        try {
           return AviaResultModel.transferUnityResult(okHttpService.postForm(okHttpService.initHeadClient(initHead(transfer.getTGmApi())),
                   transfer.getTGmApi().getApiUrl()+AviaConstants.MethodConstatns.METHOD_TRANSFER, (Map<String, String>) JSON.toJSON(tf)));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private String request(TGmApi api, String method, Object map) {
        try {
            return okHttpService.postForm(okHttpService.initHeadClient(initHead(api)), api.getApiUrl()+method, (Map<String, String>) JSON.toJSON(map));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private Map initHead(TGmApi api) {
        Map head = new HashMap(1);
        head.put(AviaConstants.MAP_ACCEPT, api.getMd5Key());
        return head;
    }
}
