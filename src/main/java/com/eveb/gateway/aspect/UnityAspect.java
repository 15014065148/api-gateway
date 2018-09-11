package com.eveb.gateway.aspect;

import com.alibaba.fastjson.JSON;
import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.constants.PlatFromEnum;
import com.eveb.gateway.game.agin.service.AginService;
import com.eveb.gateway.game.avia.service.AviaServiceImpl;
import com.eveb.gateway.game.bbin.service.BbinService;
import com.eveb.gateway.game.bg.service.BgServiceImpl;
import com.eveb.gateway.game.ea.service.TsServiceImpl;
import com.eveb.gateway.game.elg.service.ElgServiceImpl;
import com.eveb.gateway.game.fun.service.FgServiceImpl;
import com.eveb.gateway.game.gd.service.GdServiceImpl;
import com.eveb.gateway.game.gg.service.GgServiceImpl;
import com.eveb.gateway.game.gns.service.GnsServiceImpl;
import com.eveb.gateway.game.kg.service.KgServiceImpl;
import com.eveb.gateway.game.mg.service.MgService;
import com.eveb.gateway.game.model.RequestLog;
import com.eveb.gateway.game.model.TGmApi;
import com.eveb.gateway.game.n2.service.N2ServiceImpl;
import com.eveb.gateway.game.pg.service.PgServiceImpl;
import com.eveb.gateway.game.png.service.PngService;
import com.eveb.gateway.game.pt.service.PtService;
import com.eveb.gateway.game.pt2.service.Pt2Service;
import com.eveb.gateway.game.service.ElasticService;
import com.eveb.gateway.game.ttg.service.TtgServiceImpl;
import com.eveb.gateway.game.vr.service.VrServiceImpl;
import com.eveb.gateway.game.service.SysService;
import com.eveb.gateway.game.unity.model.UnityParameterModel;
import com.eveb.gateway.game.unity.model.UnityResultModel;
import com.eveb.gateway.utils.IpUtils;
import com.eveb.gateway.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Aspect
@Component
public class UnityAspect {

    @Autowired
    private SysService sysService;
    @Autowired
    private BbinService bbinService;
    @Autowired
    private PtService ptService;
    @Autowired
    private AginService aginService;
    @Autowired
    private MgService mgService;
    @Autowired
    private Pt2Service pt2Service;
    @Autowired
    private PngService pngService;
    @Autowired
    private GnsServiceImpl gnsService;
    @Autowired
    private PgServiceImpl pgServiceImpl;
    @Autowired
    private FgServiceImpl fgService;
    @Autowired
    private GgServiceImpl ggService;
    @Autowired
    private TsServiceImpl tsServiceImpl;
    @Autowired
    private VrServiceImpl vrServiceImpl;
    @Autowired
    private BgServiceImpl bgServiceImpl;
    @Autowired
    private GdServiceImpl gdServiceImpl;
    @Autowired
    private ElgServiceImpl elgServiceImpl;
    @Autowired
    private AviaServiceImpl aviaService;
    @Autowired
    private N2ServiceImpl n2Service;
    @Autowired
    private TtgServiceImpl ttgService;
    @Autowired
    private ElasticService elasticService;
    @Autowired
    private KgServiceImpl kgServiceImpl;

    public static ThreadLocal<RequestLog> threadLocalRequestLog = new ThreadLocal<>();

