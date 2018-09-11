package com.eveb.gateway.game.service;

import com.eveb.gateway.game.mapper.SysMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OKHttpClientService {

    @Autowired
    private SysMapper sysMapper;

    public List<String> getProxys(String groups){return sysMapper.selectProxys(groups);}
}
