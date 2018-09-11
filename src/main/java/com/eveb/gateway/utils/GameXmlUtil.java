package com.eveb.gateway.utils;

import org.dom4j.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameXmlUtil {


    public static void mgParseRes(Map<String, String> map,String result)
	{
		Document doc;
		try {
			doc = DocumentHelper.parseText(result);
			Element root = doc.getRootElement();
			//System.out.println("当前节点的名称：" + root.getName());
			// 首先获取当前节点的所有属性节点
			List<Attribute> list = root.attributes();
			// 遍历属性节点
			for (Attribute attribute : list) {
				map.put(attribute.getName(), attribute.getValue());
				// System.out.println("属性"+attribute.getName() +":" + attribute.getValue());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
    
    public static void mgParseBanlceRes(Map<String, Object> map,String result)
	{
		Document doc;
		try {
			doc = DocumentHelper.parseText(result);
			Element root = doc.getRootElement();
			//System.out.println("当前节点的名称：" + root.getName());
			// 首先获取当前节点的所有属性节点
			List<Attribute> list = root.attributes();
			// 遍历属性节点
			for (Attribute attribute : list) {
				map.put(attribute.getName(), attribute.getValue());
				// System.out.println("属性"+attribute.getName() +":" + attribute.getValue());
			}
			Iterator<Element> iterator = root.elementIterator();
			Element walletNode = iterator.next();
			 iterator = walletNode.elementIterator();
			while (iterator.hasNext()) {
				Element e = iterator.next();
				List<Attribute> listnode = e.attributes();
				Map<String,String> mapnode=new HashMap<String,String>();
				// 遍历属性节点
				for (Attribute attributeNode : listnode) {
					mapnode.put(attributeNode.getName(), attributeNode.getValue());
				}
				map.put(e.getName(), mapnode);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
