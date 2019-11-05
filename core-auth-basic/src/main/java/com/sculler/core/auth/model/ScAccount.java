package com.sculler.core.auth.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="scaccounts")
public class ScAccount {
	@Id
	@Size(max = 32)
	private String username;
	
	@NotNull
	@Size(max = 32)
	private String password;
	
	@Size(max = 32)
	private String permits;
	
	@Size(max = 32)
	private String email;
	
	@Size(max = 32)
	private String mobilephone;
	
	private Timestamp createtime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPermits() {
		return permits;
	}

	public void setPermits(String permits) {
		this.permits = permits;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
}
