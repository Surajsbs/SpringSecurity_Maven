package com.spring.security.demo.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @author surajs
 *
 */
@Component
public class AuthJdbcProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailService;

	/*
	 * Responsible to authenticate a user using it's credentials
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		UserDetails user = userDetailService.loadUserByUsername(username);
		if (!user.getPassword().equals(password)) {
			System.out.println("Received invalid user credentials");
			throw new BadCredentialsException("Invalid user credentials");
		}
		System.out.println("User : '" + username + "' is authenticated successfully");
		return new UsernamePasswordAuthenticationToken(username, "", user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}