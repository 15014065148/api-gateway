package com.eveb.gateway.game.fun.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.game.fun.model.FgConstants;
import com.eveb.gateway.game.fun.model.FgParameterModel;
import com.eveb.gateway.game.fun.model.FgResultModel;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/1 16:42
 **/
@Slf4j
@Service
public class FgServiceImpl implements DepotService {

    @Autowired
    public OkHttpService okHttpService;

    @Override
    public Object tryPlayGame(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object playGame(PlayGameModel playGame) {
        FgParameterModel.GameModel game=new FgParameterModel.GameModel();
        game.setMember_code(playGame.getTGmApi().getPrefix()+playGame.getUserName());
        game.setGame_code(playGame.getGameId());
        game.setGame_type(FgConstants.DEFAULT_VALUE_GAMETYPE);
        game.setIp(ApplicationConstants.DEFAULT_IP);
        game.setLanguage(FgConstants.DEFAULT_VALUE_LANGUAGE);
        game.setReturn_url(FgConstants.DEFAULT_VALUE_RETURNURL);
        try {
            Response result = okHttpService.postFormRsp(okHttpService.initHeadClient(initHead(playGame.getTGmApi())), playGame.getTGmApi().getApiUrl()+FgConstants.MethodConstatns.METHOD_PLAYGAME, (Map<String, String>) JSON.toJSON(game));
            return FgResultModel.playRsCode(result);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Object openHall(PlayGameModel playGame) {
        String method=FgConstants.MethodConstatns.METHOD_OPEN_HALL+playGame.getTGmApi().getPrefix()+playGame.getUserName();
        return request(playGame.getTGmApi(),method);
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        FgParameterModel.RegisterModel reg = new FgParameterModel.RegisterModel();
        reg.setMember_code(register.getTGmApi().getPrefix() + register.getUserName());
        reg.setPassword(register.getPassword());
        return request(register.getTGmApi(),FgConstants.MethodConstatns.METHOD_REGISTER,reg);
    }

    @Override
    public Object login(LoginModel login) {
        return null;
    }

    @Override
    public Object logout(LoginModel login) {
        String method=FgConstants.MethodConstatns.METHOD_LOGOUT+login.getTGmApi().getPrefix()+login.getUserName();
        try {
            Response result = okHttpService.deleteRsp(okHttpService.initHeadClient(initHead(login.getTGmApi())), login.getTGmApi().getApiUrl()+method,"");
            return FgResultModel.delUnityResult(result);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Object deposit(TransferModel transfer) {
        return transfer(transfer);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        /**取负值为转出**/
        transfer.setAmount(transfer.getAmount().negate());
        return transfer(transfer);
    }

    @Override
    public Object queryBalance(LoginModel login) {
        String method=FgConstants.MethodConstatns.METHOD_BALANCE+login.getTGmApi().getPrefix()+login.getUserName();
        return request(login.getTGmApi(),method);
    }

    @Override
    public Object checkTransfer(TransferModel transfer) {
        String method=FgConstants.MethodConstatns.METHOD_TRANSFER_STATUS+transfer.getTGmApi().getPrefix()+transfer.getOrderNo();
        return request(transfer.getTGmApi(),method);
    }

    @Override
    public Object getToken() {
        return null;
    }

    private UnityResultModel transfer(TransferModel transfer) {
        FgParameterModel.TransferModel transferModel = new FgParameterModel.TransferModel();
        transferModel.setMember_code(transfer.getTGmApi().getPrefix() + transfer.getUserName());
        transferModel.setAmount(transfer.getAmount().intValue() * FgConstants.AMOUNT_UNIT);
        transferModel.setExternaltransactionid(transfer.getOrderNo());
        try {
            Response result = okHttpService.putFormRsp(okHttpService.initHeadClient(initHead(transfer.getTGmApi())), transfer.getTGmApi().getApiUrl() + FgConstants.MethodConstatns.METHOD_TRANSFER + transferModel.getMember_code(), (Map<String, String>) JSON.toJSON(transferModel));
            return FgResultModel.postUnityResult(result);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private UnityResultModel request(TGmApi api,String method) {
        try {
            Response result = okHttpService.getRsp(okHttpService.initHeadClient(initHead(api)), api.getApiUrl()+method);
            return FgResultModel.getUnityResult(result);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private UnityResultModel request(TGmApi api,String method, Object map) {
        try {
            Response result = okHttpService.postFormRsp(okHttpService.initHeadClient(initHead(api)), api.getApiUrl()+method, (Map<String, String>) JSON.toJSON(map));
            return FgResultModel.postUnityResult(result);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private Map initHead(TGmApi api) {
        Map head = new HashMap(3);
        head.put(FgConstants.MAP_ACCEPT,FgConstants.MAP_ACCEPT_VALUE);
        head.put(FgConstants.MAP_MHNAME, api.getAgyAcc());
        head.put(FgConstants.MAP_MHCODE, api.getMd5Key());
        return head;
    }
}
