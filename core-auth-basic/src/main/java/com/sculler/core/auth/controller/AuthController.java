package com.sculler.core.auth.controller;

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

import com.sculler.core.auth.basic.ScDigest;
import com.sculler.core.auth.basic.ScToken;
import com.sculler.core.auth.pojo.AuthRequestPojo;
import com.sculler.core.auth.pojo.BaseAuthRfc;
import com.sculler.core.auth.pojo.BasicDigestInfo;

@RestController
public class AuthController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ScDigest digest;
	
	@RequestMapping(value = { "/sc/core/auth/login" }, method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResponseEntity<ScToken> login( @RequestBody AuthRequestPojo pojo,
			@RequestHeader("Authorization") String authorization) {
		if (StringUtils.isBlank(authorization)) {
			BasicDigestInfo digestInfo = BaseAuthRfc.parseAuthrization(authorization);
			if (null == digestInfo) {
				logger.error("invalid digest header.");
				return new ResponseEntity<ScToken>(null, null, HttpStatus.FORBIDDEN);
			}
			ScToken token = digest.verifyChallenge(digestInfo);
			if (null == token) {
				logger.error("digest failed.");
				return new ResponseEntity<ScToken>(null, null, HttpStatus.FORBIDDEN);
			}
			
			return new ResponseEntity<ScToken>(token, null, HttpStatus.OK);
		} else {		
			if (StringUtils.isBlank(pojo.getUsername()) ||
					StringUtils.isBlank(pojo.getApp())) {
				return new ResponseEntity<ScToken>(null, null, HttpStatus.BAD_REQUEST);
			}
			String challengeHeaderValue = digest.makeChallenge(pojo.getUsername());
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set(BaseAuthRfc.WWW_AUTHENTICATE, challengeHeaderValue);
			return new ResponseEntity<ScToken>(null, responseHeaders, HttpStatus.OK);
		}
		
	}

}
