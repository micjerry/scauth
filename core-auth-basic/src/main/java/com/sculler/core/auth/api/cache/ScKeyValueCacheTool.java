package com.sculler.core.auth.api.cache;

import java.util.concurrent.TimeUnit;

public interface ScKeyValueCacheTool {
	public void cache(ScAuthKeyType keyType, String key, ScAuthValueType valueType, String value, int expires, TimeUnit unit);
	
	public String get(ScAuthKeyType keyType, String key, ScAuthValueType valueType);
	
	public void clear(ScAuthKeyType keyType, String key, ScAuthValueType valueType);
	
	public void expire(ScAuthKeyType keyType, String key, ScAuthValueType valueType, int expires, TimeUnit unit);
}
