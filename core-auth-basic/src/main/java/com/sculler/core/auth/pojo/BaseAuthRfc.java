package com.sculler.core.auth.pojo;

import org.apache.commons.lang3.StringUtils;

public class BaseAuthRfc {
	public static final String AUTHORIZATION = "Authorization";
	public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
	public static final String DIGEST_NAME = "Digest ";
	public static final String DIGEST_REALM_KEY = "realm";
	public static final String DIGEST_REALM_DOMAIN = "auth@scullerps.com";
	
	public static final String DIGEST_NONCE_KEY = "nonce";
	public static final String DIGEST_USERNAME_KEY = "username";
	public static final String DIGEST_RESPONSE_KEY = "response";
	
	public static String makeDigest(String nonce) 
	{
		StringBuilder strb = new StringBuilder(DIGEST_NAME);
		strb.append(DIGEST_REALM_KEY);
		strb.append("=");
		strb.append("\"");
		strb.append(DIGEST_REALM_DOMAIN);
		strb.append("\"");
		strb.append(",");
		strb.append(DIGEST_NONCE_KEY);
		strb.append("=");
		strb.append("\"");
		strb.append(nonce);
		strb.append("\"");
		return strb.toString();
	}
	
	public static BasicDigestInfo parseAuthrization(String authorization)
	{
		BasicDigestInfo digest = new BasicDigestInfo();
		String[] authes = authorization.split(",");
		
		if (StringUtils.isBlank(authorization))
			return null;
		
		for (String auth: authes) {
			String[] keyvalue = auth.split("=");
			if (keyvalue.length != 2)
				return null;
			
			String key = keyvalue[0].trim();
			String value = keyvalue[1].trim().replaceAll("\"", "");
			
			if (StringUtils.isBlank(value))
				return null;
			
			if (digest.getUsername() == null && key.contains(DIGEST_USERNAME_KEY)) {
				digest.setUsername(value);
				continue;
			}
			
			if (digest.getNonce() == null && key.contains(DIGEST_NONCE_KEY)) {
				digest.setNonce(value);
				continue;
			}
			
			if (digest.getResponse() == null && key.contains(DIGEST_RESPONSE_KEY)) {
				digest.setResponse(value);
				continue;
			}
			
			if (digest.getRealm() == null && key.contains(DIGEST_REALM_KEY)) {
				digest.setRealm(value);
				continue;
			}
		}
		
		if (digest.getNonce() == null || digest.getRealm() == null ||
				digest.getUsername() == null || digest.getResponse() == null) {
			return null;
		}
		
		return digest;
	}
	
}
