package com.sculler.core.auth.api.cache;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface ScSetCacheTool {
	public void add(ScAuthKeyType keyType, String key, ScAuthValueType valueType, String value, int expires, TimeUnit unit);
	
	public void remove(ScAuthKeyType keyType, String key, ScAuthValueType valueType,  String value);
	
	public Set<String> get(ScAuthKeyType keyType, String key, ScAuthValueType valueType);
	
	public void clear(ScAuthKeyType keyType, String key, ScAuthValueType valueType);
}
