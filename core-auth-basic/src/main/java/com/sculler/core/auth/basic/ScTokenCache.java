package com.sculler.core.auth.basic;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sculler.core.auth.api.cache.ScAuthKeyType;
import com.sculler.core.auth.api.cache.ScAuthValueType;
import com.sculler.core.auth.api.cache.ScCacheContext;
import com.sculler.core.auth.api.cache.ScKeyValueCacheTool;
import com.sculler.core.auth.api.cache.ScSetCacheTool;
import com.sculler.core.auth.api.digest.ScToken;

@Component
class ScTokenCache {

	@Autowired
	private ScKeyValueCacheTool keyCacheTool;

	@Autowired
	private ScSetCacheTool setCacheTool;

	void cacheToken(ScToken token) {
		keyCacheTool.cache(ScAuthKeyType.TOKEN_KEY, token.getToken(), ScAuthValueType.USERNAME_VALUE,
				token.getUsername(), ScCacheContext.EXPIRETIME_TOKEN_SECONDS, TimeUnit.SECONDS);
		keyCacheTool.cache(ScAuthKeyType.REFRESH_TOKEN_KEY, token.getRefreshtoken(), ScAuthValueType.TOKEN_VALUE,
				token.getToken(), ScCacheContext.EXPIRETIME_TOKEN_SECONDS, TimeUnit.SECONDS);
		keyCacheTool.cache(ScAuthKeyType.REFRESH_TOKEN_KEY, token.getRefreshtoken(), ScAuthValueType.USERNAME_VALUE,
				token.getUsername(), ScCacheContext.EXPIRETIME_REFRESHTOKEN_DAYS, TimeUnit.DAYS);
		setCacheTool.add(ScAuthKeyType.USERNAME_KEY, token.getUsername(), ScAuthValueType.REFRESH_TOKEN_VALUE,
				token.getRefreshtoken(), ScCacheContext.EXPIRETIME_USERREFTOKENS_DAYS, TimeUnit.DAYS);

	}

	void clearToken(String refreshToken) {
		String token = keyCacheTool.get(ScAuthKeyType.REFRESH_TOKEN_KEY, refreshToken, ScAuthValueType.TOKEN_VALUE);
		if (!StringUtils.isBlank(token)) {
			keyCacheTool.clear(ScAuthKeyType.TOKEN_KEY, token, ScAuthValueType.USERNAME_VALUE);
		}

		keyCacheTool.clear(ScAuthKeyType.REFRESH_TOKEN_KEY, refreshToken, ScAuthValueType.TOKEN_VALUE);

		String username = keyCacheTool.get(ScAuthKeyType.REFRESH_TOKEN_KEY, refreshToken,
				ScAuthValueType.USERNAME_VALUE);
		keyCacheTool.clear(ScAuthKeyType.REFRESH_TOKEN_KEY, refreshToken, ScAuthValueType.USERNAME_VALUE);

		if (!StringUtils.isBlank(username)) {
			setCacheTool.remove(ScAuthKeyType.USERNAME_KEY, username, ScAuthValueType.REFRESH_TOKEN_VALUE,
					refreshToken);
		}
	}

	void cacheNewToken(ScToken token) {
		String oldToken = keyCacheTool.get(ScAuthKeyType.REFRESH_TOKEN_KEY, token.getRefreshtoken(),
				ScAuthValueType.TOKEN_VALUE);
		if (!StringUtils.isBlank(oldToken)) {
			keyCacheTool.expire(ScAuthKeyType.TOKEN_KEY, oldToken, ScAuthValueType.USERNAME_VALUE,
					ScCacheContext.EXPIRETIME_TOKENDROPED_SECONDS, TimeUnit.SECONDS);
		}

		keyCacheTool.cache(ScAuthKeyType.TOKEN_KEY, token.getToken(), ScAuthValueType.USERNAME_VALUE,
				token.getUsername(), ScCacheContext.EXPIRETIME_TOKEN_SECONDS, TimeUnit.SECONDS);
		keyCacheTool.cache(ScAuthKeyType.REFRESH_TOKEN_KEY, token.getRefreshtoken(), ScAuthValueType.TOKEN_VALUE,
				token.getToken(), ScCacheContext.EXPIRETIME_TOKEN_SECONDS, TimeUnit.SECONDS);

		keyCacheTool.expire(ScAuthKeyType.REFRESH_TOKEN_KEY, token.getRefreshtoken(), ScAuthValueType.USERNAME_VALUE,
				ScCacheContext.EXPIRETIME_REFRESHTOKEN_DAYS, TimeUnit.DAYS);
	}
	
	String getUsernameByToken(String token) {
		return keyCacheTool.get(ScAuthKeyType.TOKEN_KEY, token, ScAuthValueType.USERNAME_VALUE);
	}
	
	String getUsernameByRefreshToken(String refreshToken) {
		return keyCacheTool.get(ScAuthKeyType.REFRESH_TOKEN_KEY, refreshToken, ScAuthValueType.USERNAME_VALUE);
	}
	
	void clearAllTokenByUsername(String username) {
		Set<String> refreshTokens = setCacheTool.get(ScAuthKeyType.USERNAME_KEY, username, ScAuthValueType.REFRESH_TOKEN_VALUE);
		
		for (String refreshToken: refreshTokens) {
			clearToken(refreshToken);
		}
		
		keyCacheTool.clear(ScAuthKeyType.USERNAME_KEY, username, ScAuthValueType.REFRESH_TOKEN_VALUE);
	}
	
	void cacheNonce(String nonce, String username) {
		keyCacheTool.cache(ScAuthKeyType.NONCE_KEY, nonce, ScAuthValueType.USERNAME_VALUE,
				username, ScCacheContext.EXPIRETIME_NONCE_SECONDS, TimeUnit.SECONDS);
	}
	
	String getUsernameByNonce(String nonce) {
		return keyCacheTool.get(ScAuthKeyType.NONCE_KEY, nonce, ScAuthValueType.USERNAME_VALUE);
	}

}
