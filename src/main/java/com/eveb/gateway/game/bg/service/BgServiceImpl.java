package com.eveb.gateway.game.bg.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.game.bg.model.BgConstants;
import com.eveb.gateway.game.bg.model.BgParameterModel;
import com.eveb.gateway.game.bg.model.BgResultModel;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.unity.model.*;
import com.eveb.gateway.game.unity.service.DepotService;
import com.eveb.gateway.utils.HashUtil;
import com.eveb.gateway.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class BgServiceImpl implements DepotService {

    @Autowired
    private BgResultModel bgResultModel;
    @Autowired
    public OkHttpService okHttpService;
    private SimpleDateFormat bgDateFormat = new SimpleDateFormat(BgConstants.DATEFORMAT);

    @Override
    public Object tryPlayGame(PlayGameModel playGame) {
        BgParameterModel bgParameterModel = initBgTryGameParameterModel(playGame);
        bgParameterModel.setMethod(getTryGameMethod(playGame.getGameType(), playGame));
        return tryPlayGame(bgParameterModel);
    }

    @Override
    public Object playGame(PlayGameModel playGame) {
        String secretCode = HashUtil.sha1Base64(playGame.getTGmApi().getSecureCodes().get(BgConstants.AGENTPASSWORD));
        String secretKey = playGame.getTGmApi().getSecureCodes().get(BgConstants.SECRETKEY);
        BgParameterModel bgParameterModel = initBgParameterModel(playGame);
        bgParameterModel.setMethod(getGameMethod(playGame.getGameType(), playGame));
        bgParameterModel.setGameType(playGame.getGameType());
        bgParameterModel.setSign(MD5.getMD5(bgParameterModel.getRandom() + bgParameterModel.getSn() + secretKey));
        bgParameterModel.setDigest(MD5.getMD5(bgParameterModel.getRandom() + bgParameterModel.getSn() + bgParameterModel.getLoginId() + secretCode));
        bgParameterModel.setLocale(playGame.getTGmApi().getSecureCodes().get(BgConstants.LOCALE));
        setOrigin(playGame.getOrigin(), bgParameterModel);
        return playGame(bgParameterModel);
    }

    @Override
    public Object openHall(PlayGameModel playGame) {
        return playGame(playGame);
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        String secretCode = HashUtil.sha1Base64(register.getTGmApi().getSecureCodes().get(BgConstants.AGENTPASSWORD));
        BgParameterModel bgParameterModel = initBgParameterModel(register);
        bgParameterModel.setMethod(register.getTGmApi().getSecureCodes().get(BgConstants.GREATEUSER_METHOD));
        bgParameterModel.setDigest(MD5.getMD5(bgParameterModel.getRandom() + bgParameterModel.getSn() + secretCode));
        bgParameterModel.setNickname(bgParameterModel.getNickname());
        bgParameterModel.setAgentLoginId(register.getTGmApi().getSecureCodes().get(BgConstants.AGENTLOGINID));
        return createUser(bgParameterModel);
    }

    @Override
    public Object login(LoginModel login) {
        PlayGameModel playGame = new PlayGameModel();
        playGame.setTGmApi(login.getTGmApi());
        playGame.setUserName(login.getUserName());
        playGame.setGameType(BgConstants.VIDEOGAME_METHOD);
        return playGame(playGame);
    }

    @Override
    public Object logout(LoginModel login) {
        String secretCode = HashUtil.sha1Base64(login.getTGmApi().getSecureCodes().get(BgConstants.AGENTPASSWORD));
        BgParameterModel bgParameterModel = initBgParameterModel(login);
        bgParameterModel.setMethod(login.getTGmApi().getSecureCodes().get(BgConstants.USERLOGOUT_METHOD));
        bgParameterModel.setDigest(MD5.getMD5(bgParameterModel.getRandom() + bgParameterModel.getSn() + bgParameterModel.getLoginId() + secretCode));
        return logout(bgParameterModel);
    }

    @Override
    public Object deposit(TransferModel transfer) {
        BgParameterModel bgParameterModel = getBgParameterModel(transfer);
        return transfer(bgParameterModel);
    }


    @Override
    public Object withdrawal(TransferModel transfer) {
        transfer.setAmount(transfer.getAmount().negate());
        BgParameterModel bgParameterModel = getBgParameterModel(transfer);
        return transfer(bgParameterModel);
    }

    @Override
    public Object queryBalance(LoginModel login) {
        String secretCode = HashUtil.sha1Base64(login.getTGmApi().getSecureCodes().get(BgConstants.AGENTPASSWORD));
        BgParameterModel bgParameterModel = initBgParameterModel(login);
        bgParameterModel.setMethod(login.getTGmApi().getSecureCodes().get(BgConstants.USEBALANCE_METHOD));
        bgParameterModel.setDigest(MD5.getMD5(bgParameterModel.getRandom() + bgParameterModel.getSn() + bgParameterModel.getLoginId() + secretCode));
        return queryBalance(bgParameterModel);
    }

    @Override
    public Object checkTransfer(TransferModel transfer) {
        String secretKey = transfer.getTGmApi().getSecureCodes().get(BgConstants.SECRETKEY);
        BgParameterModel bgParameterModel = initBgParameterModel(transfer);
        bgParameterModel.setMethod(transfer.getTGmApi().getSecureCodes().get(BgConstants.CHECKTRANSFER_METHOD));
        bgParameterModel.setSign(MD5.getMD5(bgParameterModel.getRandom() + bgParameterModel.getSn() + secretKey));
        bgParameterModel.setEndTime(new Date());
        bgParameterModel.setStartTime(getCheckTransferStartTime(bgParameterModel.getEndTime()));
        bgParameterModel.setBizId(Long.parseLong(transfer.getOrderNo()));
        bgParameterModel.setPageIndex(1);
        bgParameterModel.setPageSize(10);
        return checkTransfer(bgParameterModel);
    }

    @Override
    public Object getToken() {
        return null;
    }

    /**
     * 创建会员
     */
    private UnityResultModel createUser(BgParameterModel bgParameterModel) {
        Map<String, Object> map = createUserRequest(bgParameterModel);
        return bgResultModel.getCreateUserResult(map);
    }

    /**
     * 会员登出
     */
    private UnityResultModel logout(BgParameterModel bgParameterModel) {
        Map<String, Object> map = userLogOutRequest(bgParameterModel);
        return bgResultModel.getUserLogOutResult(map);
    }

    /**
     * 会员存款
     */
    private UnityResultModel transfer(BgParameterModel bgParameterModel) {
        Map<String, Object> map = transferRequest(bgParameterModel);
        return bgResultModel.getTransferResult(map);
    }

    /**
     * 会员查询余额
     */
    private UnityResultModel queryBalance(BgParameterModel bgParameterModel) {
        Map<String, Object> map = queryBalanceRequest(bgParameterModel);
        return bgResultModel.getQueryBalanceResult(map);
    }

    /**
     * 会员确认转账
     */
    private UnityResultModel checkTransfer(BgParameterModel bgParameterModel) {
        Map<String, Object> map = checkTransferRequest(bgParameterModel);
        return bgResultModel.getcheckTransferResult(map);
    }

    /**
     * 会员玩游戏
     */
    private UnityResultModel playGame(BgParameterModel bgParameterModel) {
        Map<String, Object> map = playGameRequest(bgParameterModel);
        return bgResultModel.getPlayGameResult(map);
    }

    /**
     * 会员试玩游戏
     */
    private UnityResultModel tryPlayGame(BgParameterModel bgParameterModel) {
        Map<String, Object> map = tryPlayGameRequest(bgParameterModel);
        return bgResultModel.getPlayGameResult(map);
    }


    private Map<String, Object> createUserRequest(BgParameterModel bgParameterModel) {
        try {
            Map<String, Object> map = initParamMap(bgParameterModel);
            map.put(BgConstants.RequestParamMapConstants.DIGEST, bgParameterModel.getDigest());
            map.put(BgConstants.RequestParamMapConstants.NICKNAME, bgParameterModel.getNickname());
            map.put(BgConstants.RequestParamMapConstants.AGENTLOGINID, bgParameterModel.getAgentLoginId());
            log.info("\n" + "BG平台====>请求：" + "\n" + "请求URL: " + bgParameterModel.getUrl() + "\n" + "请求参数: " + map);
            return JSON.parseObject(okHttpService.postJson(okHttpService.proxyClient, bgParameterModel.getUrl(), map), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Map<String, Object> userLogOutRequest(BgParameterModel bgParameterModel) {
        try {
            return getStringObjectMap(bgParameterModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Map<String, Object> transferRequest(BgParameterModel bgParameterModel) {
        try {
            Map<String, Object> map = initParamMap(bgParameterModel);
            map.put(BgConstants.RequestParamMapConstants.DIGEST, bgParameterModel.getDigest());
            map.put(BgConstants.RequestParamMapConstants.AMOUNT, bgParameterModel.getAmount());
            map.put(BgConstants.RequestParamMapConstants.BIZID, bgParameterModel.getBizId());
            log.info("\n" + "BG平台====>请求：" + "\n" + "请求URL: " + bgParameterModel.getUrl() + "\n" + "请求参数: " + map);
            return JSON.parseObject(okHttpService.postJson(okHttpService.proxyClient, bgParameterModel.getUrl(), map), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Map<String, Object> queryBalanceRequest(BgParameterModel bgParameterModel) {
        try {
            return getStringObjectMap(bgParameterModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Map<String, Object> checkTransferRequest(BgParameterModel bgParameterModel) {
        try {
            Map<String, Object> map = initParamMap(bgParameterModel);
            map.put(BgConstants.RequestParamMapConstants.SIGN, bgParameterModel.getSign());
            map.put(BgConstants.RequestParamMapConstants.STARTTIME, bgDateFormat.format(bgParameterModel.getStartTime()));
            map.put(BgConstants.RequestParamMapConstants.ENDTIME, bgDateFormat.format(bgParameterModel.getEndTime()));
            map.put(BgConstants.RequestParamMapConstants.BIZID, bgParameterModel.getBizId());
            map.put(BgConstants.RequestParamMapConstants.PAGEINDEX, bgParameterModel.getPageIndex());
            map.put(BgConstants.RequestParamMapConstants.PAGESIZE, bgParameterModel.getPageSize());
            log.info("\n" + "BG平台====>请求：" + "\n" + "请求URL: " + bgParameterModel.getUrl() + "\n" + "请求参数: " + map);
            return JSON.parseObject(okHttpService.postJson(okHttpService.proxyClient, bgParameterModel.getUrl(), map), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Map<String, Object> playGameRequest(BgParameterModel bgParameterModel) {
        try {
            Map<String, Object> map = initParamMap(bgParameterModel);
            if(StringUtils.isEmpty(bgParameterModel.getGameType())){
                initVideoGameParamMap(map, bgParameterModel);
                return JSON.parseObject(okHttpService.postJson(okHttpService.proxyClient, bgParameterModel.getUrl(), map), Map.class);
            }
            switch (bgParameterModel.getGameType()) {
                case ApplicationConstants.GameTypeConstants.LIVE: {
                    initVideoGameParamMap(map, bgParameterModel);
                    break;
                }
                case ApplicationConstants.GameTypeConstants.HUNTER:
                case ApplicationConstants.GameTypeConstants.SLOT: {
                    map.put(BgConstants.RequestParamMapConstants.SIGN, bgParameterModel.getSign());
                    break;
                }
                default: {
                    initVideoGameParamMap(map, bgParameterModel);
                    break;
                }
            }
            return JSON.parseObject(okHttpService.postJson(okHttpService.proxyClient, bgParameterModel.getUrl(), map), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Map<String, Object> tryPlayGameRequest(BgParameterModel bgParameterModel) {
        try {
            Map<String, Object> map = initParamMap(bgParameterModel);
            map.put(BgConstants.RequestParamMapConstants.SIGN, bgParameterModel.getSign());
            return JSON.parseObject(okHttpService.postJson(okHttpService.proxyClient, bgParameterModel.getUrl(), map), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private BgParameterModel getBgParameterModel(TransferModel transfer) {
        String secretCode = HashUtil.sha1Base64(transfer.getTGmApi().getSecureCodes().get(BgConstants.AGENTPASSWORD));
        BgParameterModel bgParameterModel = initBgParameterModel(transfer);
        bgParameterModel.setMethod(transfer.getTGmApi().getSecureCodes().get(BgConstants.USERTRANSFER_METHOD));
        bgParameterModel.setDigest(MD5.getMD5(bgParameterModel.getRandom() + bgParameterModel.getSn() + bgParameterModel.getLoginId() + transfer.getAmount() + secretCode));
        bgParameterModel.setAmount(transfer.getAmount().toString());
        bgParameterModel.setBizId(Long.parseLong(transfer.getOrderNo()));
        return bgParameterModel;
    }

    /**
     * 初始化共同对象参数
     */
    private BgParameterModel initBgParameterModel(UnityParameterModel unityParameterModel) {
        String id = UUID.randomUUID().toString();
        BgParameterModel bgParameterModel = new BgParameterModel();
        bgParameterModel.setUrl(unityParameterModel.getTGmApi().getApiUrl());
        bgParameterModel.setId(id);
        bgParameterModel.setJsonrpc(unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.JSONPC));
        bgParameterModel.setRandom(id);
        bgParameterModel.setSn(unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.SN));
        bgParameterModel.setLoginId(unityParameterModel.getTGmApi().getPrefix() + unityParameterModel.getUserName());
        bgParameterModel.setIsMobileUrl(0);
        bgParameterModel.setIsHttpsUrl(0);
        return bgParameterModel;
    }

    /**
     * 试玩游戏初始化对象参数
     */
    private BgParameterModel initBgTryGameParameterModel(UnityParameterModel unityParameterModel) {
        String secretKey = unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.SECRETKEY);
        String id = UUID.randomUUID().toString();
        BgParameterModel bgParameterModel = new BgParameterModel();
        bgParameterModel.setUrl(unityParameterModel.getTGmApi().getApiUrl());
        bgParameterModel.setId(id);
        bgParameterModel.setJsonrpc(unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.JSONPC));
        bgParameterModel.setRandom(id);
        bgParameterModel.setSn(unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.SN));
        bgParameterModel.setSign(MD5.getMD5(bgParameterModel.getRandom() + bgParameterModel.getSn() + secretKey));
        return bgParameterModel;
    }

    private Map<String, Object> initParamMap(BgParameterModel bgParameterModel) {
        Map<String, Object> map = new HashMap<>();
        map.put(BgConstants.RequestParamMapConstants.ID, bgParameterModel.getId());
        map.put(BgConstants.RequestParamMapConstants.METHOD, bgParameterModel.getMethod());
        map.put(BgConstants.RequestParamMapConstants.JSONRPC, bgParameterModel.getJsonrpc());
        map.put(BgConstants.RequestParamMapConstants.RANDOM, bgParameterModel.getRandom());
        map.put(BgConstants.RequestParamMapConstants.SN, bgParameterModel.getSn());
        map.put(BgConstants.RequestParamMapConstants.LOGINID, bgParameterModel.getLoginId());
        return map;
    }


    private Date getCheckTransferStartTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//date 换成已经已知的Date对象
        cal.add(Calendar.HOUR_OF_DAY, -24);// before 24 hour
        return cal.getTime();
    }

    private void initVideoGameParamMap(Map<String, Object> map, BgParameterModel bgParameterModel) {
        map.put(BgConstants.RequestParamMapConstants.DIGEST, bgParameterModel.getDigest());
        map.put(BgConstants.RequestParamMapConstants.LOCALE, bgParameterModel.getLocale());
        map.put(BgConstants.RequestParamMapConstants.ISMOBILEURL, bgParameterModel.getIsMobileUrl());
        map.put(BgConstants.RequestParamMapConstants.ISHTTPSURL, bgParameterModel.getIsHttpsUrl());
    }

    private Map<String, Object> getStringObjectMap(BgParameterModel bgParameterModel) throws Exception {
        Map<String, Object> map = initParamMap(bgParameterModel);
        map.put(BgConstants.RequestParamMapConstants.DIGEST, bgParameterModel.getDigest());
        log.info("\n" + "BG平台====请求：" + "\n" + "请求URL: " + bgParameterModel.getUrl() + "\n" + "请求参数: " + map);
        return JSON.parseObject(okHttpService.postJson(okHttpService.proxyClient, bgParameterModel.getUrl(), map), Map.class);
    }

    private String getGameMethod(String gameType, UnityParameterModel unityParameterModel) {
        if(StringUtils.isEmpty(gameType)){
            return unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.VIDEOGAME_METHOD);
        }
        switch (gameType) {
            case ApplicationConstants.GameTypeConstants.LIVE:
                return unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.VIDEOGAME_METHOD);
            case ApplicationConstants.GameTypeConstants.HUNTER:
                return unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.FISHINGGAME_METHOD);
            case ApplicationConstants.GameTypeConstants.SLOT:
                return unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.EGAMEGAME_METHOD);
            default:
                return unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.VIDEOGAME_METHOD);
        }
    }

    private String getTryGameMethod(String gameType, UnityParameterModel unityParameterModel) {
        switch (gameType) {
            case ApplicationConstants.GameTypeConstants.LIVE:
                return unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.TRYVIDEOGAME_METHOD);
            case ApplicationConstants.GameTypeConstants.HUNTER:
                return unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.TRYFISHINGGAME_METHOD);
            case ApplicationConstants.GameTypeConstants.SLOT:
                return unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.TRYEGAMEGAME_METHOD);
            default:
                return unityParameterModel.getTGmApi().getSecureCodes().get(BgConstants.TRYVIDEOGAME_METHOD);
        }
    }

    /**
     * 设置显示终端，返回对应的终端连接
     */
    private void setOrigin(String origin, BgParameterModel bgParameterModel) {
        if (StringUtils.isEmpty(origin)) {
            bgParameterModel.setIsHttpsUrl(1);
            return ;
        }
        switch (origin) {
            case ApplicationConstants.ORIGIN_PC: {
                bgParameterModel.setIsHttpsUrl(1);
                break;
            }
            case ApplicationConstants.ORIGIN_APP: {
                bgParameterModel.setIsMobileUrl(1);
                break;
            }
            case ApplicationConstants.ORIGIN_H5: {
                bgParameterModel.setIsMobileUrl(1);
                break;
            }
        }

    }

}
