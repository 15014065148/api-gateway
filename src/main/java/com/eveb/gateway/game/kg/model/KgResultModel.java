package com.eveb.gateway.game.kg.model;

import com.eveb.gateway.game.unity.model.UnityResultModel;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class KgResultModel {

    /**
     * 统一结果解析返回
     */
    public UnityResultModel getResult(String resultXml) {
        Map<String, String> map = xmlDataAnalysis(resultXml);
        if (map != null) {
            if (StringUtils.isEmpty(map.get("name")) || StringUtils.isEmpty(map.get("value"))) {
                return new UnityResultModel(false, KgConstants.ErrEnum.getMsg(map.get("value").replace(" ", "")));
            }
            switch (map.get("name")) {
                case KgConstants.LINK:
                    return new UnityResultModel(true, map.get("value"));
                case KgConstants.FUNDINTEGRATIONID:
                    return new UnityResultModel(true, Integer.parseInt(map.get("value")));
                case KgConstants.CREDIT:
                    return new UnityResultModel(true, Double.parseDouble(map.get("value")));
                case KgConstants.REMAIN:
                    return new UnityResultModel(true, Double.parseDouble(map.get("value")));
            }
            return new UnityResultModel(true, map.get("value"));
        }
        return new UnityResultModel(false, KgConstants.ErrEnum.getMsg(map.get("value").replace(" ", "")));
    }


    /**
     * xml请求结果统一解析
     */
    private Map<String, String> xmlDataAnalysis(String resultXml) {
        try {
            Map<String, String> map = new HashMap<>();
            StringReader sr = new StringReader(resultXml);
            InputSource is = new InputSource(sr);
            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);
            Element memberElement = doc.getRootElement().element("params").element("param").element("value").element("struct").element("member");
            Element nameElement = memberElement.element("name");
            map.put("name", nameElement.getTextTrim());
            Element valueElement = memberElement.element("value");
            Iterator<Element> it = valueElement.elementIterator();
            Element subValueElement = it.next();
            map.put("value", subValueElement.getTextTrim());
            return map;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
