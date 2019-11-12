package com.sculler.core.auth.redis;

import com.sculler.core.auth.api.cache.ScAuthKeyType;
import com.sculler.core.auth.api.cache.ScAuthValueType;
import com.sculler.core.auth.api.cache.ScCacheContext;

public class RedisUtil {
	static String buildCacheKey(ScAuthKeyType keyType, String key, ScAuthValueType valueType) {
		return ScCacheContext.CACHE_PREFIX + key + keyType + valueType;
	}
}
