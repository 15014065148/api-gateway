package com.eveb.gateway.game.ea.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.eveb.gateway.game.ea.model.Helper;
import com.eveb.gateway.game.service.OkHttpService;
import com.eveb.gateway.utils.RequestParameter;
import com.eveb.gateway.utils.RsaUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class TsController {

    @Autowired
    public OkHttpService okHttpService;
    
    @Autowired
    private Helper authHelper;

    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQYumg0fERVdJ3Zzdbw4fyquXucZcaISMX8BxVPYoH3fn+fyZopikPnCLEgyiBGbt6ad/EkhJXMg2u8nffhunRqV7r3B+agqEDhs1n/2ihfATYeK6cyGJJsj256mY4loNNhVnV3pR0Ce9GatuCpHGEgRZBqmV4+0ydMhrgBeQbjQIDAQAB";
    
    public static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJBi6aDR8RFV0ndnN1vDh/Kq5e5xlxohIxfwHFU9igfd+f5/JmimKQ+cIsSDKIEZu3pp38SSElcyDa7yd9+G6dGpXuvcH5qCoQOGzWf/aKF8BNh4rpzIYkmyPbnqZjiWg02FWdXelHQJ70Zq24KkcYSBFkGqZXj7TJ0yGuAF5BuNAgMBAAECgYBXVPyq4VFGgvqCblP1lRctmGDrEJmsnE51tC3vAQH0MDd9z7qToa1EW9RLuGlPbRkQuek3JIMjaosMPmxzL09NKgUnUiO3gUs7UmWcFrPZcsmUjVccU84GQdlQ6w9F1GUOUNxOdaBfYZcC+VFZxr0wG3yMPQ2/oo5fTAutq2wGAQJBAPUZIlMMKduAnNgNqYAyMbECR6RMDzfunxBJZ3cifsJu9pjBJJar9y0tYoxKdYxaUj+wq1MqNVcnpU10kla3jl0CQQCWzwAzmjkvF1R1zuRJsCiOSLsmdPyS0C9plzQyrp1wz/gh5dUs8lLLW04+15sEabZ2OFScz7gXk6ynQJ2dpg7xAkEAvWWdu5UTp1Y8XUw5ZiHjekgruk84F12itwAyHsKeN3ttvV7K+k+KovOGSqijK0EEe8j2qz5bl6zANRlWmAIh1QJAXtyGu+7e2YhLUG97mf59BEMrtd0QoyjhKgI6i0bbMgMfifMQTN2AxvoAFfd5QyVb6LE2SA1BN1Nk3y/99RRngQJACkCjliOIrDkfPdmArPxwtNqt1XMUHCPLdBys9xGhPRS/RL1XbNhObj3OPQE0WyOCbiF7htcVYLOahh9RlXCxdw==";

    /**
     * 用户名密码登录
     * 签名字段（signature）根据username+timestamp变成字节，然后根据私钥进 行签名。
     *
     * @param request
     * @return String
     */
    @RequestMapping(value = {"/login"})
    @SuppressWarnings("unchecked")
    public String loginRequest(HttpServletRequest request) throws Exception {
        String jsonStr = httpProcess(request);
        Map maps = JSON.parseObject(jsonStr, Map.class);
        log.error("login所有的map的值====================" + JSON.toJSONString(maps));
        Map map = new HashMap<>();
        String cmd = maps.get("cmd").toString();
        log.error("cmd==============" + cmd);
        if("UserInfo".equals(cmd)) {
        	map.put("status", 200);
        	//返回的用户信息 此处暂时不用，不做处理
        } else if ("RegisterOrLoginReq".equals(cmd)){
        	Integer eventType = Integer.parseInt(maps.get("eventType").toString());
        	if (4 == eventType) {
                //登录
            	String accessToken = maps.get("accessToken").toString();
                log.error("accessToken===========" + accessToken);
                String signature = maps.get("signature").toString();
                log.error("signature==============" + signature);
                String timestamp = maps.get("timestamp").toString();
                log.error("timestamp==============" + timestamp);
                String channelId = maps.get("channelId").toString();
                log.error("channelId===========" + channelId);
                String ip = maps.get("ip").toString();
                log.error("ip==============" + ip);
                String username = authHelper.getUsername(accessToken);
                log.error("username================"+username);
                //String str = URLEncoder.encode(RsaUtil.sign((username + timestamp).getBytes(), privateKey), "UTF-8");
                boolean flag = RsaUtil.verify((timestamp+accessToken).getBytes(), publicKey, signature);
                log.error("flag===="+flag);
                if(flag) {
                	map.put("status", 200);
                    map.put("subChannelId", 0);
                    map.put("accessToken", accessToken);
                    map.put("username", username);
                } else {
                	map.put("status", 4026);
                }
        	}
        }
		return JSON.toJSONString(map);
    }

    
    public static void main(String[] args) {
    	String timestamp = "1533794586886";
    	String accessToken = "03c1621a6cb742f181711e68581ede21";
    	String signature = "MztgEIGSaZF+vEm/x9AaCCo67ZKUQQP7xok7uJiZ9jpuKWEOr+ESo9o+wlJ3n/YPyrgyjOIxWVcoTH1API6DgoMlXQxnQAxwuWq8N1PqbQisyNfxfgjG6vKcPvHGUm+WvZpmb9mCkeWMT3JPN9GanSETr2r/MucyWPt0yhkhIWc=";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIv6GqFOv3D8UMrJLvV/x4ikJjziQIjhtLssZ4b5NtINmDlyeoMtZgEG85TtXjk9MUCKiq6g3xBzm6+mr+h5H4F0b/jthfA+jngRiif8wBdaH6Dg8sUu0nUjCwkO9+8lAlHwEYup9H/99W5VFyin668Wl+8Hnnf0FqJS0hSTiWqQIDAQAB";
        try {
			boolean flag = RsaUtil.verify((timestamp+accessToken).getBytes(), publicKey, signature);
			System.out.println("==============="+flag+"==================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    
    /*
     *将post请求的json字符串格式的参数转为JSONObject
     */
    public static String httpProcess(HttpServletRequest request) throws IOException, JSONException {
        StringBuffer sb = new StringBuffer();
        InputStream is = request.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String s = "";
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        if (sb.toString().length() <= 0) {
            return null;
        } else {
            return sb.toString();
        }
    }

    /**
     * 通过token登录
     * 签名字段（signature）根据timestamp+accessToken变成字节，然后根据私钥 进行签名
     *
     * @param str
     * @return
     * @throws SignatureException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @RequestMapping(value = {"/tokenLogin"})
    public String tokenLoginRequest(@RequestBody String str) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException {
        log.error("***************tokenLogin************************" + str);
        Map maps = (Map) JSON.parse(str);
        Map returnMap = new HashMap<>();
        String signatureStr = maps.get("timestamp").toString() +
                maps.get("accessToken").toString();
        boolean flag = RsaUtil.verify(signatureStr.getBytes(), publicKey, maps.get("signature").toString());
        if (!flag) {
            returnMap.put("status", 4026);
            return JSON.toJSONString(returnMap);
        }
        /*log.error(maps.get("cmd").toString());
        log.error(maps.get("eventType").toString());
        log.error(maps.get("channelId").toString());
        log.error(maps.get("signature").toString());
        log.error(maps.get("timestamp").toString());
        log.error(maps.get("accessToken").toString());
        log.error(maps.get("ip").toString());*/
        /**
         * 状态码: 200 成功 410 无效的token 4026 验签失败 4027 IP无访问权限 505 渠道服务器在维护中
         */
        returnMap.put("status", 200);
        returnMap.put("subChannelId", 100);
        returnMap.put("username ", "test123");
        return JSON.toJSONString(returnMap);
    }


    /**
     * oAuthLogin登录
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = {"/oAuthLogin"})
    public void oAuthLogin(HttpServletRequest request) throws Exception {
        //log.error("***************oAuthLogin************************"+request);
        Map<String, String> paraMap = RequestParameter.getPara(request);
        log.error("所有的map的值====================" + JSON.toJSONString(paraMap));
        String username = paraMap.get("username");
        log.error("username===========" + username);
        String channelId = paraMap.get("channelId");
        log.error("channelId===========" + channelId);
        String subChannelId = paraMap.get("subChannelId");
        log.error("subChannelId===========" + subChannelId);
        String accessToken = paraMap.get("access_token");
        log.error("accessToken===========" + accessToken);
        String timestamp = paraMap.get("timestamp");
        username = "admin";
        channelId = "1";
        subChannelId = "0";
        timestamp = System.currentTimeMillis() + "";
        log.error("timestamp===========" + timestamp);
		/*accessToken = RsaUtil.decryptByPrivateKey(accessToken.getBytes(), publicKey).toString();
		accessToken = RsaUtil.encryptByPrivateKey(accessToken.getBytes(), privateKey).toString();*/
        String str = URLEncoder.encode(RsaUtil.sign((username + channelId + accessToken + timestamp).getBytes(), privateKey), "UTF-8");
        //调用艺博会--

        //回调后台
        String url = new String("http://168.63.153.54:6677/api/callback?username=" + username + "&channelId=" + channelId + "&subChannelId=" + subChannelId
                + "&timestamp=" + timestamp + "&accessToken=" + accessToken + "&signature=" + str);
        log.error("url==========================================================" + url);
        okHttpService.post(okHttpService.client, url, "");
        /*Integer accessToken = 123456789;

		String str="admin"+"1"+"123456789"+"1532656197242";
        str=URLEncoder.encode(RsaUtil.sign(str.getBytes(),privateKey), "UTF-8");*/

        //String strings=(username+channelId+accessToken+System.currentTimeMillis());
        //String signature= URLEncoder.encode(RsaUtil.sign(strings.getBytes(), privateKey), "UTF-8");

        /*returnMap.put("status",200);
        api.setStatus(200);*/
        //return okHttpService.postJson(okHttpService.initHeadClient(initHeand(api)), api.getPcUrl(), api);
    }


    /**
     * 查询用户信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/queryUserInfo"})
    public String queryUserInfo(HttpServletRequest request) throws Exception {
        //log.error("***************oAuthLogin************************"+request);
        Map<String, String> paraMap = RequestParameter.getPara(request);
        log.error("queryUserInfo所有的map的值====================" + JSON.toJSONString(paraMap));
        String username = paraMap.get("username");
        log.error("username===========" + username);
        Integer channelId = Integer.parseInt(paraMap.get("channelId"));
        log.error("channelId===========" + channelId);
        String timestamp = System.currentTimeMillis() + "";
        log.error("timestamp===========" + timestamp);
        String str = URLEncoder.encode(RsaUtil.sign((username + timestamp).getBytes(), privateKey), "UTF-8");
        //调用艺博会--

        //回调后台
        String url = new String("http://168.63.153.54:6677/api/userinfo?username=" + username + "&channelId=" + channelId
                + "&timestamp=" + timestamp + "&signature=" + str);
        log.error("请求url==========================================================" + url);
        String resultStr = okHttpService.post(okHttpService.client, url, "");
        log.error("请求返回str=========================" + resultStr);
        /*Map maps = JSON.parseObject(resultStr, Map.class);

        //验签
        boolean flag = RsaUtil.verify((username+maps.get("timestamp")).getBytes(), publicKey, maps.get("signature").toString());
        log.error("是否验证签证成功================="+flag);
        if(!flag) {
        	maps.put("status",4026);
        	return JSON.toJSONString(maps);
        }
        maps.put("status", 200);
        return JSON.toJSONString(maps);*/
        return resultStr;
    }

    /**
     * 同步用户(创建用户)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/createUserInfo"})
    public String createUserInfo(HttpServletRequest request) throws Exception {
        //log.error("***************oAuthLogin************************"+request);
        Map<String, String> paraMap = RequestParameter.getPara(request);
        log.error("createUserInfo所有的map的值====================" + JSON.toJSONString(paraMap));
        String username = paraMap.get("username");
        log.error("username===========" + username);
        Integer channelId = Integer.parseInt(paraMap.get("channelId"));
        log.error("channelId===========" + channelId);
        Integer subChannelId = Integer.parseInt(paraMap.get("subChannelId"));
        log.error("subChannelId===========" + subChannelId);
        String lang = paraMap.get("lang");
        log.error("lang===========" + lang);

        String str = URLEncoder.encode(RsaUtil.sign(username.getBytes(), privateKey), "UTF-8");

        //回调后台
        String url = new String("http://168.63.153.54:6677/api/syncuser?username=" + username + "&channelId=" + channelId
                + "&subChannelId=" + subChannelId + "&signature=" + str);
        log.error("请求url==========================================================" + url);
        String resultStr = okHttpService.post(okHttpService.client, url, "");
        log.error("请求返回str=========================" + resultStr);
        return JSON.toJSONString(resultStr);
    }

    /**
     * 用户充值
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/recharge"})
    public String recharge(HttpServletRequest request) throws Exception {
        //log.error("***************oAuthLogin************************"+request);
        Map<String, String> paraMap = RequestParameter.getPara(request);
        log.error("recharge所有的map的值====================" + JSON.toJSONString(paraMap));
        String username = paraMap.get("username");
        log.error("username===========" + username);
        Double money = Double.parseDouble(paraMap.get("money"));   //充值金额，可以负数，超过两位四舍五入
        log.error("money================" + money);
        Integer channelId = Integer.parseInt(paraMap.get("channelId"));
        log.error("channelId===========" + channelId);
        Integer rechargeReqId = Integer.parseInt(paraMap.get("rechargeReqId"));  //充值请求id
        log.error("rechargeReqId===========" + rechargeReqId);
        String timestamp = System.currentTimeMillis() + "";
        log.error("timestamp===========" + timestamp);
        String str = URLEncoder.encode(RsaUtil.sign((username + timestamp).getBytes(), privateKey), "UTF-8");

        //回调后台
        String url = new String("http://168.63.153.54:6677/api/recharge?username=" + username + "&money=" + money + "&channelId=" + channelId
                + "&rechargeReqId=" + rechargeReqId + "&timestamp=" + timestamp + "&signature=" + str);
        log.error("请求url==========================================================" + url);
        String resultStr = okHttpService.post(okHttpService.client, url, "");
        log.error("请求返回str=========================" + resultStr);
        return resultStr;
    }


    /**
     * 查询充值状态
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/rechargeStatus"})
    public String rechargeStatus(HttpServletRequest request) throws Exception {
        //log.error("***************oAuthLogin************************"+request);
        Map<String, String> paraMap = RequestParameter.getPara(request);
        log.error("rechargeStatus所有的map的值====================" + JSON.toJSONString(paraMap));
        Integer channelId = Integer.parseInt(paraMap.get("channelId"));
        log.error("channelId===========" + channelId);
        Integer rechargeReqId = Integer.parseInt(paraMap.get("rechargeReqId"));  //充值请求id
        log.error("rechargeReqId===========" + rechargeReqId);
        String str = URLEncoder.encode(RsaUtil.sign((rechargeReqId.toString()).getBytes(), privateKey), "UTF-8");

        //回调后台
        String url = new String("http://168.63.153.54:6677/api/rechargestatus?channelId=" + channelId
                + "&rechargeReqId=" + rechargeReqId + "&signature=" + str);
        log.error("请求url==========================================================" + url);
        String resultStr = okHttpService.post(okHttpService.client, url, "");
        log.error("请求返回str=========================" + resultStr);
        return resultStr;
    }


    /**
     * 获取用户余额
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/getUserMoney"})
    public String getUserMoney(HttpServletRequest request) throws Exception {
        //log.error("***************oAuthLogin************************"+request);
        Map<String, String> paraMap = RequestParameter.getPara(request);
        log.error("getUserMoney所有的map的值====================" + JSON.toJSONString(paraMap));
        String username = paraMap.get("username");
        log.error("username===========" + username);
        Integer channelId = Integer.parseInt(paraMap.get("channelId"));
        log.error("channelId===========" + channelId);
        String str = URLEncoder.encode(RsaUtil.sign((username).getBytes(), privateKey), "UTF-8");

        //回调后台
        String url = new String("http://168.63.153.54:6677/api/getusermoney?username=" + username + "&channelId=" + channelId
                + "&signature=" + str);
        log.error("请求url==========================================================" + url);
        String resultStr = okHttpService.post(okHttpService.client, url, "");
        log.error("请求返回str=========================" + resultStr);
        return resultStr;
    }


    /**
     * 获取下注限制
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/getBetLimit"})
    public String getBetLimit(HttpServletRequest request) throws Exception {
        //log.error("***************oAuthLogin************************"+request);
        Map<String, String> paraMap = RequestParameter.getPara(request);
        log.error("getBetLimit所有的map的值====================" + JSON.toJSONString(paraMap));
        String username = paraMap.get("username");
        log.error("username===========" + username);
        Integer channelId = Integer.parseInt(paraMap.get("channelId"));
        log.error("channelId===========" + channelId);
        String str = URLEncoder.encode(RsaUtil.sign((username).getBytes(), privateKey), "UTF-8");

        //回调后台
        String url = new String("http://168.63.153.54:6677/api/getbetlimit?username=" + username + "&channelId=" + channelId
                + "&signature=" + str);
        log.error("请求url==========================================================" + url);
        String resultStr = okHttpService.post(okHttpService.client, url, "");
        log.error("请求返回str=========================" + resultStr);
        return resultStr;
    }


    /**
     * 修改下注限制
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/updateBetLimit"})
    public String updateBetLimit(HttpServletRequest request) throws Exception {
        //log.error("***************oAuthLogin************************"+request);
        Map<String, String> paraMap = RequestParameter.getPara(request);
        log.error("updateBetLimit所有的map的值====================" + JSON.toJSONString(paraMap));
        String username = paraMap.get("username");
        log.error("username===========" + username);
        Integer channelId = Integer.parseInt(paraMap.get("channelId"));
        log.error("channelId===========" + channelId);
        String gameId = paraMap.get("gameId");   //房间id
        log.error("gameId===========" + gameId);
        String str = URLEncoder.encode(RsaUtil.sign((username).getBytes(), privateKey), "UTF-8");

        Integer bankerMin = Integer.parseInt(paraMap.get("bankerMin"));
        log.error("bankerMin===========" + bankerMin);
        Integer bankerMax = Integer.parseInt(paraMap.get("bankerMax"));
        log.error("bankerMax===========" + bankerMax);
        Integer bankerPairMin = Integer.parseInt(paraMap.get("bankerPairMin"));
        log.error("bankerPairMin===========" + bankerPairMin);
        Integer bankerPairMax = Integer.parseInt(paraMap.get("bankerPairMax"));
        log.error("bankerPairMax===========" + bankerPairMax);
        Integer playerMin = Integer.parseInt(paraMap.get("playerMin"));
        log.error("playerMin===========" + playerMin);
        Integer playerMax = Integer.parseInt(paraMap.get("playerMax"));
        log.error("playerMax===========" + playerMax);
        Integer playerPairMin = Integer.parseInt(paraMap.get("playerPairMin"));
        log.error("playerPairMin===========" + playerPairMin);
        Integer tieMin = Integer.parseInt(paraMap.get("tieMin"));
        log.error("tieMin===========" + tieMin);
        Integer tieMax = Integer.parseInt(paraMap.get("tieMax"));
        log.error("tieMax===========" + tieMax);

        //回调后台
        String url = new String("http://168.63.153.54:6677/api/updatebetlimit?username=" + username + "&channelId=" + channelId
                + "&gameId=" + gameId + "&signature=" + str + "&bankerMin=" + bankerMin + "&bankerMax=" + bankerMax
                + "&bankerPairMin="+bankerPairMin + "&bankerPairMax=" + bankerPairMax + "&playerMin=" + playerMin
                + "&playerMax=" + playerMax + "&playerPairMin=" + playerPairMin + "&tieMin="+tieMin+"&tieMax="+tieMax);
        log.error("请求url==========================================================" + url);
        String resultStr = okHttpService.post(okHttpService.client, url, "");
        log.error("请求返回str=========================" + resultStr);
        return resultStr;
    }


    /**
     * 获取用户的额度记录
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/getUserTransaction"})
    public String getUserTransaction(HttpServletRequest request) throws Exception {
        //log.error("***************oAuthLogin************************"+request);
        Map<String, String> paraMap = RequestParameter.getPara(request);
        log.error("getUserTransaction所有的map的值====================" + JSON.toJSONString(paraMap));
        String username = paraMap.get("username");
        log.error("username===========" + username);
        String startTimeStr = paraMap.get("startTimeStr");
        log.error("startTimeStr===========" + startTimeStr);
        String endTimeStr = paraMap.get("endTimeStr");
        log.error("endTimeStr===========" + endTimeStr);
        Integer channelId = Integer.parseInt(paraMap.get("channelId"));
        log.error("channelId===========" + channelId);
        Integer subChannelId = Integer.parseInt(paraMap.get("subChannelId"));
        log.error("subChannelId===========" + subChannelId);
        Integer pageNum = Integer.parseInt(paraMap.get("pageNum"));
        log.error("pageNum===========" + pageNum);
        Integer pageSize = Integer.parseInt(paraMap.get("pageSize"));
        log.error("pageSize===========" + pageSize);
        String timestamp = System.currentTimeMillis() + "";
        log.error("timestamp===========" + timestamp);
        String str = URLEncoder.encode(RsaUtil.sign((username + timestamp).getBytes(), privateKey), "UTF-8");

        //回调后台
        String url = new String("http://168.63.153.54:6677/api/usertransaction?username=" + username + "&startTimeStr=" + startTimeStr + "&endTimeStr=" + endTimeStr
                + "&channelId=" + channelId + "&subChannelId=" + subChannelId + "&pageNum=" + pageNum + "&pageSize=" + pageSize
                + "&timestamp=" + timestamp + "&signature=" + str);
        log.error("请求url==========================================================" + url);
        String resultStr = okHttpService.post(okHttpService.client, url, "");
        log.error("请求返回str=========================" + resultStr);
        return resultStr;
    }
}
