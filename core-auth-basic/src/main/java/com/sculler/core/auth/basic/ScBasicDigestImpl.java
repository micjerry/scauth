package com.sculler.core.auth.basic;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sculler.core.auth.api.digest.ScBasicDigest;
import com.sculler.core.auth.api.digest.ScToken;
import com.sculler.core.auth.db.AccountInfo;
import com.sculler.core.auth.db.model.ScAccount;

@Component
class ScBasicDigestImpl implements ScBasicDigest{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AccountInfo accountInfo;
	
	@Autowired
	private ScTokenTool tokenTool;
	
	@Override
	public String loginChallenge(String username, String realm) {
		String nonce = tokenTool.createNonce(username);
		return BaseAuthRfc.makeDigest(nonce, realm);
	}
	
	@Override
	public ScToken loginVerify(String authrization) {
		BasicDigestInfo digest = BaseAuthRfc.parseAuthrization(authrization);
		if (null == digest || StringUtils.isBlank(digest.getNonce())
				|| StringUtils.isBlank(digest.getResponse())
				|| StringUtils.isBlank(digest.getUsername())) {
			logger.error("invalid digest.");
			return null;
		}
		
		if (!tokenTool.identifyNonce(digest.getNonce(), digest.getUsername())) {
			logger.error("invalid username challenge for£º {} ", digest.getUsername());
		}
		
		ScAccount account = accountInfo.getAccount(digest.getUsername());
		
		if (null == account) {
			logger.error("no account for username: {} ", digest.getUsername());
			return null;
		}
		
		if (account.getForbid() == 1) {
			logger.error("account is forbid for username: {} ", digest.getUsername());
			return null;
		}
		
		String ha1 = DigestUtils.md5Hex(digest.getUsername() + ":" + digest.getRealm() + ":" + account.getPassword());
		String serverResponse = DigestUtils.md5Hex(ha1 + ":" + digest.getNonce());
		
		if (!serverResponse.equals(digest.getResponse())) {
			logger.error("digest failed client: {} server: {} not match", digest.getResponse(), serverResponse);
			return null;
		}
		
		ScToken token = tokenTool.createToken(digest.getUsername());
		
		return token;
	}
	
	@Override
	public void logout(String refreshToken) {
		tokenTool.clearRereshToken(refreshToken);
	}
	
	@Override
	public ScToken refresh(String refreshToken) {
		return tokenTool.refreshToken(refreshToken);
	}
	
	@Override
	public String authentication(String token) {
		return tokenTool.identifyToken(token);
	}
}
