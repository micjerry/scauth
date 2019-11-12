package com.sculler.core.auth.redis;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.sculler.core.auth.api.cache.ScAuthKeyType;
import com.sculler.core.auth.api.cache.ScAuthValueType;
import com.sculler.core.auth.api.cache.ScSetCacheTool;

@Component
public class ScSetCacheToolImpl implements ScSetCacheTool {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void add(ScAuthKeyType keyType, String key, ScAuthValueType valueType, String value, int expires,
			TimeUnit unit) {
		String cache_key = RedisUtil.buildCacheKey(keyType, key, valueType);
		redisTemplate.opsForSet().add(cache_key, value);
		redisTemplate.expire(cache_key, expires, unit);
		
	}

	@Override
	public void remove(ScAuthKeyType keyType, String key, ScAuthValueType valueType, String value) {
		String cache_key = RedisUtil.buildCacheKey(keyType, key, valueType);
		redisTemplate.opsForSet().remove(cache_key, value);
	}

	@Override
	public Set<String> get(ScAuthKeyType keyType, String key, ScAuthValueType valueType) {
		String cache_key = RedisUtil.buildCacheKey(keyType, key, valueType);
		return redisTemplate.opsForSet().members(cache_key);
	}

	@Override
	public void clear(ScAuthKeyType keyType, String key, ScAuthValueType valueType) {
		String cache_key = RedisUtil.buildCacheKey(keyType, key, valueType);
		redisTemplate.delete(cache_key);
	}

}
