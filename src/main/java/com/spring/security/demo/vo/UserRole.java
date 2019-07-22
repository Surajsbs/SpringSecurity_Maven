package com.spring.security.demo.vo;

/**
 * @author surajs
 *
 */
public class UserRole {
	private String role;
	private int id;

	public String getRole() {
		return role;
	}

	public int getId() {
		return id;
	}

	public UserRole(String role, int id) {
		super();
		this.role = role;
		this.id = id;
	}

	@Override
	public String toString() {
		return role;
	}

}