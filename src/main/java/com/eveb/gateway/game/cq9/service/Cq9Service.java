package com.eveb.gateway.game.cq9.service;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.cq9.constants.Cq9Constants;
import com.eveb.gateway.game.cq9.model.*;
import com.eveb.gateway.game.cq9.mapper.MbrTransferRecordMapper;
import com.eveb.gateway.game.service.SysService;
import com.eveb.gateway.utils.DateFormat;
import com.eveb.gateway.utils.JwtUtils;
import com.eveb.gateway.utils.RequestParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class Cq9Service {

    @Autowired
    private MbrTransferRecordMapper recordMapper;
    @Autowired
    private SysService sysService;
    @Autowired
    private JwtUtils jwtUtils;

    public static Map<String, BigDecimal> accountMap = new HashMap<String, BigDecimal>() {{
        put("testybh", new BigDecimal(1000));
        put("testybh1", new BigDecimal(1000));
        put("testybh2", new BigDecimal(1000));
        put("testybh3", new BigDecimal(1000));
    }};

    private static final String MAPKEY_CODE = "CODE";
    private static final String MAPKEY_BALANCE = "BALANCE";

    /***
     * 转出
     * @return
     */
    public RsModel transferOut(HttpServletRequest request) {
        log.info("transferOut：" + request.getRequestURI());
        BetRqModel bet = new BetRqModel();
        MbrTransferRecordModel record = new MbrTransferRecordModel();
        MbrTransferRecordModel.Data rdata = new MbrTransferRecordModel.Data();
        String methon = request.getRequestURI();
        methon = methon.substring(methon.lastIndexOf("/") + 1);

        RsModel rs = new RsModel();
        RsModel.Status status = new RsModel.Status();
        RsModel.Data data = null;
        Map paramap = RequestParameter.getPara(request);
        log.info("Paramer:" + JSON.toJSONString(paramap));
        Map rsmap = checkTransferOut(paramap);
        Cq9Constants.Code code = (Cq9Constants.Code) rsmap.get(MAPKEY_CODE);
        log.info("Valid:" + code.getValue());
        BigDecimal balance = (BigDecimal) rsmap.get(MAPKEY_BALANCE);
        rdata.setBefore(balance);
        try {
            if (Cq9Constants.Code.CODE_0.equals(code)) {
                bet = JSON.parseObject(JSON.toJSONString(paramap), BetRqModel.class);
                balance = balance.subtract(bet.getAmount());
                /**成功，写入日志*/
                rdata.setAction(methon);
                rdata.setTarget(new MbrTransferRecordModel.Data.Target(bet.getAccount()));
                rdata.setStatus(new MbrTransferRecordModel.Data.Status(DateFormat.ConventDate(new Date()), DateFormat.ConventDate(new Date()), "success"));
                rdata.setBalance(balance);
                rdata.setCurrency("CNY");
                rdata.setEvent(new MbrTransferRecordModel.Data.Event(bet.getMtcode(), bet.getAmount(), DateFormat.ConventDate(new Date())));
                record.setData(rdata);
                record.setStatus(new MbrTransferRecordModel.Status(status.getCode(), status.getMessage(), DateFormat.ConventDate(new Date())));
                insertTransferRecord(record);
                accountMap.put(bet.getAccount(), balance);
                data=new RsModel.Data();
                data.setBalance(balance);
                data.setCurrency("CNY");
            }
        } catch (Exception e) {
            code = Cq9Constants.Code.CODE_1003;
        }
        status.setCode(code.getKey());
        status.setMessage(code.getValue());
        status.setDatetime(DateFormat.ConventDate_RFC3339(new Date()));
        rs.setData(data);
        rs.setStatus(status);
        return rs;
    }

    /***
     * 转入
     * @return
     */
    public RsModel transferIn(HttpServletRequest request) {
        log.info("转入" + request.getRequestURI());
        BetRqModel bet = new BetRqModel();
        MbrTransferRecordModel record = new MbrTransferRecordModel();
        MbrTransferRecordModel.Data rdata = new MbrTransferRecordModel.Data();
        String methon = request.getRequestURI();
        methon = methon.substring(methon.lastIndexOf("/") + 1);

        RsModel rs = new RsModel();
        RsModel.Status status = new RsModel.Status();
        RsModel.Data data = new RsModel.Data();
        Map paramap = RequestParameter.getPara(request);
        Map rsmap = checkTransferIn(paramap);
        Cq9Constants.Code code = (Cq9Constants.Code) rsmap.get(MAPKEY_CODE);
        log.info("Valid:" + code.getValue());
        BigDecimal balance = (BigDecimal) rsmap.get(MAPKEY_BALANCE);
        rdata.setBefore(balance);
        try {
            if (Cq9Constants.Code.CODE_0.equals(code)) {
                bet = JSON.parseObject(JSON.toJSONString(paramap), BetRqModel.class);
                balance = balance.add(bet.getAmount());
                /**成功，写入日志*/
                rdata.setAction(methon);
                rdata.setTarget(new MbrTransferRecordModel.Data.Target(bet.getAccount()));
                rdata.setStatus(new MbrTransferRecordModel.Data.Status(DateFormat.ConventDate(new Date()), DateFormat.ConventDate(new Date()), "success"));
                rdata.setBalance(balance);
                rdata.setCurrency("CNY");
                rdata.setEvent(new MbrTransferRecordModel.Data.Event(bet.getMtcode(), bet.getAmount(), DateFormat.ConventDate(new Date())));
                record.setData(rdata);
                record.setStatus(new MbrTransferRecordModel.Status(status.getCode(), status.getMessage(), DateFormat.ConventDate(new Date())));
                insertTransferRecord(record);
                accountMap.put(bet.getAccount(), balance);
                data.setBalance(balance);
                data.setCurrency("CNY");
            }
        } catch (Exception e) {
            code = Cq9Constants.Code.CODE_1003;
        }
        status.setCode(code.getKey());
        status.setMessage(code.getValue());
        status.setDatetime(DateFormat.ConventDate_RFC3339(new Date()));
        rs.setData(data);
        rs.setStatus(status);
        return rs;
    }

    public RsModel takeAll(HttpServletRequest request) {
        log.info("takeAll：" + request.getRequestURI());
        BetRqModel bet = new BetRqModel();
        MbrTransferRecordModel record = new MbrTransferRecordModel();
        MbrTransferRecordModel.Data rdata = new MbrTransferRecordModel.Data();
        String methon = request.getRequestURI();
        methon = methon.substring(methon.lastIndexOf("/") + 1);
        RsModel rs = new RsModel();
        RsModel.Status status = new RsModel.Status();
        RsModel.Data data =null;
        Map paramap = RequestParameter.getPara(request);
        Cq9Constants.Code code = checkTransferOutAll(paramap);
        try {
            if (Cq9Constants.Code.CODE_0.equals(code)) {
                bet = JSON.parseObject(JSON.toJSONString(paramap), BetRqModel.class);
                rdata.setBefore(accountMap.get(bet.getAccount()));
                /**成功，写入日志*/
                rdata.setAction(methon);
                rdata.setTarget(new MbrTransferRecordModel.Data.Target(bet.getAccount()));
                rdata.setStatus(new MbrTransferRecordModel.Data.Status(DateFormat.ConventDate(new Date()), DateFormat.ConventDate(new Date()), "success"));
                rdata.setBalance(BigDecimal.ZERO);
                rdata.setCurrency("CNY");
                rdata.setEvent(new MbrTransferRecordModel.Data.Event(bet.getMtcode(), bet.getAmount(), DateFormat.ConventDate(new Date())));
                record.setData(rdata);
                record.setStatus(new MbrTransferRecordModel.Status(status.getCode(), status.getMessage(), DateFormat.ConventDate(new Date())));
                insertTransferRecord(record);
                accountMap.put(bet.getAccount(), BigDecimal.ZERO);
                data = new RsModel.Data();
                data.setBalance(BigDecimal.ZERO);
                data.setCurrency("CNY");
            }
        } catch (Exception e) {
            code = Cq9Constants.Code.CODE_1003;
        }
        status.setCode(code.getKey());
        status.setMessage(code.getValue());
        status.setDatetime(DateFormat.ConventDate_RFC3339(new Date()));
        rs.setData(data);
        rs.setStatus(status);
        return rs;

    }

    /***
     * 结束
     */
    public RsModel endRound(HttpServletRequest request) {
        log.info("endRound：" + request.getRequestURI());
        Cq9Constants.Code code = Cq9Constants.Code.CODE_0;
        String methon = request.getRequestURI();
        methon = methon.substring(methon.lastIndexOf("/") + 1);
        RsModel rs = new RsModel();
        RsModel.Status status = new RsModel.Status();
        RsModel.Data rd =null;
        Map<String, String> paramap = RequestParameter.getPara(request);
        String pdata = paramap.get("data");
        paramap.put("data", null);
        EndRoundRqModel rounds = JSON.parseObject(JSON.toJSONString(paramap), EndRoundRqModel.class);
        try {
            List<EndRoundRqModel.Data> list = JSON.parseArray(pdata, EndRoundRqModel.Data.class);
            if (list.size() > 0) {
                rounds.setData(list);
                Map rsmap;
                BigDecimal balance = BigDecimal.ZERO;
                for (EndRoundRqModel.Data data : rounds.getData()) {
                    HashMap map = new HashMap();
                    map.put("account", rounds.getAccount());
                    map.put("amount", data.getAmount());
                    map.put("mtcode", data.getMtcode());
                    rsmap = checkTransferIn(map);
                    code = (Cq9Constants.Code) rsmap.get(MAPKEY_CODE);
                    balance = (BigDecimal) rsmap.get(MAPKEY_BALANCE);
                    /**参数校验不通过*/
                    if (!Cq9Constants.Code.CODE_0.equals(code)) {
                        rd.setBalance(balance);
                        rd.setCurrency("CNY");
                        status.setCode(code.getKey());
                        status.setMessage(code.getValue());
                        status.setDatetime(DateFormat.ConventDate_RFC3339(new Date()));
                        rs.setData(new RsModel.Data());
                        rs.setStatus(status);
                        return rs;
                    }
                }
                String finalMethon = methon;
                rounds.getData().stream().forEach(as -> {
                    BigDecimal finalBalance = accountMap.get(rounds.getAccount());
                    MbrTransferRecordModel record = new MbrTransferRecordModel();
                    MbrTransferRecordModel.Data rdata = new MbrTransferRecordModel.Data();
                    rdata.setBefore(finalBalance);
                    /**成功，写入日志*/
                    finalBalance = finalBalance.add(as.getAmount());
                    rdata.setAction(finalMethon);
                    rdata.setTarget(new MbrTransferRecordModel.Data.Target(rounds.getAccount()));
                    rdata.setStatus(new MbrTransferRecordModel.Data.Status(DateFormat.ConventDate(new Date()), DateFormat.ConventDate(new Date()), "success"));
                    rdata.setBalance(finalBalance);
                    rdata.setCurrency("CNY");
                    rdata.setEvent(new MbrTransferRecordModel.Data.Event(as.getMtcode(), as.getAmount(), DateFormat.ConventDate(new Date())));
                    record.setData(rdata);
                    record.setStatus(new MbrTransferRecordModel.Status(status.getCode(), status.getMessage(), DateFormat.ConventDate(new Date())));
                    insertTransferRecord(record);
                    accountMap.put(rounds.getAccount(), finalBalance);
                });
                rd = new RsModel.Data();
                rd.setBalance(accountMap.get(rounds.getAccount()));
                rd.setCurrency("CNY");
            }
        } catch (Exception e) {
            code = Cq9Constants.Code.CODE_1003;
        }
        status.setCode(code.getKey());
        status.setMessage(code.getValue());
        status.setDatetime(DateFormat.ConventDate_RFC3339(new Date()));
        rs.setData(rd);
        rs.setStatus(status);
        return rs;
    }

    /***
     * 退还
     */
    public RsModel refund(HttpServletRequest request) {
        Cq9Constants.Code code = Cq9Constants.Code.CODE_0;
        log.info("refund：" + request.getRequestURI());
        RsModel rs = new RsModel();
        RsModel.Status status = new RsModel.Status();
        RsModel.Data data = null;
        BigDecimal balance = BigDecimal.ZERO;
        Map paramap = RequestParameter.getPara(request);
        MbrTransferRecordModel record = recordMapper.getTransferRecord(paramap.get("mtcode").toString());
        try {
            if (record == null) {
                code = Cq9Constants.Code.CODE_1014;
            } else if (!Cq9Constants.Status.SUCCESS.equals(record.getProcessStatus())) {
                code = Cq9Constants.Code.CODE_1015;
            } else {
                balance = accountMap.get(record.getData().getTarget().getAccount());
                balance = balance.add(record.getData().getEvent().getAmount());
                accountMap.put(record.getData().getTarget().getAccount(), balance);
                record.setProcessStatus(Cq9Constants.Status.Refund);
                updateTransferRecord(record);
                data = new RsModel.Data();
                data.setBalance(balance);
                data.setCurrency("CNY");
            }
        } catch (Exception e) {
            code = Cq9Constants.Code.CODE_1100;
        }
        status.setCode(code.getKey());
        status.setMessage(code.getValue());
        status.setDatetime(DateFormat.ConventDate_RFC3339(new Date()));
        rs.setData(data);
        rs.setStatus(status);
        return rs;
    }

    /**
     * 判断转出是否通过
     */
    public Map checkTransferOut(Map paramap) {
        Map rs = new HashMap();
        BetRqModel bet = new BetRqModel();
        Cq9Constants.Code code = Cq9Constants.Code.CODE_0;
        BigDecimal balance = new BigDecimal(-1);
        try {
            bet = JSON.parseObject(JSON.toJSONString(paramap), BetRqModel.class);
            if (bet.getAccount().isEmpty()) {
                code = Cq9Constants.Code.CODE_1003;
            } else {
                balance = accountMap.get(bet.getAccount());
            }
            if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
                code = Cq9Constants.Code.CODE_1006;
            }
            if (bet.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                code = Cq9Constants.Code.CODE_1003;
            }
            if (balance != null && balance.compareTo(bet.getAmount()) < 0) {
                code = Cq9Constants.Code.CODE_1005;
            }
            if (recordMapper.getTransferRecord(bet.getMtcode())!= null) {
                code = Cq9Constants.Code.CODE_2009;
            }
        } catch (Exception e) {
            code = Cq9Constants.Code.CODE_1003;
        }
        rs.put(MAPKEY_CODE, code);
        rs.put(MAPKEY_BALANCE, balance);
        return rs;
    }

    /**
     * 判断转出是否通过
     */
    public Cq9Constants.Code checkTransferOutAll(Map paramap) {
        BetRqModel bet = new BetRqModel();
        Cq9Constants.Code code = Cq9Constants.Code.CODE_0;
        try {
            bet = JSON.parseObject(JSON.toJSONString(paramap), BetRqModel.class);
            if (bet.getAccount().isEmpty()) {
                code = Cq9Constants.Code.CODE_1003;
            }
            if (recordMapper.getTransferRecord(bet.getMtcode()) != null) {
                code = Cq9Constants.Code.CODE_2009;
            }
        } catch (Exception e) {
            code = Cq9Constants.Code.CODE_1003;
        }
        return code;
    }

    /**
     * 判断转入是否通过
     */
    public Map checkTransferIn(Map paramap) {
        Map rs = new HashMap();
        BetRqModel bet = new BetRqModel();
        Cq9Constants.Code code = Cq9Constants.Code.CODE_0;
        BigDecimal balance = new BigDecimal(-1);
        try {
            bet = JSON.parseObject(JSON.toJSONString(paramap), BetRqModel.class);
            if (bet.getAccount().isEmpty()) {
                code = Cq9Constants.Code.CODE_1003;
            } else {
                balance = accountMap.get(bet.getAccount());
            }
            if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
                code = Cq9Constants.Code.CODE_1006;
            }
            if (bet.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                code = Cq9Constants.Code.CODE_1003;
            }

            if (recordMapper.getTransferRecord(bet.getMtcode())!= null) {
                code = Cq9Constants.Code.CODE_2009;
            }
        } catch (Exception e) {
            code = Cq9Constants.Code.CODE_1003;
        }

        rs.put(MAPKEY_CODE, code);
        rs.put(MAPKEY_BALANCE, balance);
        return rs;
    }

    public RsMbrTransferRecordModel getTransferRecord(HttpServletRequest request) {
        Cq9Constants.Code code = Cq9Constants.Code.CODE_0;
        MbrTransferRecordModel rs = new MbrTransferRecordModel();
        RsMbrTransferRecordModel rsmbr=new RsMbrTransferRecordModel();
        rs.setData(null);
        try {
            String mtcode =URLDecoder.decode(request.getRequestURI(),"utf8") ;
            mtcode=mtcode.substring(mtcode.lastIndexOf("/") + 1);
            log.info("getTransferRecord" + mtcode);
            if (!mtcode.isEmpty()) {
                rs = recordMapper.getTransferRecord(mtcode);
                if (rs == null) {
                    rs = new MbrTransferRecordModel();
                    code = Cq9Constants.Code.CODE_1014;
                }
            } else {
                code = Cq9Constants.Code.CODE_1003;
            }
        } catch (Exception e) {
            code = Cq9Constants.Code.CODE_1003;
        }
        MbrTransferRecordModel.Status status = new MbrTransferRecordModel.Status();
        status.setCode(code.getKey());
        status.setMessage(code.getValue());
        status.setDatetime(DateFormat.ConventDate_RFC3339(new Date()));
        rsmbr.setStatus(status);
        rsmbr.setData(rs.getData());
        return rsmbr;
    }


    public RsModel getBalance(HttpServletRequest request) {
        String methon = request.getRequestURI();
        log.info("getBalance" + methon);
        BetRqModel bet = new BetRqModel();
        bet.setAccount(methon.substring(methon.lastIndexOf("/") + 1));
        Cq9Constants.Code code = Cq9Constants.Code.CODE_0;
        BigDecimal balance = BigDecimal.ZERO;
        if (accountMap.get(bet.getAccount()) == null) {
            code = Cq9Constants.Code.CODE_1006;
        } else {
            balance = accountMap.get(bet.getAccount());
        }
        RsModel rs = new RsModel();
        RsModel.Status status = new RsModel.Status();
        RsModel.Data data = new RsModel.Data();
        data.setBalance(balance);
        data.setCurrency("CNY");
        status.setCode(code.getKey());
        status.setMessage(code.getValue());
        status.setDatetime(DateFormat.ConventDate_RFC3339(new Date()));
        rs.setData(data);
        rs.setStatus(status);
        return rs;
    }

    public Map checkPlayer(HttpServletRequest request) {
        log.info("checkPlayer" + request.getRequestURI());
        Map map = new HashMap();
        String methon = request.getRequestURI();
        BetRqModel bet = new BetRqModel();
        bet.setAccount(methon.substring(methon.lastIndexOf("/") + 1));
        Cq9Constants.Code code = Cq9Constants.Code.CODE_0;
        if (accountMap.get(bet.getAccount()) == null) {
            code = Cq9Constants.Code.CODE_1006;
            map.put("data", false);
        } else {
            map.put("data", true);
        }
        RsModel.Status status = new RsModel.Status();
        status.setCode(code.getKey());
        status.setMessage(code.getValue());
        status.setDatetime(DateFormat.ConventDate_RFC3339(new Date()));
        map.put("status", status);
        return map;
    }

    public Map generateToken() {
        String token = jwtUtils.generateToken(PlatFromEnum.ENUM_CQ9.getKey());
        Map<String, Object> map = new HashMap<>();
        map.put("wtoken", token);
        map.put("expire", jwtUtils.getExpire());
        sysService.updateDepotTokenCache(PlatFromEnum.ENUM_CQ9.getKey(), token);
        return map;
    }

    public int insertTransferRecord(MbrTransferRecordModel rd) {
        return recordMapper.insertTransferRecord(rd);
    }

    public int updateTransferRecord(MbrTransferRecordModel rd) {
        return recordMapper.updateTransferRecord(rd);
    }

}
