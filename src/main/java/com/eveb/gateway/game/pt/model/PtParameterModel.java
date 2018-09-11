package com.eveb.gateway.game.pt.model;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class PtParameterModel {

	private String loginname;
	private String kioskname;
	private String adminname;
	private String password;
	private String language;
	private String game;
	private String mod;
	private Integer amount;
	private String externaltranid;
	private String externaltransactionid;
	private String[] frozen;
	private String isForce;
	private String unFrozen;

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		if (!StringUtils.isEmpty(mod))
			buffer.append(mod);
		if (!StringUtils.isEmpty(loginname) && StringUtils.isEmpty(frozen) && StringUtils.isEmpty(unFrozen))
			buffer.append("/playername/").append(loginname.toUpperCase());
		
		if (!StringUtils.isEmpty(kioskname))
			buffer.append("/kioskname/").append(kioskname);
		
		if (!StringUtils.isEmpty(adminname))
			buffer.append("/adminname/").append(adminname);
		
		if (!StringUtils.isEmpty(password))
			buffer.append("/password/").append(password);
		
		if (!StringUtils.isEmpty(amount))
			buffer.append("/amount/").append(amount);
		
		if (!StringUtils.isEmpty(externaltranid))
			buffer.append("/externaltranid/").append(externaltranid);
		
		if (!StringUtils.isEmpty(isForce))
			buffer.append("/isForce/").append(isForce);
		
		if (!StringUtils.isEmpty(externaltransactionid))
			buffer.append("/externaltransactionid/").append(externaltransactionid);

		if (!StringUtils.isEmpty(frozen))
			buffer.append(frozen[0]).append("/"+loginname).append("/frozen/").append(frozen[1]);

		if (!StringUtils.isEmpty(unFrozen))
			buffer.append(unFrozen).append("/"+loginname);
		return buffer.toString();
	}
	public interface IsForce
	{
		String is="1";//是强制提款
		String no="0";//否强制提款
	}

}
