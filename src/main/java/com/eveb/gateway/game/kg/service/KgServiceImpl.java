package com.eveb.gateway.game.kg.service;

import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.game.kg.model.KgConstants;
import com.eveb.gateway.game.kg.model.KgParameterModel;
import com.eveb.gateway.game.kg.model.KgResultModel;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;

@Slf4j
@Service
public class KgServiceImpl implements DepotService {
    @Autowired
    private KgResultModel kgResultModel;

    @Autowired
    public OkHttpService okHttpService;

    @Override
    public Object tryPlayGame(PlayGameModel playGame) {

        return null;
    }

    /**
     * kgParameterModel对象的gameType，时时彩=2 ,北京PK10=3,六合彩=4，不传默认进大厅
     */
    @Override
    public Object playGame(PlayGameModel playGame) {
        KgParameterModel kgParameterModel = initParam(playGame);
        if (!StringUtils.isEmpty(playGame.getGameId())) {
            kgParameterModel.setGameType(playGame.getGameId());
        }
        return createPlayer(kgParameterModel);
    }

    /**
     * kgParameterModel对象的gameType=0时，表示打开大厅，默认在数据库中配置gameType=0
     */
    @Override
    public Object openHall(PlayGameModel playGame) {
        KgParameterModel kgParameterModel = initParam(playGame);
        if (StringUtil.isNotEmpty(playGame.getOrigin())) {
            kgParameterModel.setEditPassword(getOrigin(playGame.getOrigin()));
        }
        return createPlayer(kgParameterModel);
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        KgParameterModel kgParameterModel = initParam(register);
        return createPlayer(kgParameterModel);
    }

    @Override
    public Object login(LoginModel login) {
        KgParameterModel kgParameterModel = initParam(login);
        return createPlayer(kgParameterModel);
    }

    @Override
    public Object logout(LoginModel login) {
        return null;
    }

    @Override
    public Object deposit(TransferModel transfer) {
        KgParameterModel kgParameterModel = initParam(transfer);
        kgParameterModel.setAmount(String.valueOf(transfer.getAmount().setScale(2, BigDecimal.ROUND_DOWN)));
        return transfer(kgParameterModel);
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
        KgParameterModel kgParameterModel = initParam(transfer);
        kgParameterModel.setAmount(String.valueOf(transfer.getAmount().setScale(2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(-1))));
        return transfer(kgParameterModel);
    }

