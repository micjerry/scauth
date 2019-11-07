package com.sculler.core.auth.db.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sculler.core.auth.db.model.ScAccount;

public interface ScAccountRepository extends CrudRepository<ScAccount, Integer> {
	
	@Query(value = "select * from scaccounts b where b.username = ?1", nativeQuery = true)
	ScAccount findByUsername(String username);

}
