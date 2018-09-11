package com.eveb.gateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * jwt工具类
 */
@ConfigurationProperties(prefix = "eveb.jwt")
@Component
@Getter
@Setter
public class JwtUtils {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private String secret;
	private long expire;
	private String header;
	private String secretFindPwd;
	private long expireFindPwd;

	/**
	 * 生成jwt token
	 */
	public String generateToken(String depotName) {
		Date nowDate = new Date();
		// 过期时间
		Date expireDate = new Date(nowDate.getTime() + expire * 1000);

		return Jwts.builder().setHeaderParam("typ", "JWT").setSubject(depotName)
				.setIssuedAt(nowDate).setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Claims getClaimByToken(String token) {
		try {
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			logger.debug("validate is token error ", e);
			return null;
		}
	}

	/**
	 * token是否过期
	 *
	 * @return true：过期
	 */
	public boolean isTokenExpired(Date expiration) {
		return expiration.before(new Date());
	}

	/**
	 * 生成jwt token
	 */
	public String generatefindPwdToken(String depotName) {
		Date nowDate = new Date();
		// 过期时间
		Date expireDate = new Date(nowDate.getTime() + expireFindPwd * 1000);

		return Jwts.builder().setHeaderParam("typ", "JWT").setSubject(depotName).setIssuedAt(nowDate)
				.setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, secretFindPwd).compact();
	}


	public Claims getClaimByfindPwdToken(String token) {
		try {
			return Jwts.parser().setSigningKey(secretFindPwd).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			logger.debug("validate is token error ", e);
			return null;
		}
	}
}