    @Around("execution(* com.eveb.gateway.game.unity.controller.*.*(..))")
    public Object initDepotService(ProceedingJoinPoint point) throws Throwable {
        /**写入操作日志**/
        RequestLog rlog = new RequestLog();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        TGmApi tg = null;
        String[] paras = request.getRequestURI().split("/");
        Object[] args = point.getArgs();
        UnityParameterModel um = (UnityParameterModel) args[0];
        String depot = paras[paras.length - 2];
        rlog.setPlatform(depot);
        rlog.setParameter(JSON.toJSONString(args[0]));
        rlog.setSitePrefix(um.getSiteCode());
        if (PlatFromEnum.ENUM_BBIN.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_BBIN.getKey());
            args[1] = bbinService;
        } else if (PlatFromEnum.ENUM_PT.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_PT.getKey());
            args[1] = ptService;
        } else if (PlatFromEnum.ENUM_AGIN.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_AGIN.getKey());
            args[1] = aginService;
        } else if (PlatFromEnum.ENUM_PT2.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_PT2.getKey());
            args[1] = pt2Service;
        } else if (PlatFromEnum.ENUM_PNG.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_PNG.getKey());
            args[1] = pngService;
        } else if (PlatFromEnum.ENUM_MG.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_MG.getKey());
            args[1] = mgService;
        } else if (PlatFromEnum.ENUM_GNS.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_GNS.getKey());
            args[1] = gnsService;
        } else if (PlatFromEnum.ENUM_PG.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_PG.getKey());
            args[1] = pgServiceImpl;
        } else if (PlatFromEnum.ENUM_FUN.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_FUN.getKey());
            args[1] = fgService;
        } else if (PlatFromEnum.ENUM_GG.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_GG.getKey());
            args[1] = ggService;
        } else if (PlatFromEnum.ENUM_TS.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_TS.getKey());
            args[1] = tsServiceImpl;
        } else if (PlatFromEnum.ENUM_VR.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_VR.getKey());
            args[1] = vrServiceImpl;
        } else if (PlatFromEnum.ENUM_BG.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_BG.getKey());
            args[1] = bgServiceImpl;
        } else if (PlatFromEnum.ENUM_GD.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_GD.getKey());
            args[1] = gdServiceImpl;
        } else if (PlatFromEnum.ENUM_ELG.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_ELG.getKey());
            args[1] = elgServiceImpl;
        } else if (PlatFromEnum.ENUM_AVIA.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_AVIA.getKey());
            args[1] = aviaService;
        } else if (PlatFromEnum.ENUM_N2.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_N2.getKey());
            args[1] = n2Service;
        } else if (PlatFromEnum.ENUM_TTG.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_TTG.getKey());
            args[1] = ttgService;
        }else if (PlatFromEnum.ENUM_KG.getKey().equals(depot.toUpperCase())) {
            tg = sysService.getApiBySiteCode(um.getSiteCode(), PlatFromEnum.ENUM_KG.getKey());
            args[1] = kgServiceImpl;
        } else {
            return new UnityResultModel(Boolean.FALSE, UnityResultModel.ErrMsg.ERROR_PLATFORM_NULL);
        }
        if (tg == null) {
            return new UnityResultModel(Boolean.FALSE, UnityResultModel.ErrMsg.ERROR_API_NULL);
        }
        um.setTGmApi(tg);
        args[0] = um;
        rlog.setStatus(Boolean.TRUE);
        Long startTime = System.currentTimeMillis();
        Object obj = new UnityResultModel(false, ApplicationConstants.ACTION_ERROR);
        threadLocalRequestLog.set(rlog);
        try {
            obj = point.proceed(args);
        } catch (Exception e) {
            rlog.setStatus(Boolean.FALSE);
            log.info("全局异常："+e.getMessage());
        }
        /**写入时间**/
        rlog = threadLocalRequestLog.get();
        Long endTime = System.currentTimeMillis();
        rlog.setTimes(endTime - startTime);
        rlog.setStartTime(new Date(startTime));
        rlog.setEndTime(new Date(endTime));
        rlog.setIp(IpUtils.getIp());
        RequestLog finalRlog = rlog;
        CompletableFuture.runAsync(() -> {
            saveLog(point, finalRlog);
        });
        return obj;
    }

    private void saveLog(ProceedingJoinPoint point, RequestLog rlog) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        rlog.setApiName(method.getName());
        elasticService.insert(rlog);
    }
}
