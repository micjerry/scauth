package com.sculler.core.auth.db;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sculler.core.auth.api.cache.ScAuthKeyType;
import com.sculler.core.auth.api.cache.ScAuthValueType;
import com.sculler.core.auth.api.cache.ScCacheContext;
import com.sculler.core.auth.api.cache.ScKeyObjectCacheTool;
import com.sculler.core.auth.db.model.ScAccount;

@Component
class ScAccountCache {
	@Autowired
	private ScKeyObjectCacheTool cacheTool;

	ScAccount getAccount(String username) {
		return (ScAccount) cacheTool.get(ScAuthKeyType.USERNAME_KEY, username, ScAuthValueType.ACCOUNT_VALUE);
	}

	void cacheAccount(String username, ScAccount account) {
		cacheTool.cache(ScAuthKeyType.USERNAME_KEY, username, ScAuthValueType.ACCOUNT_VALUE, account,
				ScCacheContext.EXPIRETIME_ACCOUNT_DAYS, TimeUnit.DAYS);
	}
}
