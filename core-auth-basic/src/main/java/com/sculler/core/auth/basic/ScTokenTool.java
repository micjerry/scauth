package com.sculler.core.auth.basic;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sculler.core.auth.api.cache.ScCacheContext;
import com.sculler.core.auth.api.digest.ScToken;

@Component
class ScTokenTool {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ScTokenCache tokenCacheTool;
	
	public ScToken createToken(String username) {	
		ScToken token = new ScToken();
		token.setUsername(username);
		token.setRefreshtoken(UUID.randomUUID().toString().replaceAll("-", ""));
		token.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
		token.setExpires(ScCacheContext.EXPIRETIME_TOKEN_SECONDS);
		
		tokenCacheTool.cacheToken(token);
		
		return token;
	}
	
	public String identifyToken(String token) {
		return tokenCacheTool.getUsernameByToken(token);
	}
	
	public ScToken refreshToken(String refreshToken) {
		String username = tokenCacheTool.getUsernameByRefreshToken(refreshToken);
		if (StringUtils.isBlank(username)) {
			logger.error("invalid refresh token: {}", refreshToken);
			return null;
		}
		
		String newToken = UUID.randomUUID().toString().replaceAll("-", "");
		
		ScToken token = new ScToken();
		token.setRefreshtoken(refreshToken);
		token.setToken(newToken);
		token.setUsername(username);
		token.setExpires(ScCacheContext.EXPIRETIME_TOKEN_SECONDS);
		
		tokenCacheTool.cacheNewToken(token);
		return token;
	}
	
	public void clearRereshToken(String refreshToken) {
		tokenCacheTool.clearToken(refreshToken);
	}
	
	public void clearAllToken(String username) {
		tokenCacheTool.clearAllTokenByUsername(username);
	}
	
	public String createNonce(String username) {
		String nonce = UUID.randomUUID().toString().replaceAll("-", "");
		tokenCacheTool.cacheNonce(nonce, username);
		return nonce;
	}
	
	public boolean identifyNonce(String nonce, String username) {
		String cachUsername = tokenCacheTool.getUsernameByNonce(nonce);
		if (StringUtils.isBlank(cachUsername)) {
			return false;
		}
		
		if (!cachUsername.equals(username))
			return false;
		
		return true;
	}
}
