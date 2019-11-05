package com.sculler.core.auth.pojo;

public class BaseAuthRfc {
	public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
	public static final String WWW_REALM_KEY = "Digest realm";
	public static final String WWW_REALM_CONTENT = "auth@scullerps.com";
	public static final String WWW_NONCE_KEY = "nonce";
	
	public static final String WWW_DIGEST_USERNAME = "Digest username";
	public static final String WWW_DIGEST_RESPONSE = "response";
	
	public static String makeDigest(String nonce) 
	{
		StringBuilder strb = new StringBuilder(WWW_REALM_KEY);
		strb.append("=");
		strb.append("\"");
		strb.append(WWW_REALM_CONTENT);
		strb.append("\"");
		strb.append(",");
		strb.append(WWW_NONCE_KEY);
		strb.append("=");
		strb.append("\"");
		strb.append(nonce);
		strb.append("\"");
		return strb.toString();
	}
	
}
