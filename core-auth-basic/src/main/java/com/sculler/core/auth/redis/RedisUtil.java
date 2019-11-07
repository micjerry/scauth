package com.sculler.core.auth.redis;

public class RedisUtil {
	private static final String PREFIX = "auth_";
	
	public static final String USERNAME_REFRESHTOKEN = "u2r";
	public static final String USERNAME_ACCOUNT = "u2a";
	
	public static String buildKey(String key) {
		return PREFIX + key;
	}
	
	public static String buildUserAccountKey(String username) {
		return PREFIX + username + USERNAME_ACCOUNT;
	}
	
	public static String buildUserRefreshTokenKey(String username) {
		return PREFIX + username + USERNAME_REFRESHTOKEN;
	}
}
