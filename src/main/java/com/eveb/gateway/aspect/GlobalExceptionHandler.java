package com.eveb.gateway.aspect;

import com.eveb.gateway.constants.ApplicationConstants;
import com.eveb.gateway.game.bg.model.BgConstants;
import com.eveb.gateway.game.unity.model.UnityResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常处理
 */
@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public UnityResultModel commonHandler(Exception e) {
        log.info("全局异常："+e.getMessage());
        e.printStackTrace();
        return new UnityResultModel(false, ApplicationConstants.ACTION_ERROR);
    }

}
