package com.spring.security.demo.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.spring.security.demo.vo.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate template;

	@Override
	public Object getAll() {
		return template.query("select id, first_name, last_name, user_name from users", new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				int id = rs.getInt("id");
				String fn = rs.getString("first_name");
				String ln = rs.getString("last_name");
				String un = rs.getString("user_name");
				return new User(id, fn, ln, un);
			}
		});
	}

}
