package com.sculler.core.auth.api.digest;

public interface ScBasicDigest {
	public static final String HEADER_FORWARDED_USER = "X-Forwarded-User";
	
	public String loginChallenge(String username, String realm);
	
	public ScToken loginVerify(String authrization);
	
	public void logout(String refreshToken);
	
	public ScToken refresh(String refreshToken);
	
	public String authentication(String baseToken);
}
