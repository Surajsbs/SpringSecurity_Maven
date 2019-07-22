package com.spring.security.demo.vo;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class User {
	private int user_id;
	private String first_name;
	private String last_name;
	private String user_name;
	private String password;
	private List<GrantedAuthority> grantedAuthorities;

	public List<GrantedAuthority> getGrantedAuthorities() {
		return grantedAuthorities;
	}

	public void setGrantedAuthorities(List<GrantedAuthority> grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
	}

	public String getPassword() {
		return password;
	}

	public int getUser_id() {
		return user_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public User(int user_id, String first_name, String last_name, String user_name, String password) {
		super();
		this.user_id = user_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.user_name = user_name;
		this.password = password;
	}

	public User(int user_id, String first_name, String last_name, String user_name) {
		super();
		this.user_id = user_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.user_name = user_name;
	}

	public User(int user_id, String first_name, String last_name, String user_name, String password,
			List<GrantedAuthority> simpleGrantedAuthorityList) {
		super();
		this.user_id = user_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.user_name = user_name;
		this.password = password;
		this.grantedAuthorities = simpleGrantedAuthorityList;
	}

	@Override
	public String toString() {
		return "Users [user_id=" + user_id + ", first_name=" + first_name + ", last_name=" + last_name + ", user_name="
				+ user_name + ", password=" + password + ", grantedAuthorities=" + grantedAuthorities + "]";
	}

}
