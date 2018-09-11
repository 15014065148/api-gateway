package com.eveb.gateway.game.ttg.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.model.TGmGame;
import com.eveb.gateway.game.service.GameService;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.ttg.model.TtgConstants;
import com.eveb.gateway.game.ttg.model.TtgPrameterModel;
import com.eveb.gateway.game.ttg.model.TtgResultModel;
import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/8/29 17:51
 **/
@Slf4j
@Service
public class TtgServiceImpl implements DepotService {

    @Autowired
    public OkHttpService okHttpService;
    @Autowired
    private GameService gameService;
    private static final String DEPOT_NAME = PlatFromEnum.ENUM_TTG.getKey();

    @Override
    public Object tryPlayGame(PlayGameModel playGame) {
        TGmGame gmGame = gameService.queryGame(DEPOT_NAME, playGame.getGameId());
        String url;
        if (ApplicationConstants.ORIGIN_H5.equals(playGame.getOrigin())) {
            url = String.format(playGame.getTGmApi().getMbUrl(),
                    gmGame.getGameNameEn(), gmGame.getMbGameCode(), ((Map) JSON.parse(gmGame.getGameParam())).get(ApplicationConstants.ORIGIN_H5));
        } else {
            url = String.format(playGame.getTGmApi().getMbUrl(),
                    gmGame.getGameNameEn(), gmGame.getGameCode(), ((Map) JSON.parse(gmGame.getGameParam())).get(ApplicationConstants.ORIGIN_PC));
        }
        return new UnityResultModel(false, url);
    }

    @Override
    public Object playGame(PlayGameModel playGame) {
        LoginModel lg = new LoginModel();
        lg.setUserName(playGame.getUserName());
        lg.setTGmApi(playGame.getTGmApi());
        UnityResultModel un = (UnityResultModel) login(lg);
        TGmGame gmGame = gameService.queryGame(DEPOT_NAME, playGame.getGameId());
        String resultStr;
        if (gmGame != null && un.getCode()) {
            if (ApplicationConstants.ORIGIN_H5.equals(playGame.getOrigin())) {
                resultStr = playGame.getTGmApi().getPcUrl2() + String.format(TtgConstants.MethodConstants.PLAYGAME, un.getMessage(),
                        gmGame.getGameNameEn(), gmGame.getMbGameCode(), ((Map) JSON.parse(gmGame.getGameParam())).get(ApplicationConstants.ORIGIN_H5), "HTML5");
            } else {
                resultStr = playGame.getTGmApi().getPcUrl2() + String.format(TtgConstants.MethodConstants.PLAYGAME, un.getMessage(),
                        gmGame.getGameNameEn(), gmGame.getGameCode(), ((Map) JSON.parse(gmGame.getGameParam())).get(ApplicationConstants.ORIGIN_PC), "Flash");
            }
            return new UnityResultModel(Boolean.TRUE, resultStr);
        }
        return new UnityResultModel(false, ApplicationConstants.FAIL);
    }

    @Override
    public Object openHall(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        String url = String.format(TtgConstants.MethodConstants.LOGIN, register.getTGmApi().getApiUrl(), register.getTGmApi().getPrefix() + register.getUserName());
        return TtgResultModel.unityLoginResult(okHttpService.postXml(okHttpService.proxyClient, url, new TtgPrameterModel.LoginModel().toString()));
    }

    @Override
    public Object login(LoginModel login) {
        String url = String.format(TtgConstants.MethodConstants.LOGIN, login.getTGmApi().getApiUrl(), login.getTGmApi().getPrefix() + login.getUserName());
        return TtgResultModel.unityLoginResult(okHttpService.postXml(okHttpService.proxyClient, url, new TtgPrameterModel.LoginModel().toString()));
    }

    @Override
    public Object logout(LoginModel login) {
        return null;
    }

    @Override
    public Object deposit(TransferModel transfer) {
        return transfer(transfer);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        /**取反**/
        transfer.setAmount(transfer.getAmount().negate());
        return transfer(transfer);
    }

    @Override
    public Object queryBalance(LoginModel login) {
        String url = String.format(TtgConstants.MethodConstants.BALANCE, login.getTGmApi().getApiUrl(), login.getTGmApi().getPrefix() + login.getUserName());
        return TtgResultModel.unityBalanceResult(getRequest(url));

    }

    @Override
    public Object checkTransfer(TransferModel transfer) {
        String url = String.format(TtgConstants.MethodConstants.CHECKTRANSFER, transfer.getTGmApi().getApiUrl(), transfer.getTGmApi().getAgyAcc(), transfer.getOrderNo());
        return TtgResultModel.unityChkTransferResult(getRequest(url));
    }

    private Object transfer(TransferModel transfer) {
        String url = String.format(TtgConstants.MethodConstants.TRANSFER, transfer.getTGmApi().getApiUrl(), transfer.getTGmApi().getAgyAcc(), transfer.getOrderNo());
        TtgPrameterModel.TransferModel tf = new TtgPrameterModel.TransferModel();
        tf.setUid(transfer.getTGmApi().getPrefix() + transfer.getUserName());
        tf.setAmount(transfer.getAmount());
        return TtgResultModel.unityTransferResult(okHttpService.postXmlRsp(okHttpService.proxyClient, url, tf.toString()));
    }

    private Response getRequest(String url) {
        try {
            return okHttpService.getRsp(okHttpService.proxyClient, url);
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getToken() {
        return null;
    }
}
