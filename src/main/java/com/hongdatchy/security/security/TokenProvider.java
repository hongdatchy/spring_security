package com.hongdatchy.security.security;

import com.hongdatchy.security.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class TokenProvider {
	private static final String SECRET = Base64.getEncoder()
			.encodeToString("hongdatchy".getBytes());
	private static final long EXPIRED = 86400000;

	public String createToken(Authentication authentication) {
		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", myUserDetails.getUsername());
		claims.put("password", myUserDetails.getPassword());
		claims.put("role", authentication.getAuthorities());
		long time = System.currentTimeMillis();
		return Jwts.builder().setClaims(claims).setSubject(authentication.getName()).setIssuedAt(new Date(time))
				.setExpiration(new Date(time + EXPIRED)).signWith(SignatureAlgorithm.HS512, SECRET).compact();

	}

	@SuppressWarnings("unchecked")
    public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		User user = new User();
		user.setUsername(claims.get("username").toString());
		user.setPassword(claims.get("password").toString());
		List<LinkedHashMap<String, String>> linkedHashMaps = (List<LinkedHashMap<String, String>>) claims.get("role");
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (LinkedHashMap<String, String> linkedHashMap : linkedHashMaps) {
			authorities.add(new SimpleGrantedAuthority(linkedHashMap.get("authority")));
		}
		return new UsernamePasswordAuthenticationToken(user, token,authorities);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}