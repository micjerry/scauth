package com.sculler.core.auth.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sculler.core.auth.db.model.ScAccount;
import com.sculler.core.auth.db.repository.ScAccountRepository;

@Component
public class AccountInfo {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ScAccountRepository scAccountRespository;
	
	@Autowired
	private ScAccountCache cacheTool;
	
	public ScAccount getAccount(String username) {
		ScAccount account = cacheTool.getAccount(username);
		if (null == account) {
			account = scAccountRespository.findByUsername(username);
			if (null == account) {
				logger.error("invalid account: {} not exist.", username);
				return null;
			}
			
			cacheTool.cacheAccount(username, account);			
		}	
		return account;
	}
	
}
