package com.spring.security.demo.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.spring.security.demo.exception.handler.RestAccessDeniedHandler;
import com.spring.security.demo.exception.handler.RestAuthenticationEntryPoint;
import com.spring.security.demo.filter.AuthFilter;
import com.spring.security.demo.provider.AuthJdbcProvider;

/**
 * @author surajs
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthJdbcProvider jdbcAuthProvider;

	@Autowired
	private RestAuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private RestAccessDeniedHandler accessDeniedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.anonymous().disable().csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// http.cors().configurationSource(corsConfigSource());

		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "data/user/getAll", "data/user/get/**")
				.access("hasRole('ROLE_ADMIN') Or hasRole('ROLE_TEAM_LEADER') or hasRole('ROLE_BA') oR hasRole('ROLE_QE') OR hasRole('ROLE_DEVELOPER')")
			.antMatchers(HttpMethod.POST, "/data/user/create")
				.access("hasRole('ROLE_DEVELOPER') OR hasRole('ROLE_TEAM_LEADER') OR hasRole('ROLE_BA') OR hasRole('ROLE_QE')")
			.antMatchers(HttpMethod.POST, "data/user/assgin/**")
				.access("hasRole('ROLE_DEVELOPER') OR hasRole('ROLE_TEAM_LEADER') or hasRole('ROLE_QE')")
			.antMatchers(HttpMethod.PUT, "data/user/update/**")
				.access("hasRole('ROLE_DEVELOPER') OR hasRole('ROLE_TEAM_LEADER') OR hasRole('ROLE_BA')")
			.antMatchers(HttpMethod.PUT, "data/user/delete/**", "data/user/revoke/**")
				.access("hasRole('ROLE_ADMIN')")
			.antMatchers(HttpMethod.POST, "data/user/create")
				.access("hasRole('ROLE_TEAM_LEADER') OR hasRole('ROLE_BA') OR hasRole('ROLE_QE') OR hasRole('ROLE_DEVELOPER')");
		
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler)
			.authenticationEntryPoint(unauthorizedHandler).and()
			.addFilterBefore(new AuthFilter(authenticationManager()), BasicAuthenticationFilter.class);

		// http.authorizeRequests().antMatchers("/data/user/invalidate/**",
		// "/data/user/get/**").authenticated().and()
		// .addFilterBefore(new AuthFilter(authenticationManager()),
		// BasicAuthenticationFilter.class);
	}

	// @Bean
	// public CorsConfigurationSource corsConfigSource() {
	// CorsConfiguration configuration = new CorsConfiguration();
	// configuration.setAllowedOrigins(Arrays.asList("*"));
	// configuration.setAllowedMethods(Arrays.asList("*"));
	// configuration.setAllowCredentials(true);
	// UrlBasedCorsConfigurationSource source = new
	// UrlBasedCorsConfigurationSource();
	// source.registerCorsConfiguration("/**", configuration);
	// return source;
	// }

	@Override
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return new ProviderManager(Arrays.asList(jdbcAuthProvider));
	}
}