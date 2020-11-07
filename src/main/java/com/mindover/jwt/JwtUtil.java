package com.mindover.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindover.util.SecurityConstantsEnum;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtil {
	
	protected static final Log logger = LogFactory.getLog(JwtUtil.class);
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	/**
	 * Add the token to header and body response
	 * @param res
	 * @param auth
	 * @throws IOException
	 */
	public void addTokenToResponse(HttpServletResponse res, Authentication auth) throws IOException {
		String token = this.generateToken(auth.getName(), auth.getAuthorities());
		
		token = SecurityConstantsEnum.AUTHORIZATION_PREFIX.getValue() + " " + token;
		
		Map<String, Object> data = new HashMap<>();
		data.put("token", token);
		
		res.setContentType(MediaType.APPLICATION_JSON_VALUE);
		res.addHeader(SecurityConstantsEnum.AUTHORIZATION.getValue(),token);
		res.getOutputStream().println(new ObjectMapper().writeValueAsString(data));
	}

	/**
	 * Generate a new token
	 * @param username
	 * @param authorityList
	 * @return new token
	 */
	public String generateToken(String username, Collection<? extends GrantedAuthority> authorityList) {
		return Jwts.builder().setSubject(username).
				setExpiration(new Date(System.currentTimeMillis() + (60000 * 30)))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}
	
	/**
	 * 
	 * @param jwtToken
	 * @return claim with token info
	 */
	public void validateToken(String jwtToken) {
		
		
		String token = Strings.EMPTY;
		
		try {
			token = jwtToken.replace(SecurityConstantsEnum.AUTHORIZATION_PREFIX.getValue(), Strings.EMPTY).trim();
			Claims claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
			
			authorizeUser(claim);
			
		} catch (ExpiredJwtException exception) {
			logger.warn("Request to parse expired JWT failed : " + token + " " + exception.getMessage());
		} catch (UnsupportedJwtException exception) {
			logger.warn("Request to parse unsupported JWT failed : " + token + " " + exception.getMessage());
		} catch (MalformedJwtException exception) {
			logger.warn("Request to parse invalid formatted JWT failed : " + token + " " + exception.getMessage());
		} catch (SignatureException exception) {
			logger.warn("Request to parse invalid signature JWT failed : " + token + " " + exception.getMessage());
		} catch (IllegalArgumentException exception) {
			logger.warn("Request to parse invalid JWT failed : " + token + " " + exception.getMessage());
		}

	}
	
	/**
	 * Authorize the user in spring authentication flow
	 * 
	 * @param claim
	 */
	private void authorizeUser(Claims claim) {

//		Collection<SimpleGrantedAuthority> authorities = Arrays
//				.stream(claim.get(CommonConstantsEnum.AUTHORITIES.getValue()).toString().split(","))
//				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claim.getSubject(),
				Strings.EMPTY, Collections.emptyList());

		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	
}
