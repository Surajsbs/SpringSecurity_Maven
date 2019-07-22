package com.spring.security.demo.user.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.security.demo.vo.User;
import com.spring.security.demo.vo.UserRole;

/**
 * @author surajs
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private JdbcTemplate template;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = template.query(
				"select id, first_name, last_name, user_name, password from users where user_name = ?",
				new Object[] { username }, new ResultSetExtractor<User>() {

					@Override
					public User extractData(ResultSet rs) throws SQLException, DataAccessException {
						User users = null;
						if (rs.next()) {
							int id = rs.getInt("id");
							String fn = rs.getString("FIRST_NAME");
							String ln = rs.getString("LAST_NAME");
							String un = rs.getString("USER_NAME");
							String pw = rs.getString("PASSWORD");
							users = new User(id, fn, ln, un, pw);
						}
						return users;
					}
				});

		if (user != null) {
			System.out.println("User : '" + username + "' is found successfully");
			user.setGrantedAuthorities(getGrantedAuthorities(username));
			return new org.springframework.security.core.userdetails.User(user.getUser_name(), user.getPassword(), true,
					true, true, true, user.getGrantedAuthorities());
		}
		System.out.println("User : '" + username + "' is not found");
		throw new UsernameNotFoundException(username);
	}

	private List<GrantedAuthority> getGrantedAuthorities(String username) {
		List<UserRole> roles = template.query(
				"select r.id, r.role_name from users u inner join USER_ROLE ur on ur.user_id = u.id "
						+ "inner join ROLES r on r.id = ur.role_id where u.user_name = ?",
				new Object[] { username }, new RowMapper<UserRole>() {

					@Override
					public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new UserRole(rs.getString("role_name"), rs.getInt("id"));
					}
				});

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles.size());
		for (UserRole role : roles) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
		}
		System.out.println("Role are found for user : " + username + " as '" + roles + "'");
		return grantedAuthorities;
	}
}
