package com.infy.jwt.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JWTService {

	public static final String SECRET="5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
		final Claims claims=extractClaimes(token);
		return claimResolver.apply(claims);
	}

	private Claims extractClaimes(String token) {
		 Claims claims = Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		 log.info(" From extractedClaims method {} and token is {}",claims, token);
		 
		 return claims;
	}
	
	private Boolean isTokenExpired(String token) {
		Date extractExpiration = extractExpiration(token);
		Date currentDatetime=new Date();
		boolean before = extractExpiration(token).before(new Date());
		log.info("From isTokenExpirationMethod expirationDate {} and CurentDate {} and boolean value is {}",extractExpiration, currentDatetime, before);
		return before;
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username= extractUsername(token);
		return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
	}
	
	public String generateToken(String username) {
		Map<String, Object> claims=new HashMap<>();
		return createToken(claims, username);
	}

	private String createToken(Map<String, Object> claims, String username) {
		 String token = Jwts
				.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*1))
				.signWith(getSignKey(), SignatureAlgorithm.ES256)
				.compact();
		 log.info("generate token is {} with username {}",token, username);
		 return token;
	}
	
	private Key getSignKey() {
		byte[] keyBytes=Decoders.BASE64.decode(SECRET);
		log.info("keybytes are {}",keyBytes);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
