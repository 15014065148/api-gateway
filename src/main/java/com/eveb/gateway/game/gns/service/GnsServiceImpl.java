package com.eveb.gateway.game.gns.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.gns.model.GnsConstants;
import com.eveb.gateway.game.gns.model.GnsParameterModel;
import com.eveb.gateway.game.gns.model.GnsResultModel;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.model.TGmGame;
import com.eveb.gateway.game.service.GameService;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.service.SysService;
import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import com.eveb.gateway.game.vr.model.VrConstants;
import com.eveb.gateway.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GnsServiceImpl implements DepotService {

    @Autowired
    public OkHttpService okHttpService;
    @Autowired
    private SysService sysService;
    @Autowired
    private GameService gameService;
    private static final String DEPOT_NAME = PlatFromEnum.ENUM_GNS.getKey();

    @Override
    public Object tryPlayGame(PlayGameModel playGame) {
        LoginModel login = new LoginModel();
        login.setUserName(playGame.getUserName());
        login.setSiteCode(playGame.getSiteCode());
        TGmApi api = playGame.getTGmApi();
        GnsResultModel.Balance balance = getBalance(login, api);
        Map map = JSON.parseObject(JSON.toJSONString(balance));
        if (map != null) {
            try {
                TGmGame gmGame = gameService.queryGame(DEPOT_NAME, playGame.getGameId());
                String resultStr = gmGame.getUrl() + String.format(GnsConstants.URL_PLAYGAME_TRY, api.getAgyAcc(), map.get(GnsConstants.JsonKey.KEY_SSESSION));
                if (!StringUtils.isEmpty(resultStr)) {
                    return new UnityResultModel(true, resultStr);
                }
                return new UnityResultModel(false, ApplicationConstants.FAIL);
            } catch (Exception e) {
                return new UnityResultModel(false, ApplicationConstants.FAIL);
            }
        }
        return new UnityResultModel(false, ApplicationConstants.FAIL);
    }

    private GnsResultModel.Balance getBalance(LoginModel login, TGmApi api) {
        GnsResultModel.Balance balance = null;
        try {
            String rs = okHttpService.get(okHttpService.initHeadClient(initHeand(api)), api.getPcUrl() + GnsConstants.URL_BANALCE + api.getPrefix() + login.getUserName());
            balance = JSON.parseObject(rs, GnsResultModel.Balance.class);
            balance.setInternal_balance(balance.getInternal_balance().divide(BigDecimal.valueOf(100)));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return balance;
    }

    @Override
    public Object playGame(PlayGameModel playGame) {
        LoginModel login = new LoginModel();
        login.setUserName(playGame.getUserName());
        login.setSiteCode(playGame.getSiteCode());
        TGmApi api = playGame.getTGmApi();
        GnsResultModel.Balance balance = getBalance(login, api);
        Map map = JSON.parseObject(JSON.toJSONString(balance));
        if (map != null) {
            try {
                TGmGame gmGame = gameService.queryGame(DEPOT_NAME, playGame.getGameId());
                String resultStr = gmGame.getUrl() + String.format(GnsConstants.URL_PLAYGAME, api.getAgyAcc(), map.get(GnsConstants.JsonKey.KEY_SSESSION));
                if (!StringUtils.isEmpty(resultStr)) {
                    return new UnityResultModel(true, resultStr);
                }
                return new UnityResultModel(false, ApplicationConstants.FAIL);
            } catch (Exception e) {
                return new UnityResultModel(false, ApplicationConstants.FAIL);
            }
        }
        return new UnityResultModel(false, ApplicationConstants.FAIL);
    }

    @Override
    public Object openHall(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        TransferModel transfer = new TransferModel();
        transfer.setSiteCode(register.getSiteCode());
        transfer.setUserName(register.getUserName());
        transfer.setTGmApi(register.getTGmApi());
        return transfer(transfer, GnsConstants.Transfer.TF_DEPOSIT);
    }

    @Override
    public Object login(LoginModel login) {
        return null;
    }

    @Override
    public Object logout(LoginModel login) {
        return null;
    }

    @Override
    public Object deposit(TransferModel transfer) {
        return transfer(transfer, GnsConstants.Transfer.TF_WITHDRAW);
    }

    private Object transfer(TransferModel transfer, String transferType) {
        TGmApi api = transfer.getTGmApi();
        if (api == null) {
            return null;
        }
        GnsParameterModel.Transfer transferPara = new GnsParameterModel.Transfer();
        transferPara.setUser_id(api.getPrefix() + transfer.getUserName());
        transferPara.setPartner_id(api.getAgyAcc());
        transferPara.setCurrency(GnsConstants.CURRENCY);
        transferPara.setCredits(transfer.getAmount() == null ? 0 : transfer.getAmount().longValue() * GnsConstants.AMOUNT_UNIT);
        transferPara.setAction(transferType);
        transferPara.setExternal_transaction_id(transfer.getOrderNo() == null ? new SnowFlake().nextId() + "" : transfer.getOrderNo());
        try {
            Map<String, Object> map = JSON.parseObject(okHttpService.postJson(okHttpService.initHeadClient(initHeand(api)), api.getPcUrl() + GnsConstants.URL_TRANSFER, transferPara), Map.class);
            if (!StringUtils.isEmpty(map.get("transaction_id"))) {
                BigDecimal bigDecimal=new BigDecimal( map.get("internal_balance").toString());
                bigDecimal=bigDecimal.divide(BigDecimal.valueOf(100));
                return new UnityResultModel(true, bigDecimal);
            } else {
                return new UnityResultModel(false, getError(map));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new UnityResultModel(false, ApplicationConstants.FAIL);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        return transfer(transfer, GnsConstants.Transfer.TF_DEPOSIT);
    }

    @Override
    public Object queryBalance(LoginModel login) {
        TGmApi api = login.getTGmApi();
        if (api == null) {
            return null;
        }
        String rs = "";
        try {
            rs = okHttpService.get(okHttpService.initHeadClient(initHeand(api)), api.getPcUrl() + GnsConstants.URL_BANALCE + api.getPrefix() + login.getUserName());
            GnsResultModel.Balance balance = JSON.parseObject(rs, GnsResultModel.Balance.class);
            balance.setInternal_balance(balance.getInternal_balance().divide(BigDecimal.valueOf(100)));
            return new UnityResultModel(true, balance.getInternal_balance());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new UnityResultModel(false, getError(JSON.parseObject(rs, Map.class)));
    }

    @Override
    public Object checkTransfer(TransferModel transferModel) {
        TGmApi api = transferModel.getTGmApi();
        Map<String, Object> map = null;
        try {
            map = JSON.parseObject(okHttpService.get(okHttpService.initHeadClient(initHeand(api)), api.getPcUrl() + GnsConstants.URL_QUERYTRANSFER + transferModel.getOrderNo()), Map.class);
            if (!StringUtils.isEmpty(map.get("balance"))) {
                return new UnityResultModel(true, map.get("balance"));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new UnityResultModel(false, getError(map));
    }

    private Map initHeand(TGmApi api) {
        Map map = new HashMap();
        map.put(GnsConstants.HEAD_PARTNER, api.getAgyAcc());
        map.put(GnsConstants.HEAD_GENESIS, api.getMd5Key());
        return map;
    }

    @Override
    public Object getToken() {
        return null;
    }

    private String getError(Map<String, Object> map) {
        return JSON.parseObject(map.get("error").toString(), Map.class).get("reason").toString();
    }
}
