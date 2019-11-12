package com.sculler.core.auth.api.cache;

public enum ScAuthKeyType {
	USERNAME_KEY("un"),
	TOKEN_KEY("tk"),
	REFRESH_TOKEN_KEY("rk"),
	NONCE_KEY("nc");
	
	private String key;
	
	ScAuthKeyType(String key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		return key;
	}
}
