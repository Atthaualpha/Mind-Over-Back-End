package com.mindover.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.mindover.jwt.JwtAuthorizationFilter;
import com.mindover.jwt.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	protected static final String[] WHITE_LIST = { "/api-docs", "/api-docs/*", "/swagger-ui.html", "/swagger-ui/*",
			"/authentication/**", "/authentication/signin" };

	@Autowired
	private CustomSecurityUserDetailsService customUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private JwtAuthorizationFilter jwtFilter;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService);
	}
	
	@Bean
	public JwtAuthorizationFilter jwtFilter() {
		return jwtFilter;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf()
				.disable().authorizeRequests().antMatchers(WHITE_LIST).permitAll().anyRequest().authenticated().and()
				.addFilterAt(new AuthenticationFilter("/authentication/signin", authenticationManager(), jwtUtil),
						UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(jwtFilter(),UsernamePasswordAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
