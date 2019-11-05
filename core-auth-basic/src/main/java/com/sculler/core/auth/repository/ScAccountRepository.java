package com.sculler.core.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.sculler.core.auth.model.ScAccount;

public interface ScAccountRepository extends CrudRepository<ScAccount, Integer> {

}
