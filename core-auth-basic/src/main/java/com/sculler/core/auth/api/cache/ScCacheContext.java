package com.sculler.core.auth.api.cache;

public interface ScCacheContext {
	public static final String CACHE_PREFIX = "auth_";
	
	public static final int EXPIRETIME_TOKEN_SECONDS = 1800;
	public static final int EXPIRETIME_REFRESHTOKEN_DAYS = 7;
	public static final int EXPIRETIME_USERREFTOKENS_DAYS = 15;
	public static final int EXPIRETIME_TOKENDROPED_SECONDS = 5;
	public static final int EXPIRETIME_ACCOUNT_DAYS = 10;
	public static final int EXPIRETIME_NONCE_SECONDS = 30;
}
