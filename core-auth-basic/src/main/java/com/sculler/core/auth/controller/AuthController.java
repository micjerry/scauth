package com.sculler.core.auth.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sculler.core.auth.api.digest.ScBasicDigest;
import com.sculler.core.auth.api.digest.ScToken;
import com.sculler.core.auth.pojo.LoginRequestPojo;
import com.sculler.core.auth.pojo.LoginResponsePojo;
import com.sculler.core.auth.pojo.LogoutRequestPojo;
import com.sculler.core.auth.pojo.RefreshTokenRequestPojo;
import com.sculler.core.auth.pojo.RefreshTokenResponsePojo;

@RestController
public class AuthController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String DIGEST_REALM_DOMAIN = "auth@scullerps.com";
	
	@Autowired
	private ScBasicDigest digest;
	
	@RequestMapping(value = { "/sc/core/auth/login" }, method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResponseEntity<LoginResponsePojo> login( @RequestBody LoginRequestPojo pojo,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
		if (!StringUtils.isBlank(authorization)) {
			ScToken token = digest.loginVerify(authorization);
			if (null == token) {
				logger.error("digest failed.");
				return new ResponseEntity<LoginResponsePojo>(null, null, HttpStatus.FORBIDDEN);
			}
			
			LoginResponsePojo response = new LoginResponsePojo();
			response.setUsername(token.getUsername());
			response.setRefreshtoken(token.getRefreshtoken());
			response.setToken(token.getToken());
			response.setExpires(token.getExpires());
			
			return new ResponseEntity<LoginResponsePojo>(response, null, HttpStatus.OK);
		} else {		
			if (null == pojo) {
				logger.error("invalid digest body.");
				return new ResponseEntity<LoginResponsePojo>(null, null, HttpStatus.FORBIDDEN);
			}
			if (StringUtils.isBlank(pojo.getUsername()) ||
					StringUtils.isBlank(pojo.getApp())) {
				return new ResponseEntity<LoginResponsePojo>(null, null, HttpStatus.BAD_REQUEST);
			}
			String challengeHeaderValue = digest.loginChallenge(pojo.getUsername(), DIGEST_REALM_DOMAIN);
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set(HttpHeaders.WWW_AUTHENTICATE, challengeHeaderValue);
			return new ResponseEntity<LoginResponsePojo>(null, responseHeaders, HttpStatus.UNAUTHORIZED);
		}
		
	}
	
	@RequestMapping(value = { "/sc/core/auth/logout" }, method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> logout(@RequestBody LogoutRequestPojo pojo) {
		if (null == pojo || StringUtils.isBlank(pojo.getRefreshtoken())) {
			logger.error("invalid logout body.");
			return new ResponseEntity<String>(null, null, HttpStatus.BAD_REQUEST);
		}
		
		digest.logout(pojo.getRefreshtoken());
		return new ResponseEntity<String>(null, null, HttpStatus.OK);
	}
	
	@RequestMapping(value = { "/sc/core/auth/refresh" }, method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<RefreshTokenResponsePojo> refresh(@RequestBody RefreshTokenRequestPojo pojo) {
		if (null == pojo || StringUtils.isBlank(pojo.getRefreshtoken())) {
			logger.error("invalid logout body.");
			return new ResponseEntity<RefreshTokenResponsePojo>(null, null, HttpStatus.BAD_REQUEST);
		}
		
		ScToken token = digest.refresh(pojo.getRefreshtoken());
		
		if (null == token) {
			logger.error("refresh failed.");
			return new ResponseEntity<RefreshTokenResponsePojo>(null, null, HttpStatus.FORBIDDEN);
		}
		
		RefreshTokenResponsePojo response = new RefreshTokenResponsePojo();
		response.setToken(token.getToken());
		response.setExpires(token.getExpires());
		
		return new ResponseEntity<RefreshTokenResponsePojo>(response, null, HttpStatus.OK);
	}
	
	@RequestMapping(value = { "/sc/core/auth/authentication" })
	public ResponseEntity<String> authentication(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
		if (StringUtils.isBlank(auth)) {
			logger.error("invalid request no Authorization header.");
			return new ResponseEntity<String>(null, null, HttpStatus.UNAUTHORIZED);
		}
		
		String basic_token = auth.substring(6);
		String decode_token = new String(Base64.getDecoder().decode(basic_token), StandardCharsets.UTF_8);
		String[] tokens = decode_token.split(":");
		if (tokens.length < 2 ) {
			logger.error("invalid Authorization header.");
			return new ResponseEntity<String>(null, null, HttpStatus.FORBIDDEN);
		}
		String username = digest.authentication(tokens[0]);
		
		if (null == username) {
			logger.error("can not uthorization: {}.", auth);
			return new ResponseEntity<String>(null, null, HttpStatus.FORBIDDEN);
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set(ScBasicDigest.HEADER_FORWARDED_USER, username);
		return new ResponseEntity<String>(null, responseHeaders, HttpStatus.OK);
	}

}
