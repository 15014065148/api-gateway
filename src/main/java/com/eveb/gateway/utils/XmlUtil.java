package com.eveb.gateway.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultAttribute;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class XmlUtil {

    public static JSONObject xml2JSON(byte[] xml) throws Exception {
        JSONObject json = new JSONObject();
        InputStream is = new ByteArrayInputStream(xml);
        SAXBuilder sb = new SAXBuilder();
        org.jdom2.Document doc = sb.build(is);
        Element root = doc.getRootElement();
        json.put(root.getName(), iterateElement(root));
        return json;
    }

    private static JSONObject iterateElement(Element element) {
        List node = element.getChildren();
        Element et = null;
        JSONObject obj = new JSONObject();
        List list = null;
        for (int i = 0; i < node.size(); i++) {
            list = new LinkedList();
            et = (Element) node.get(i);
            if (et.getTextTrim().equals("")) {
                if (et.getChildren().size() == 0) {
                    continue;
                }
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(iterateElement(et));
                obj.put(et.getName(), list);
            } else {
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(et.getTextTrim());
                obj.put(et.getName(), list);
            }
        }
        return obj;
    }

    /***
     * 格式 属性方式 <aaa xxx="xxx" xxx="xxx" xxx="xxx" xxx="xxx"/>
     */
    public static Map xmlToMap(String rs) {
        Map rsmap = new HashMap();
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new ByteArrayInputStream(rs.getBytes()));
        } catch (DocumentException e) {

            return rsmap;
        }
        // 获得文档对象的根节点
        org.dom4j.Element rootElement = document.getRootElement();
        // 获得根节点下面所有的子节点
        List<DefaultAttribute> listElement = rootElement.attributes();
        // 遍历所有的子节点
        for (DefaultAttribute e1 : listElement) {
            rsmap.put(e1.getQName().getName(), e1.getStringValue());
        }
        return rsmap;
    }

    public static Map<String, Object> n2JSON(String xml) {
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            SAXBuilder sb = new SAXBuilder();
            org.jdom2.Document doc = sb.build(is);
            List node = doc.getRootElement().getChildren();
            Element et = null;
            JSONObject obj = new JSONObject();
            for (int i = 0; i < node.size(); i++) {
                et = (Element) node.get(i);
                Attribute ab = et.getAttributes().get(0);
                obj.put(ab.getName(), ab.getValue());
                for (Element e : ab.getParent().getChildren()) {
                    obj.put(e.getAttributes().get(0).getValue(), e.getValue());
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
