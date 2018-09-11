package com.eveb.gateway.game.elg.service;

import com.eveb.gateway.game.elg.model.ElgConstants;
import com.eveb.gateway.game.elg.model.ElgParameterModel;
import com.eveb.gateway.game.elg.model.ElgResultModel;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import com.eveb.gateway.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
@Slf4j
@Service
public class ElgServiceImpl implements DepotService {

    @Autowired
    private ElgResultModel egResultModel;

    @Autowired
    public OkHttpService okHttpService;

    @Override
    public Object tryPlayGame(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object playGame(PlayGameModel playGame) {
        ElgParameterModel elgParameterModel = initParam(playGame);
        elgParameterModel.setPage((StringUtils.isEmpty(playGame.getGameId()) ? "100" : playGame.getGameId()));
        return playGame(elgParameterModel);
    }

    @Override
    public Object openHall(PlayGameModel playGame) {
        return playGame(playGame);
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        ElgParameterModel elgParameterModel = initParam(register);
        return createPlayer(elgParameterModel);
    }

    @Override
    public Object login(LoginModel login) {
        ElgParameterModel elgParameterModel = initParam(login);
        elgParameterModel.setPage("100");
        return playGame(elgParameterModel);
    }

    @Override
    public Object logout(LoginModel login) {
        return null;
    }

    @Override
    public Object deposit(TransferModel transfer) {
        ElgParameterModel elgParameterModel = initParam(transfer);
        elgParameterModel.setAmount(transfer.getAmount().toString());
        return transfer(elgParameterModel);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        ElgParameterModel elgParameterModel = initParam(transfer);
        elgParameterModel.setAmount(transfer.getAmount().multiply(new BigDecimal(-1)).toString());
        return transfer(elgParameterModel);
    }

    @Override
    public Object queryBalance(LoginModel login) {
        ElgParameterModel elgParameterModel = initParam(login);
        return queryBalance(elgParameterModel);
    }

    @Override
    public Object checkTransfer(TransferModel transfer) {
        return null;
    }

    @Override
    public Object getToken() {
        return null;
    }

    /**
     * 初始化参数
     */
    private ElgParameterModel initParam(UnityParameterModel unityParameterModel) {
        ElgParameterModel elgParameterModel = new ElgParameterModel();
        elgParameterModel.setKey(unityParameterModel.getTGmApi().getSecureCodes().get(ElgConstants.KEY));
        elgParameterModel.setPwd(unityParameterModel.getTGmApi().getSecureCodes().get(ElgConstants.PWD));
        elgParameterModel.setSystem(unityParameterModel.getTGmApi().getSecureCodes().get(ElgConstants.SYSTEM));
        elgParameterModel.setCurrency(unityParameterModel.getTGmApi().getSecureCodes().get(ElgConstants.CURRENCY));
        elgParameterModel.setLanguage(unityParameterModel.getTGmApi().getSecureCodes().get(ElgConstants.LANGUAGE));
        elgParameterModel.setIp(unityParameterModel.getTGmApi().getSecureCodes().get(ElgConstants.IP));
        elgParameterModel.setServer(unityParameterModel.getTGmApi().getSecureCodes().get(ElgConstants.SERVER));
        elgParameterModel.setPassword(unityParameterModel.getTGmApi().getSecureCodes().get(ElgConstants.PASSWORD));
        elgParameterModel.setUserName(unityParameterModel.getTGmApi().getPrefix() + unityParameterModel.getUserName());
        return elgParameterModel;
    }

    /**
     * 创建会员
     */
    private UnityResultModel createPlayer(ElgParameterModel elgParameterModel) {
        String result = createPlayerRequest(elgParameterModel);
        return egResultModel.getCreatePlayerResult(result);
    }

    /**
     * 会员存款/取款
     */
    private UnityResultModel transfer(ElgParameterModel elgParameterModel) {
        String result = transferRequest(elgParameterModel);
        return egResultModel.getTransferResult(result);
    }

    /**
     * 查询余额
     */
    private UnityResultModel queryBalance(ElgParameterModel elgParameterModel) {
        String result = queryBalanceRequest(elgParameterModel);
        return egResultModel.getQueryBalanceResult(result);
    }

    /**
     * 玩游戏
     */
    private UnityResultModel playGame(ElgParameterModel elgParameterModel) {
        String result = playGameRequest(elgParameterModel);
        return egResultModel.getPlayGameResult(result);
    }

    private String createPlayerRequest(ElgParameterModel elgParameterModel) {
        try {
            String tid = getTID();
            String hashStr=String.format(elgParameterModel.getCreateUserHash(), elgParameterModel.getIp(), tid, elgParameterModel.getKey(), elgParameterModel.getUserName(), elgParameterModel.getPassword(), elgParameterModel.getCurrency(), elgParameterModel.getPwd());
            String hash = MD5.getMD5(hashStr);
            String requestUrl = String.format(elgParameterModel.getCreateUserUrl(), elgParameterModel.getServer(), elgParameterModel.getKey(), elgParameterModel.getUserName(), elgParameterModel.getPassword(), tid, elgParameterModel.getCurrency(), elgParameterModel.getLanguage(), elgParameterModel.getIp(), hash);
            log.info("\n"+"ELG平台====>创建会员请求："+"\n"+"Hash: "+hashStr+"\n"+"请求URL: "+requestUrl);
            return okHttpService.get(okHttpService.proxyClient, requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String transferRequest(ElgParameterModel elgParameterModel) {
        try {
            String tid = getTID();
            String hashStr=String.format(elgParameterModel.getTransferHash(), elgParameterModel.getIp(), tid, elgParameterModel.getKey(), elgParameterModel.getSystem(), elgParameterModel.getAmount(), elgParameterModel.getUserName(), elgParameterModel.getCurrency(), elgParameterModel.getPwd());
            String hash = MD5.getMD5(hashStr);
            String requestUrl = String.format(elgParameterModel.getTransferUrl(), elgParameterModel.getServer(), elgParameterModel.getKey(), elgParameterModel.getUserName(), elgParameterModel.getSystem(), elgParameterModel.getAmount(), tid, elgParameterModel.getCurrency(), hash);
            log.info("\n"+"ELG平台====>查询转账请求："+"\n"+"Hash: "+hashStr+"\n"+"请求URL: "+requestUrl);
            return okHttpService.get(okHttpService.proxyClient, requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String queryBalanceRequest(ElgParameterModel elgParameterModel) {
        try {
            String tid = getTID();
            String hashStr=String.format(elgParameterModel.getBalanceHash(), elgParameterModel.getIp(), tid, elgParameterModel.getKey(), elgParameterModel.getSystem(), elgParameterModel.getUserName(), elgParameterModel.getPwd());
            String hash = MD5.getMD5(hashStr);
            String requestUrl = String.format(elgParameterModel.getBalanceUrl(), elgParameterModel.getServer(), elgParameterModel.getKey(), elgParameterModel.getUserName(), elgParameterModel.getSystem(), tid, hash);
            log.info("\n"+"ELG平台====>查询余额请求："+"\n"+"Hash: "+hashStr+"\n"+"请求URL: "+requestUrl);
            return okHttpService.get(okHttpService.proxyClient, requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String playGameRequest(ElgParameterModel elgParameterModel) {
        try {
            String tid = getTID();
            String hashStr=String.format(elgParameterModel.getPlayGameHash(), elgParameterModel.getIp(), tid, elgParameterModel.getKey(), elgParameterModel.getUserName(), elgParameterModel.getPassword(), elgParameterModel.getSystem(), elgParameterModel.getPwd());
            String hash = MD5.getMD5(hashStr);
            String requestUrl = String.format(elgParameterModel.getPlayGameUrl(), elgParameterModel.getServer(), elgParameterModel.getKey(), elgParameterModel.getUserName(), elgParameterModel.getPassword(), elgParameterModel.getSystem(), tid, hash, elgParameterModel.getPage(), elgParameterModel.getIp());
            log.info("\n"+"ELG平台====>玩游戏/登陆请求："+"\n"+"Hash: "+hashStr+"\n"+"请求URL: "+requestUrl);
            return okHttpService.get(okHttpService.proxyClient, requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTID() {
        return UUID.randomUUID().toString();
    }

}
