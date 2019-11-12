package com.sculler.core.auth.api.cache;

public enum ScAuthValueType {
	USERNAME_VALUE("un"),
	TOKEN_VALUE("tk"),
	REFRESH_TOKEN_VALUE("rk"),
	ACCOUNT_VALUE("ac");
	
	private String value;
	
	ScAuthValueType(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
