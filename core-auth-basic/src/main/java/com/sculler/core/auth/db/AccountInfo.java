package com.sculler.core.auth.db;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.sculler.core.auth.db.model.ScAccount;
import com.sculler.core.auth.db.repository.ScAccountRepository;
import com.sculler.core.auth.redis.RedisUtil;

@Component
public class AccountInfo {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ScAccountRepository scAccountRespository;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	public ScAccount getAccount(String username) {
		ScAccount account = (ScAccount)redisTemplate.opsForValue().get(RedisUtil.buildKey(username));
		if (null == account) {
			account = scAccountRespository.findByUsername(username);
			if (null == account) {
				logger.error("invalid account: {} not exist.", username);
				return null;
			}
			
			redisTemplate.opsForValue().set(RedisUtil.buildUserAccountKey(username), account);
			redisTemplate.expire(RedisUtil.buildUserAccountKey(username), 1, TimeUnit.DAYS);
			
		}	
		return account;
	}
	
}
