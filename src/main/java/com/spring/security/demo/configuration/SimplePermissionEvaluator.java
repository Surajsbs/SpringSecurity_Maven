package com.spring.security.demo.configuration;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author surajs
 *
 */
@Component
public class SimplePermissionEvaluator implements PermissionEvaluator {

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate template;

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if (authentication != null && targetDomainObject instanceof String) {
			if ("hasAccess".equalsIgnoreCase(String.valueOf(targetDomainObject))) {
				boolean hasAccess = hasPermission(authentication.getPrincipal().toString(), String.valueOf(permission));
				return hasAccess;
			}
		}
		return false;
	}

	private boolean hasPermission(String principal, String permission) {
		System.out.println("permission == " + permission);
		return retrivePermissions(principal).contains(permission.toUpperCase()) ? true : false;
	}

	private List<String> retrivePermissions(String principal) {
		String query = "select p.per_name, u.user_name from USER_ROLE ur inner join USERS u on ur.user_id = u.id "
				+ "inner join ROLES r on r.id = ur.role_id inner join ROLE_PERMISSION rp on r.id = rp.role_id "
				+ "inner join PERMISSION p on p.id = rp.permission_id where u.user_name  = ?";

		return template.query(query, new Object[] { principal }, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("per_name").toUpperCase();
			}
		});
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return false;
	}
}