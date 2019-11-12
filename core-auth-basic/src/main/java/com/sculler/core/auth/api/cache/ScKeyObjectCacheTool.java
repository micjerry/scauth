package com.sculler.core.auth.api.cache;

import java.util.concurrent.TimeUnit;

public interface ScKeyObjectCacheTool {
	public void cache(ScAuthKeyType keyType, String key, ScAuthValueType valueType, Object value, int expires, TimeUnit unit);
	
	public Object get(ScAuthKeyType keyType, String key, ScAuthValueType valueType);
	
	public void clear(ScAuthKeyType keyType, String key, ScAuthValueType valueType);
	
	public void expire(ScAuthKeyType keyType, String key, ScAuthValueType valueType, int expires, TimeUnit unit);
}
