package com.eveb.gateway.utils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class RequestParameter {

    /**
     * 读取拼接方式
     * @param request
     * @return
     */
    public static Map getPara(HttpServletRequest request) {
        Map<String, String> parmMap = new HashMap<>();
        Map<String, String[]> map = (Map<String, String[]>) request.getParameterMap();
        //参数名称
        Set<String> key = map.keySet();
        //参数迭代器
        Iterator<String> iterator = key.iterator();
        while (iterator.hasNext()) {
            String k = iterator.next();
            parmMap.put(k, map.get(k)[0]);
        }
        return parmMap;
    }

    public static String getHead(HttpServletRequest request, String key) {
        String rs = "";
        Enumeration enu = request.getHeaderNames();
        //以此取出头信息
        while (enu.hasMoreElements()) {
            String headerName = (String) enu.nextElement();
            rs = request.getHeader(headerName);
            if (key.toUpperCase().equals(headerName.toUpperCase())) {
                break;
            }
        }
        return rs;
    }

    /***
     * 返回大写的key
     * @param request
     * @return
     */
    public static Map<String, String> getHead(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        String rs = "";
        Enumeration enu = request.getHeaderNames();
        //以此取出头信息
        while (enu.hasMoreElements()) {
            String headerName = (String) enu.nextElement();
            rs = request.getHeader(headerName);
            map.put(headerName.toUpperCase(), rs);
        }
        return map;
    }

    /**
     * 读取流形式
     * @param request
     * @return
     * @throws Exception
     */
    public static String reader(HttpServletRequest request) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (ServletInputStream) request.getInputStream()));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }catch (Exception e)
        {
            return "";
        }
    }
}
