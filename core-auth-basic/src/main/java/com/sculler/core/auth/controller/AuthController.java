package com.sculler.core.auth.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sculler.core.auth.pojo.AuthRequestPojo;
import com.sculler.core.auth.pojo.BaseAuthRfc;

@RestController
public class AuthController {
	
	@RequestMapping(value = { "/ps/core/auth/login" }, method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResponseEntity<String> login( @RequestBody AuthRequestPojo pojo) { 
		HttpHeaders responseHeaders = new HttpHeaders();
		
		if (StringUtils.isBlank(pojo.getUsername()) ||
				StringUtils.isBlank(pojo.getApp())) {
			return new ResponseEntity<String>(null, responseHeaders, HttpStatus.BAD_REQUEST);
		}
		
		responseHeaders.set(BaseAuthRfc.WWW_AUTHENTICATE, "");
		
		return new ResponseEntity<String>(null, responseHeaders, HttpStatus.OK);
	}

}
