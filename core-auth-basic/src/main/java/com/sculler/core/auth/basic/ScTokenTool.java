package com.sculler.core.auth.basic;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.sculler.core.auth.redis.RedisUtil;

@Component
class ScTokenTool {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	public ScToken createToken(String username) {	
		ScToken token = new ScToken();
		token.setUsername(username);
		token.setToken(genTempToken(username));
		token.setRefreshtoken(genRefreshToken(username));
		token.setExpires(ScToken.DEFAULT_TOKEN_EXPIRESECONDS);
		
		redisTemplate.opsForList().leftPush(RedisUtil.buildUserRefreshTokenKey(token.getUsername()), token.getRefreshtoken());
		
		return token;
	}
	
	public String identifyToken(String token) {
		String username = redisTemplate.opsForValue().get(RedisUtil.buildKey(token));
		return username;
	}
	
	public ScToken refreshToken(String refreshToken) {
		String username = redisTemplate.opsForValue().get(RedisUtil.buildKey(refreshToken));
		if (null == username || StringUtils.isBlank(username)) {
			logger.error("invalid refresh token: {}", refreshToken);
			return null;
		}
		
		ScToken token = new ScToken();
		String temp_token = UUID.randomUUID().toString().replaceAll("-", "");
		redisTemplate.opsForValue().set(RedisUtil.buildKey(temp_token), username);
		redisTemplate.expire(RedisUtil.buildKey(temp_token), ScToken.DEFAULT_TOKEN_EXPIRESECONDS, TimeUnit.SECONDS);
		
		redisTemplate.expire(RedisUtil.buildKey(refreshToken), ScToken.DEFAULT_REFRESH_EXPIREDAYS, TimeUnit.DAYS);
		
		token.setToken(temp_token);
		token.setExpires(ScToken.DEFAULT_TOKEN_EXPIRESECONDS);
		return token;
	}
	
	public void clearAllToken(String username) {		
		Set<Object> refreshTokens = redisTemplate.opsForHash().keys(RedisUtil.buildUserRefreshTokenKey(username));
		
		for (Object token: refreshTokens) {
			redisTemplate.delete(RedisUtil.buildKey((String)token));
		}
		
		redisTemplate.delete(RedisUtil.buildUserRefreshTokenKey(username));
	}
	
	public void clearRefreshToken(String refreshToken) {
		String username = redisTemplate.opsForValue().get(RedisUtil.buildKey(refreshToken));
		redisTemplate.delete(RedisUtil.buildKey(refreshToken));
		
		if (!StringUtils.isBlank(username)) {
			redisTemplate.opsForHash().delete(RedisUtil.buildUserRefreshTokenKey(username), refreshToken);
		}
	}
	
	private String genTempToken(String username) {
		String temp_token = UUID.randomUUID().toString().replaceAll("-", "");
		redisTemplate.opsForValue().set(RedisUtil.buildKey(temp_token), username);
		redisTemplate.expire(RedisUtil.buildKey(temp_token), ScToken.DEFAULT_TOKEN_EXPIRESECONDS, TimeUnit.SECONDS);
		return temp_token;
	}
	
	private String genRefreshToken(String username) {
		String refresh_token = UUID.randomUUID().toString().replaceAll("-", "");
		redisTemplate.opsForValue().set(RedisUtil.buildKey(refresh_token), username);
		redisTemplate.expire(RedisUtil.buildKey(refresh_token), ScToken.DEFAULT_REFRESH_EXPIREDAYS, TimeUnit.DAYS);
		
		redisTemplate.opsForHash().put(RedisUtil.buildUserRefreshTokenKey(username), refresh_token, username);
		redisTemplate.expire(RedisUtil.buildUserRefreshTokenKey(username), ScToken.DEFAULT_USERREFTOKEN_EXPIREDAYS, TimeUnit.DAYS);
		return refresh_token;
	}
}
