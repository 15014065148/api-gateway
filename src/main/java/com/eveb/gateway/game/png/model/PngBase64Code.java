package com.eveb.gateway.game.png.model;


import lombok.Data;

import java.util.Base64;

@Data
public class PngBase64Code {
	private String userName;
	private String passWd;

	@Override
	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("Basic ");
		try {
			sBuffer.append(Base64.getEncoder().encodeToString((userName + ":" + passWd).getBytes("UTF-8")));
		} catch (Exception e) {
		}
		return sBuffer.toString();
	}
}
