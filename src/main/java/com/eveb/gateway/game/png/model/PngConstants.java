package com.eveb.gateway.game.png.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PngConstants {


	public static final String REG_BIRTH_DAY = "1990-01-01";
	public static final String AUTHORIZATION = "Authorization";
	public static final String SOAPACTION="SOAPAction";
	public static final String BASIC_SOAPACTION = "http://playngo.com/v1/CasinoGameService/";
	public static final String REGISTER_SUC_CODE="RegisterUserResponse";
	public static final String DEF_IP="127.0.0.1";
	public static final String GAME_LAUNCH_DIV="pngCasinoGame";

	public static String registerDate(){
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	public interface Method {
		/**注册*/
		String REGISTER = "RegisterUser";
		/**登陆令牌*/
		String GETTICK = "GetTicket";
		/** 会员信息 余额*/
		String BALANCE = "Balance";
		/**转入*/
		String CREDIT = "Credit";
		/**转出*/
		String DEBIT = "Debit";
	}

	public interface JsonKey {
		String url = "url";
		String guid = "guid";
		String brandId = "BrandId";
		String pid="pid";
	}

	public interface Languages {
		String brazilian = "pt_BR";
		String bulgarian = "bg_BG";
		String catalan = "ca_ES";
		String chinese_S = "zh_CN";
		String chinese_T = "zh_TW";
		String croatian = "hr_HR";
		String czech = "cs_CZ";
		String danish = "da_DK";
		String dutch = "nl_NL";
		String english = "en_GB";
		String estonian = "et_EE";
		String finnish = "fi_FI";
		String french = "fr_FR";
		String german = "de_DE";
		String greek = "el_GR";
		String hungarian = "hu_HU";
		String icelandic = "is_IS";
		String italian = "lt_LT";
		String japanese = "ja_JP";
		String latvian = "lv_LV";
		String lithuanian = "lt_LT";
		String norwegian = "no_NO";
		String polish = "pl_PL";
		String portugese = "pt_PT";
		String romanian = "ro_RO";
		String russian = "ru_RU";
		String slovak = "sk_SK";
		String slovenian = "sl_SI";
		String spanish = "es_ES";
		String spanish_P = "es_PE";
		String swedish = "sv_SE";
		String turkish = "tr_TR";
	}

	public interface Currencies {
		String china = "CNY";
	}

	public interface Countrys {
		String china = "CN";
	}

	public interface Gender {
		String male = "m";
		String female = "f";
	}
	
	public interface Practice
	{
	    String free="1";//免费
	    String pay="0";//真钱玩
	}

}
