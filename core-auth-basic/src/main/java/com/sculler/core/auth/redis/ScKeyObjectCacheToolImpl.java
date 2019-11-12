package com.sculler.core.auth.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.sculler.core.auth.api.cache.ScAuthKeyType;
import com.sculler.core.auth.api.cache.ScAuthValueType;
import com.sculler.core.auth.api.cache.ScKeyObjectCacheTool;

@Component
public class ScKeyObjectCacheToolImpl implements ScKeyObjectCacheTool{
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void cache(ScAuthKeyType keyType, String key, ScAuthValueType valueType, Object value, int expires,
			TimeUnit unit) {
		String cache_key = RedisUtil.buildCacheKey(keyType, key, valueType);
		redisTemplate.opsForValue().set(cache_key, value);
		redisTemplate.expire(cache_key, expires, unit);
		
	}

	@Override
	public Object get(ScAuthKeyType keyType, String key, ScAuthValueType valueType) {
		String cache_key =  RedisUtil.buildCacheKey(keyType, key, valueType);
		return redisTemplate.opsForValue().get(cache_key);
	}

	@Override
	public void clear(ScAuthKeyType keyType, String key, ScAuthValueType valueType) {
		String cache_key =  RedisUtil.buildCacheKey(keyType, key, valueType);
		redisTemplate.delete(cache_key);
	}

	@Override
	public void expire(ScAuthKeyType keyType, String key, ScAuthValueType valueType, int expires, TimeUnit unit) {
		String cache_key =  RedisUtil.buildCacheKey(keyType, key, valueType);
		redisTemplate.expire(cache_key, expires, unit);
	}

}
