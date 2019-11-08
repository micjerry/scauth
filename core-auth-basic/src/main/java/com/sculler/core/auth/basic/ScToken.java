package com.sculler.core.auth.basic;

public class ScToken {
	static final int DEFAULT_TOKEN_EXPIRESECONDS = 1800;
	
	static final int DEFAULT_REFRESH_EXPIREDAYS = 7;
	
	static final int DEFAULT_USERREFTOKEN_EXPIREDAYS = 15;
	
	private String username;
	
	private String token;
	
	private String refreshtoken;
	
	private int expires;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshtoken() {
		return refreshtoken;
	}

	public void setRefreshtoken(String refreshtoken) {
		this.refreshtoken = refreshtoken;
	}

	public int getExpires() {
		return expires;
	}

	public void setExpires(int expires) {
		this.expires = expires;
	}
}
