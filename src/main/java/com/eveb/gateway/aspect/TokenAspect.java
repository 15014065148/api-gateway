//package com.eveb.gateway.aspect;
//
//import com.eveb.gateway.constants.PlatFromEnum;
//import com.eveb.gateway.game.service.SysService;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Aspect
//@Component
//public class TokenAspect {
//
//    @Autowired
//    private SysService sysService;
//
//    @Around("execution(* com.eveb.gateway.game.cq9.controller.*.*(..))&& !execution(* com.eveb.gateway.game.cq9.controller.TokenController.*(..))")
//    public Object checkToken(ProceedingJoinPoint point) throws Throwable {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String wtoken = request.getHeader("wtoken");
//        String token = sysService.queryDepotTokenCache(PlatFromEnum.ENUM_CQ9.getKey());
//        if (token != null && token.equals(wtoken)) {
//           return point.proceed();
//        }else
//        {
//            return "未授权的访问!";
//        }
//    }
//}
