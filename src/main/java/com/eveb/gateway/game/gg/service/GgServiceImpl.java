package com.eveb.gateway.game.gg.service;

import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.game.gg.model.GgConstants;
import com.eveb.gateway.game.gg.model.GgParameterModel;
import com.eveb.gateway.game.gg.model.GgResultModel;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.unity.model.LoginModel;
import com.eveb.gateway.game.unity.model.PlayGameModel;
import com.eveb.gateway.game.unity.model.RegisterModel;
import com.eveb.gateway.game.unity.model.TransferModel;
import com.eveb.gateway.game.unity.service.DepotService;
import com.eveb.gateway.utils.DESEncrypt;
import com.eveb.gateway.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/6 11:46
 **/
@Slf4j
@Service
public class GgServiceImpl implements DepotService {

    @Autowired
    public OkHttpService okHttpService;

    @Override
    public Object tryPlayGame(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object playGame(PlayGameModel playGame) {
        return playGames(playGame);
    }

    @Override
    public Object openHall(PlayGameModel playGame) {
        playGame.setGameId(GgConstants.OPEN_HALL_GAMETYPE);
        return playGames(playGame);
    }

    private Object playGames(PlayGameModel playGame){

        GgParameterModel.PlayGameModel ggpg=new GgParameterModel.PlayGameModel();
        ggpg.setCagent(playGame.getTGmApi().getAgyAcc());
        ggpg.setLoginname(playGame.getTGmApi().getPrefix()+playGame.getUserName());
        ggpg.setPassword(ApplicationConstants.DEFAULT_USER_PASSWORD);
        ggpg.setGametype(playGame.getGameId());
        ggpg.setSid(playGame.getTGmApi().getAgyAcc()+UUID.randomUUID().toString());
        ggpg.setLang(ApplicationConstants.DEFAULT_LANG);
        return GgResultModel.playGameUnityResult(request(playGame.getTGmApi(),ggpg.toString()));
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        GgParameterModel.RegisterModel reg = new GgParameterModel.RegisterModel();
        reg.setCagent(register.getTGmApi().getAgyAcc());
        reg.setLoginname(register.getTGmApi().getPrefix()+register.getUserName());
        reg.setPassword(register.getPassword());
        reg.setCur(ApplicationConstants.DEFAULT_CURRENCY);
        return GgResultModel.postUnityResult(request(register.getTGmApi(),reg.toString()));
    }

    @Override
    public Object login(LoginModel login) {
        return null;
    }

    @Override
    public Object logout(LoginModel login) {
        GgParameterModel.LogoutModel logout=new GgParameterModel.LogoutModel();
        logout.setCagent(login.getTGmApi().getAgyAcc());
        logout.setLoginname(login.getTGmApi().getPrefix()+login.getUserName());
        logout.setPassword(login.getPassword());
        return GgResultModel.postUnityResult(request(login.getTGmApi(),logout.toString()));
    }

    @Override
    public Object deposit(TransferModel transfer) {
        return transfer(transfer,GgConstants.TRANSFER_IN);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        return transfer(transfer,GgConstants.TRANSFER_OUT);
    }

    private Object transfer(TransferModel transfer,String type){
        GgParameterModel.TransferModel ggtf=new GgParameterModel.TransferModel();
        ggtf.setCagent(transfer.getTGmApi().getAgyAcc());
        ggtf.setLoginname(transfer.getTGmApi().getPrefix()+transfer.getUserName());
        ggtf.setPassword(ApplicationConstants.DEFAULT_USER_PASSWORD);
        ggtf.setBillno(transfer.getTGmApi().getAgyAcc()+transfer.getOrderNo());
        ggtf.setCredit(transfer.getAmount());
        ggtf.setType(type);
        ggtf.setCur(ApplicationConstants.DEFAULT_CURRENCY);
        return GgResultModel.postUnityResult(request(transfer.getTGmApi(),ggtf.toString()));
    }

    @Override
    public Object queryBalance(LoginModel login) {
        GgParameterModel.BalanceModel bal = new GgParameterModel.BalanceModel();
        bal.setCagent(login.getTGmApi().getAgyAcc());
        bal.setLoginname(login.getTGmApi().getPrefix()+login.getUserName());
        bal.setPassword(login.getPassword());
        bal.setCur(ApplicationConstants.DEFAULT_CURRENCY);
        return GgResultModel.postUnityResult(request(login.getTGmApi(),bal.toString()));
    }

    @Override
    public Object checkTransfer(TransferModel transfer) {
        GgParameterModel.TransferStatusModel ts = new GgParameterModel.TransferStatusModel();
        ts.setCagent(transfer.getTGmApi().getAgyAcc());
        ts.setBillno(transfer.getTGmApi().getAgyAcc()+transfer.getOrderNo());
        return GgResultModel.postUnityResult(request(transfer.getTGmApi(),ts.toString()));
    }

    private String request(TGmApi api,String para) {
        String cagent = api.getSecureCodes().get(GgConstants.MAP_CAGENT);
        String desKey = api.getSecureCodes().get(GgConstants.MAP_DES_KEY);
        String md5Key = api.getSecureCodes().get(GgConstants.MAP_MD5_KEY);
        DESEncrypt des = new DESEncrypt(desKey);
        try {
            String params = des.encrypt(para);
            String key = MD5.getMD5(params + md5Key);
            Map<String, String> mapParam = new HashMap<>(2);
            mapParam.put("params", params);
            mapParam.put("key", key);
            Map<String, String> mapHead = new HashMap<>(1);
            mapHead.put("GGaming", "WEB_GG_GI_" + cagent);
            return okHttpService.get(okHttpService.initHeadClient(mapHead), api.getSecureCodes().get(GgConstants.MAP_APIURL), mapParam);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Object getToken() {
        return null;
    }
}