    @Override
    public Object queryBalance(LoginModel login) {
        KgParameterModel kgParameterModel = initParam(login);
        return queryBalance(kgParameterModel);
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
     * 创建会员/会员登陆/玩游戏/打开大厅
     */
    private UnityResultModel createPlayer(KgParameterModel kgParameterModel) {
        String resultXml = createPlayerRequest(kgParameterModel);
        return kgResultModel.getResult(resultXml);
    }

    /**
     * 查询余额
     */
    private UnityResultModel queryBalance(KgParameterModel kgParameterModel) {
        String resultXml = queryBalanceRequest(kgParameterModel);
        return kgResultModel.getResult(resultXml);
    }

    /**
     * 会员转账
     */
    private UnityResultModel transfer(KgParameterModel kgParameterModel) {
        String firstResultXml = firstTransferRequest(kgParameterModel);
        UnityResultModel UnityResultModel = kgResultModel.getResult(firstResultXml);
        if (UnityResultModel.getCode()) {
            kgParameterModel.setFundIntegrationId(UnityResultModel.getMessage().toString());
            String confirmResultXml = confirmTransferRequest(kgParameterModel);
            return kgResultModel.getResult(confirmResultXml);
        } else {
            return new UnityResultModel(false, "转账失败！");
        }
    }

    private String createPlayerRequest(KgParameterModel kgParameterModel) {
        String url = kgParameterModel.getUrl() + kgParameterModel.getEnterUrl();
        String param = String.format(kgParameterModel.getCommonRequestParam(), kgParameterModel.getVendorsite(), kgParameterModel.getFundLink(), kgParameterModel.getVendorId(), kgParameterModel.getPlayerId(), kgParameterModel.getPlayerRealName(), kgParameterModel.getPlayerCurrency(), kgParameterModel.getPlayerCredit(), kgParameterModel.getPlayerAllowStake(), kgParameterModel.getTrial(), kgParameterModel.getGameType(), kgParameterModel.getLanguage(), kgParameterModel.getApiKey(), kgParameterModel.getEditPassword(), kgParameterModel.getRebateLevel(), kgParameterModel.getPlayerIP(), kgParameterModel.getVendorRef(), kgParameterModel.getRemarks());
        log.info("\n" + "KG平台====>：" + "\n" + "请求URL: " + "\n" + url + "\n" + "请求参数: " + "\n" + param);
        return okHttpService.postXml(okHttpService.proxyClient, url, param);
    }

    private String firstTransferRequest(KgParameterModel kgParameterModel) {
        String url = kgParameterModel.getUrl() + kgParameterModel.getFirstTransferUrl();
        String param = String.format(kgParameterModel.getFirstTransferRequestParam(), kgParameterModel.getVendorId(), kgParameterModel.getPlayerId(), kgParameterModel.getAmount(), kgParameterModel.getApiKey());
        log.info("\n" + "KG平台====>：" + "\n" + "请求URL: " + "\n" + url + "\n" + "请求参数: " + "\n" + param);
        return okHttpService.postXml(okHttpService.proxyClient, url, param);
    }

    private String confirmTransferRequest(KgParameterModel kgParameterModel) {
        String url = kgParameterModel.getUrl() + kgParameterModel.getConfirmTransferUrl();
        String param = String.format(kgParameterModel.getConfirmTransferRequestParam(), kgParameterModel.getVendorId(), kgParameterModel.getPlayerId(), kgParameterModel.getAmount(), kgParameterModel.getPlayerIP(), kgParameterModel.getApiKey(), kgParameterModel.getFundIntegrationId(), kgParameterModel.getVendorRef());
        log.info("\n" + "KG平台====>：" + "\n" + "请求URL: " + "\n" + url + "\n" + "请求参数: " + "\n" + param);
        return okHttpService.postXml(okHttpService.proxyClient, url, param);
    }

    private String queryBalanceRequest(KgParameterModel kgParameterModel) {
        String url = kgParameterModel.getUrl() + kgParameterModel.getQueryUrl();
        String param = String.format(kgParameterModel.getQueryBalanceRequestParam(), kgParameterModel.getVendorId(), kgParameterModel.getPlayerId(), kgParameterModel.getAmount(), kgParameterModel.getPlayerIP(), kgParameterModel.getFundIntegrationId(), kgParameterModel.getVendorRef(), kgParameterModel.getApiKey());
        log.info("\n" + "KG平台====>：" + "\n" + "请求URL: " + "\n" + url + "\n" + "请求参数: " + "\n" + param);
        return okHttpService.postXml(okHttpService.proxyClient, url, param);
    }

    /**
     * 统一初始化参数
     */
    private KgParameterModel initParam(UnityParameterModel unityParameterModel) {
        KgParameterModel kgParameterModel = new KgParameterModel();
        kgParameterModel.setUrl(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.URL));
        kgParameterModel.setEnterUrl(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.ENTERURL));
        kgParameterModel.setFirstTransferUrl(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.FIRSTTRANSFERURL));
        kgParameterModel.setConfirmTransferUrl(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.CONFIRMTRANSFERURL));
        kgParameterModel.setQueryUrl(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.QUERYURL));
        kgParameterModel.setVendorId(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.VENDORID));
        kgParameterModel.setApiKey(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.APIKEY));
        kgParameterModel.setRemarks(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.REMARKS));
        kgParameterModel.setPlayerIP(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.PLAYERIP));
        kgParameterModel.setVendorsite(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.VENDORSITE));
        kgParameterModel.setFundLink(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.FUNDLINK));
        kgParameterModel.setVendorId(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.VENDORID));
        kgParameterModel.setPlayerCurrency(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.PLAYERCURRENCY));
        kgParameterModel.setPlayerCredit(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.PLAYERCREDIT));
        kgParameterModel.setPlayerAllowStake(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.PLAYERALLOWSTAKE));
        kgParameterModel.setTrial(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.TRIAL));
        kgParameterModel.setLanguage(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.LANGUAGE));
        kgParameterModel.setEditPassword(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.EDITPASSWORD));
        kgParameterModel.setRebateLevel(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.REBATELEVEL));
        kgParameterModel.setPlayerId(unityParameterModel.getTGmApi().getPrefix() + unityParameterModel.getUserName());
        kgParameterModel.setPlayerRealName(kgParameterModel.getPlayerId());
        kgParameterModel.setGameType(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.GAMETYPE));
        kgParameterModel.setVendorRef(unityParameterModel.getTGmApi().getSecureCodes().get(KgConstants.VENDORREF));
        return kgParameterModel;
    }

    /**
     * 选择电脑端/手机端
     */
    private String getOrigin(String origin) {
        switch (origin) {
            case ApplicationConstants.ORIGIN_PC: {
                return "0";
            }
            case ApplicationConstants.ORIGIN_APP: {
                return "1";
            }
            case ApplicationConstants.ORIGIN_H5: {
                return "1";
            }
        }
        return "1";
    }
}
