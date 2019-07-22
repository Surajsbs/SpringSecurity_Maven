package com.spring.security.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.demo.vo.AuthorizationResponse;

/**
 * @author surajs
 *
 */
public final class AuthFilter extends GenericFilterBean {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private AuthenticationManager authenticationManager;

	public AuthFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		response.setContentType("application/json");

		String username = request.getHeader("username");
		String password = request.getHeader("password");

		if (username == null || password == null) {
			logger.error("Received invalid header");
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			AuthorizationResponse customeResponse = new AuthorizationResponse(HttpStatus.BAD_REQUEST.value(),
					"Received invalid request");
			response.getWriter().print(convertObjectToJson(customeResponse));
			return;
		} else {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
			try {
				Authentication responseAuth = authenticationManager.authenticate(authToken);
				if (responseAuth == null || !responseAuth.isAuthenticated()) {
					logger.error("User not able to authenticated with principal: {} ", authToken.getPrincipal());
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().print(convertObjectToJson("Invalid - 1"));
					return;
				}
				SecurityContextHolder.getContext().setAuthentication(responseAuth);
			} catch (AuthenticationException e) {
				logger.error("User not able to authenticated with principal: {} ", authToken.getPrincipal());
				response.setStatus(HttpStatus.FORBIDDEN.value());
				AuthorizationResponse customeResponse = new AuthorizationResponse(HttpStatus.FORBIDDEN.value(),
						"Invalid credentials");
				response.getWriter().print(convertObjectToJson(customeResponse));
				return;
			} catch (Exception e) {
				logger.error("Invalid request received with principal: {} ", authToken.getPrincipal());
				logger.error("Exception : {}", e);
				response.setStatus(HttpStatus.FORBIDDEN.value());
				AuthorizationResponse customeResponse = new AuthorizationResponse(HttpStatus.FORBIDDEN.value(),
						"Invalid request received with principal: " + authToken.getPrincipal());
				response.getWriter().print(convertObjectToJson(customeResponse));
				return;
			}
		}
		filterChain.doFilter(req, res);
	}

	/**
	 * @param object
	 * @return String
	 * @throws JsonProcessingException
	 *             Convert object into JSON format
	 */
	private String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
}
