package com.eveb.gateway.game.pg.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.game.pg.model.PgConstants;
import com.eveb.gateway.game.pg.model.PgParameterModel;
import com.eveb.gateway.game.pg.model.PgResultModel;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.unity.constants.UnityConstants;
import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: Miracle
 * @Description: 盘古彩播业务
 * @Date: 16:16 2018/8/1
 **/
@Slf4j
@Service
public class PgServiceImpl implements DepotService {

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
        LoginModel login = new LoginModel();
        login.setSiteCode(playGame.getSiteCode());
        login.setUserName(playGame.getTGmApi().getPrefix()+playGame.getUserName());
        login.setTGmApi(playGame.getTGmApi());
        UnityResultModel ur = login(login, playGame.getOrigin());
        if (ur.getCode()) {
            ur.setMessage(playGame.getTGmApi().getPcUrl()+ PgConstants.MethodConstatns.METHOD_PLAYGAME + ur.getMessage());
        }
        return ur;
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        PgParameterModel.RegisterModel reg = new PgParameterModel.RegisterModel();
        reg.setUsername(register.getTGmApi().getPrefix() + register.getUserName());
        reg.setPassword(register.getPassword());
        reg.setOrderNumber(ApplicationConstants.DEFAULT_USER_PASSWORD);
        reg.setPartner(register.getTGmApi().getAgyAcc());
        reg.setSign(register.getTGmApi().getMd5Key());
        return request(register.getTGmApi().getApiUrl() + PgConstants.MethodConstatns.METHOD_REGISTER, (Map<String, String>) JSON.toJSON(reg));
    }

    @Override
    public Object login(LoginModel login) {
        return login(login, null);
    }

    @Override
    public Object logout(LoginModel login) {
        return null;
    }

    private UnityResultModel login(LoginModel logins, String origin) {
        PgParameterModel.LoginModel login = new PgParameterModel.LoginModel();
        login.setUsername(logins.getUserName());
        login.setPassword(logins.getPassword());
        login.setOrderNumber(ApplicationConstants.DEFAULT_USER_PASSWORD);
        login.setPartner(logins.getTGmApi().getAgyAcc());
        login.setSign(logins.getTGmApi().getMd5Key());
        if (origin != null && origin.isEmpty()) {
            switch (origin) {
                case UnityConstants.OriginConstants.H5: {
                    login.setLogin_side(PgConstants.OriginConstants.H5);
                    break;
                }
                case UnityConstants.OriginConstants.APP: {
                    login.setLogin_side(PgConstants.OriginConstants.APP);
                    break;
                }
                default: {
                    login.setLogin_side(PgConstants.OriginConstants.PC);
                    break;
                }
            }
        } else {
            login.setLogin_side(PgConstants.OriginConstants.PC);
        }
        login.setIp(ApplicationConstants.DEFAULT_IP);
        return request(logins.getTGmApi().getApiUrl() + PgConstants.MethodConstatns.METHOD_LOGIN, (Map<String, String>) JSON.toJSON(login));
    }

    @Override
    public Object deposit(TransferModel transfer) {
        return transfer(transfer, PgConstants.TransferConstants.IN);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        return transfer(transfer, PgConstants.TransferConstants.OUT);
    }

    private Object transfer(TransferModel transfer, String type) {
        PgParameterModel.TransferModel pgTransfer = new PgParameterModel.TransferModel();
        pgTransfer.setUsername(transfer.getTGmApi().getPrefix() + transfer.getUserName());
        pgTransfer.setType(type);
        pgTransfer.setOrderNumber(transfer.getOrderNo());
        pgTransfer.setPartner(transfer.getTGmApi().getAgyAcc());
        pgTransfer.setAmount(String.valueOf(transfer.getAmount().intValue()));
        pgTransfer.setSign(transfer.getTGmApi().getMd5Key());
        pgTransfer.setTransationNo(transfer.getOrderNo());
        return request(transfer.getTGmApi().getApiUrl() + PgConstants.MethodConstatns.METHOD_TRANSFER, (Map<String, String>) JSON.toJSON(pgTransfer));
    }

    @Override
    public UnityResultModel queryBalance(LoginModel login) {
        PgParameterModel.BalanceModel bal = new PgParameterModel.BalanceModel();
        bal.setUsername(login.getTGmApi().getPrefix() + login.getUserName());
        bal.setOrderNumber(ApplicationConstants.DEFAULT_USER_PASSWORD);
        bal.setPartner(login.getTGmApi().getAgyAcc());
        bal.setSign(login.getTGmApi().getMd5Key());
        return request(login.getTGmApi().getApiUrl() + PgConstants.MethodConstatns.METHOD_BALANCE, (Map<String, String>) JSON.toJSON(bal));
    }

    private UnityResultModel request(String url, Map<String, String> map) {
        try {
            String result = okHttpService.get(okHttpService.proxyClient, url, map);
            return PgResultModel.unityResult(JSON.parseObject(result, PgResultModel.class));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Object checkTransfer(TransferModel transferModel) {
        PgParameterModel.TransferCheckModel tc = new PgParameterModel.TransferCheckModel();
        tc.setPartner(transferModel.getTGmApi().getAgyAcc());
        tc.setSign(transferModel.getTGmApi().getMd5Key());
        tc.setOrderNumber("0");
        tc.setTransationNo(transferModel.getOrderNo());
        return request(transferModel.getTGmApi().getApiUrl() + PgConstants.MethodConstatns.METHOD_TRANSFER_STATUS, (Map<String, String>) JSON.toJSON(tc));
    }

    @Override
    public Object getToken() {
        return null;
    }
}
