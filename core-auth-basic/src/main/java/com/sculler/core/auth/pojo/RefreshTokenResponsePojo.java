package com.sculler.core.auth.pojo;

public class RefreshTokenResponsePojo {
	private String token;
	
	private int expires;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getExpires() {
		return expires;
	}

	public void setExpires(int expires) {
		this.expires = expires;
	}
}
