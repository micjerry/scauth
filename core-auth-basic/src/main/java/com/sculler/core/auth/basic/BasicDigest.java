package com.sculler.core.auth.basic;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.sculler.core.auth.db.AccountInfo;
import com.sculler.core.auth.db.model.ScAccount;
import com.sculler.core.auth.redis.RedisUtil;

@Component
public class BasicDigest {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	@Autowired
	private AccountInfo accountInfo;
	
	@Autowired
	private ScTokenTool tokenTool;
	
	public String makeChallenge(String username) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		redisTemplate.opsForValue().set(RedisUtil.buildKey(uuid), username);
		redisTemplate.expire(RedisUtil.buildKey(uuid), 30, TimeUnit.SECONDS);
		return BaseAuthRfc.makeDigest(uuid);
	}
	
	public ScToken verifyChallenge(BasicDigestInfo digest) {
		if (null == digest) {
			logger.error("invalid digest.");
			return null;
		}
		
		String nonce = digest.getNonce();
		String username = redisTemplate.opsForValue().get(RedisUtil.buildKey(nonce));
		
		if (StringUtils.isBlank(username)) {
			logger.error("no challenge for digest username: {}", digest.getUsername());
			return null;
		}
		
		if (!username.equals(digest.getUsername())) {
			logger.error("invalid username challenge for£º {} response: {}", username, digest.getUsername());
		}
		
		ScAccount account = accountInfo.getAccount(username);
		
		if (null == account) {
			logger.error("no account for username: {}", username);
			return null;
		}
		
		String ha1 = DigestUtils.md5Hex(digest.getUsername() + ":" + digest.getRealm() + ":" + account.getPassword());
		String serverResponse = DigestUtils.md5Hex(ha1 + ":" + digest.getNonce());
		
		if (!serverResponse.equals(digest.getResponse())) {
			logger.error("digest failed client: {} server: {} not match", digest.getResponse(), serverResponse);
			return null;
		}
		
		ScToken token = tokenTool.createToken(username);
		
		return token;
	}
	
	public void logout(String refreshToken) {
		tokenTool.clearRefreshToken(refreshToken);
	}
	
	public ScToken refresh(String refreshToken) {
		return tokenTool.refreshToken(refreshToken);
	}
	
	public String authentication(String token) {
		return tokenTool.identifyToken(token);
	}
}
