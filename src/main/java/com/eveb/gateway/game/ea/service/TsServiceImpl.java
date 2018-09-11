package com.eveb.gateway.game.ea.service;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.ea.model.Helper;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.game.service.SysService;
import com.eveb.gateway.game.unity.model.LoginModel;
import com.eveb.gateway.game.unity.model.PlayGameModel;
import com.eveb.gateway.game.unity.model.RegisterModel;
import com.eveb.gateway.game.unity.model.TransferModel;
import com.eveb.gateway.game.unity.model.UnityResultModel;
import com.eveb.gateway.game.unity.service.DepotService;
import com.eveb.gateway.utils.RsaUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TsServiceImpl implements DepotService {

    @Autowired
    public OkHttpService okHttpService;
    
    @Autowired
    private SysService sysService;
    
    @Autowired
    private Helper authHelper;
    
    private static final String DEPOT_NAME = PlatFromEnum.ENUM_TS.getKey();

    @Override
    public Object tryPlayGame(PlayGameModel playGame) {
    	TGmApi api = sysService.getApiBySiteCode(playGame.getSiteCode(), DEPOT_NAME);
        StringBuffer buffer = new StringBuffer();
        buffer.append(api.getPcUrl2());
        buffer.append("?t=");
        buffer.append(System.currentTimeMillis());
        buffer.append("&");
        buffer.append("username=guest");
        buffer.append("&accessToken=");
        String uuid = UUID.randomUUID().toString();
        buffer.append(uuid.replaceAll("-", ""));
        buffer.append("&mode=no");
        
        String key = api.getPrefix()+playGame.getUserName();
        authHelper.putToken(key, uuid.replaceAll("-", ""));
        
        UnityResultModel resultModel = new UnityResultModel();
        resultModel.setCode(Boolean.TRUE);
        resultModel.setMessage(buffer.toString());
        log.debug("buffer===="+buffer.toString());
        return resultModel;
    }

    @Override
    public Object playGame(PlayGameModel playGame) {
    	TGmApi api = sysService.getApiBySiteCode(playGame.getSiteCode(), DEPOT_NAME);
        StringBuffer buffer = new StringBuffer();
        buffer.append(api.getPcUrl2());
        buffer.append("?t=");
        buffer.append(System.currentTimeMillis());
        buffer.append("&username=");
        buffer.append(api.getPrefix()+playGame.getUserName());
        buffer.append("&accessToken=");
        String uuid = UUID.randomUUID().toString();
        buffer.append(uuid.replaceAll("-", ""));
        buffer.append("&mode=no");
        
        String key = api.getPrefix()+playGame.getUserName();
        authHelper.putToken(key, uuid.replaceAll("-", ""));
        
        UnityResultModel resultModel = new UnityResultModel();
        resultModel.setCode(Boolean.TRUE);
        resultModel.setMessage(buffer.toString());
        log.debug("buffer===="+buffer.toString());
        return resultModel;
    }

    @Override
    public Object openHall(PlayGameModel playGame) {
        return null;
    }

    @Override
    public Object createPlayer(RegisterModel register) {
        TGmApi api = sysService.getApiBySiteCode(register.getSiteCode(), DEPOT_NAME);
        UnityResultModel resultModel = new UnityResultModel();
        resultModel.setCode(Boolean.TRUE);
        resultModel.setMessage("创建用户成功");
        return resultModel;
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
    	TGmApi api = sysService.getApiBySiteCode(transfer.getSiteCode(), DEPOT_NAME);
    	Map maps = (Map) JSON.parse(api.getSecureCode());
    	String privateKey = maps.get("PrivateKey").toString();
    	UnityResultModel resultModel = new UnityResultModel();
    	String userName = api.getPrefix()+transfer.getUserName();
    	
    	String timestamp = System.currentTimeMillis() + "";
        log.error("timestamp===========" + timestamp);
        resultModel.setCode(Boolean.FALSE);
    	String str;
		try {
			str = URLEncoder.encode(RsaUtil.sign((userName+timestamp).getBytes(), privateKey), "UTF-8");
		} catch (Exception e) {
			log.error("平台充值加签失败");
			resultModel.setMessage("平台充值加签失败");
			return resultModel;
		}
    	//拼接请求url
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(api.getPcUrl());
    	buffer.append("recharge?username=");
    	buffer.append(userName);
    	buffer.append("&money=");
    	buffer.append(transfer.getAmount()+"00");
    	buffer.append("&channelId=");
    	buffer.append(api.getAgyAcc());
    	buffer.append("&timestamp=");
    	buffer.append(timestamp);
    	buffer.append("&rechargeReqId=");
    	buffer.append(transfer.getOrderNo()); //充值请求ID，系统用于校验是否 是重复的充值请求，格式自定
    	buffer.append("&signature=");
    	buffer.append(str);
    	
    	log.error("平台充值请求url==========================================================" + buffer.toString());
    	String resultStr;
        try {
			resultStr = okHttpService.post(okHttpService.client, buffer.toString(), "");
		} catch (Exception e) {
			log.error("平台充值请求异常");
			resultModel.setMessage("平台充值请求异常");
			return resultModel;
		}
        log.error("请求返回数据============"+resultStr);
        Map resultMaps = (Map) JSON.parse(resultStr);
        Integer status = Integer.parseInt(resultMaps.get("status").toString());
        
        if(200==status) {
        	resultModel.setCode(Boolean.TRUE);
        	String money = resultMaps.get("money").toString();
        	resultModel.setMessage(Double.parseDouble(money)/100);
        } else if(201==status) {
        	resultModel.setMessage("重复的充值请求id");
        } else if(202==status) {
        	resultModel.setMessage("渠道不存在");
        } else if(203==status) {
        	resultModel.setMessage("充值id不能为空");
        } else if(4003==status) {
        	resultModel.setMessage("系统繁忙稍后再试");
        } else if(4010==status) {
        	resultModel.setMessage("已参加比赛不能充值");
        } else if(4019==status) {
        	resultModel.setMessage("用户余额不足");
        } else if(4026==status) {
        	resultModel.setMessage("验签失败");
        } else if(4027==status) {
        	resultModel.setMessage("IP无访问权限");
        } else if(4027==status) {
        	resultModel.setMessage("不能给游客充值");
        } else if(4037==status) {
        	resultModel.setMessage("用户不存在");
        }
        return resultModel;
    }

    @Override
    public Object withdrawal(TransferModel transfer) {
    	TGmApi api = sysService.getApiBySiteCode(transfer.getSiteCode(), DEPOT_NAME);
    	Map maps = (Map) JSON.parse(api.getSecureCode());
    	String privateKey = maps.get("PrivateKey").toString();
    	UnityResultModel resultModel = new UnityResultModel();
    	String userName = api.getPrefix()+transfer.getUserName();
    	
    	String timestamp = System.currentTimeMillis() + "";
        log.error("timestamp===========" + timestamp);
        resultModel.setCode(Boolean.FALSE);
    	String str;
		try {
			str = URLEncoder.encode(RsaUtil.sign((userName+timestamp).getBytes(), privateKey), "UTF-8");
		} catch (Exception e) {
			log.error("平台提款加签失败");
			resultModel.setMessage("平台提款加签失败");
			return resultModel;
		}
    	//拼接请求url
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(api.getPcUrl());
    	buffer.append("recharge?username=");
    	buffer.append(userName);
    	buffer.append("&money=-");
    	buffer.append(transfer.getAmount()+"00");
    	buffer.append("&channelId=");
    	buffer.append(api.getAgyAcc());
    	buffer.append("&timestamp=");
    	buffer.append(timestamp);
    	buffer.append("&rechargeReqId=");
    	buffer.append(transfer.getOrderNo()); //充值请求ID，系统用于校验是否 是重复的充值请求，格式自定
    	buffer.append("&signature=");
    	buffer.append(str);
    	
    	log.error("平台提款请求url==========================================================" + buffer.toString());
    	String resultStr;
        try {
			resultStr = okHttpService.post(okHttpService.client, buffer.toString(), "");
		} catch (Exception e) {
			log.error("平台提款请求异常");
			resultModel.setMessage("平台提款请求异常");
			return resultModel;
		}
        log.error("请求返回数据============"+resultStr);
        Map resultMaps = (Map) JSON.parse(resultStr);
        Integer status = Integer.parseInt(resultMaps.get("status").toString());
        
        if(200==status) {
        	resultModel.setCode(Boolean.TRUE);
        	String money = resultMaps.get("money").toString();
        	resultModel.setMessage(Double.parseDouble(money)/100);
        } else if(201==status) {
        	resultModel.setMessage("重复的充值请求id");
        } else if(202==status) {
        	resultModel.setMessage("渠道不存在");
        } else if(203==status) {
        	resultModel.setMessage("充值id不能为空");
        } else if(4003==status) {
        	resultModel.setMessage("系统繁忙稍后再试");
        } else if(4010==status) {
        	resultModel.setMessage("已参加比赛不能充值");
        } else if(4019==status) {
        	resultModel.setMessage("用户余额不足");
        } else if(4026==status) {
        	resultModel.setMessage("验签失败");
        } else if(4027==status) {
        	resultModel.setMessage("IP无访问权限");
        } else if(4027==status) {
        	resultModel.setMessage("不能给游客充值");
        } else if(4037==status) {
        	resultModel.setMessage("用户不存在");
        }
        return resultModel;
    }

    @Override
    public Object queryBalance(LoginModel login) {
    	TGmApi api = sysService.getApiBySiteCode(login.getSiteCode(), DEPOT_NAME);
    	Map maps = (Map) JSON.parse(api.getSecureCode());
    	String privateKey = maps.get("PrivateKey").toString();
    	UnityResultModel resultModel = new UnityResultModel();
    	String userName = api.getPrefix()+login.getUserName();
    	
    	resultModel.setCode(Boolean.FALSE);
    	String str;
		try {
			str = URLEncoder.encode(RsaUtil.sign((userName).getBytes(), privateKey), "UTF-8");
		} catch (Exception e) {
			log.error("获取用户余额加签失败");
			resultModel.setMessage("获取用户余额加签失败");
			return resultModel;
		}
    	//拼接请求url
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(api.getPcUrl());
    	buffer.append("getusermoney?username=");
    	buffer.append(userName);
    	buffer.append("&channelId=");
    	buffer.append(api.getAgyAcc());  //channelId
    	buffer.append("&signature=");
    	buffer.append(str);
    	
    	log.error("获取用户余额请求url==========================================================" + buffer.toString());
    	String resultStr;
        try {
			resultStr = okHttpService.post(okHttpService.client, buffer.toString(), "");
		} catch (Exception e) {
			log.error("获取用户余额请求异常");
			resultModel.setMessage("获取用户余额请求异常");
			return resultModel;
		}
        log.error("请求返回数据============"+resultStr);
        Map resultMaps = (Map) JSON.parse(resultStr);
        Integer status = Integer.parseInt(resultMaps.get("status").toString());
        resultModel.setCode(Boolean.FALSE);
        if(200==status) {
        	resultModel.setCode(Boolean.TRUE);
        	JSONArray jsonArray = new JSONArray((List<Object>) resultMaps.get("results"));
        	String money = jsonArray.getJSONObject(0).getString("money");
        	resultModel.setMessage(Double.parseDouble(money)/100);
        } else if(202==status) {
        	resultModel.setMessage("渠道不存在");
        } else if(4026==status) {
        	resultModel.setMessage("验签失败");
        } else if(4027==status) {
        	resultModel.setMessage("IP无权限访问");
        }
        return resultModel;
    }
    
    @Override
    public Object checkTransfer(TransferModel transfer) {
    	TGmApi api = sysService.getApiBySiteCode(transfer.getSiteCode(), DEPOT_NAME);
    	Map maps = (Map) JSON.parse(api.getSecureCode());
    	String privateKey = maps.get("PrivateKey").toString();
    	UnityResultModel resultModel = new UnityResultModel();
    	resultModel.setCode(Boolean.FALSE);
    	String str;
		try {
			str = URLEncoder.encode(RsaUtil.sign((transfer.getOrderNo()).getBytes(), privateKey), "UTF-8");
		} catch (Exception e) {
			log.error("查询充值状态加签失败");
			resultModel.setMessage("查询充值状态加签失败");
			return resultModel;
		}
    	//拼接请求url
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(api.getPcUrl());
    	buffer.append("rechargestatus?channelId=");
    	buffer.append(api.getAgyAcc());  //channelId
    	buffer.append("&rechargeReqId=");
    	buffer.append(transfer.getOrderNo());
    	buffer.append("&signature=");
    	buffer.append(str);
    	
    	log.error("查询充值状态请求url==========================================================" + buffer.toString());
    	String resultStr;
        try {
			resultStr = okHttpService.post(okHttpService.client, buffer.toString(), "");
		} catch (Exception e) {
			log.error("查询充值状态请求异常");
			resultModel.setMessage("查询充值状态请求异常");
			return resultModel;
		}
        log.error("查询充值状态请求返回数据============"+resultStr);
        Map resultMaps = (Map) JSON.parse(resultStr);
        Integer status = Integer.parseInt(resultMaps.get("status").toString());
        if(200==status) {
        	resultModel.setCode(Boolean.TRUE);
        	resultModel.setMessage(resultMaps.get("rechargeReqId").toString());
        } else if(-1==status) {
        	resultModel.setMessage("流水号不存在");
        } else if(0==status) {
        	resultModel.setMessage("充值中");
        } else if(2==status) {
        	resultModel.setMessage("充值失败");
        } else if(202==status) {
        	resultModel.setMessage("渠道不存在");
        } else if(4026==status) {
        	resultModel.setMessage("验签失败");
        } else if(4027==status) {
        	resultModel.setMessage("IP无访问权限");
        }
        return resultModel;
    }


    @Override
    public Object getToken() {
        return null;
    }
}
