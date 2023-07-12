package com.citse.kunduApp.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    /**
     * Description: methods for generate token time life and extract user info
     * author: @Omar*/

    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    public String extractUsername(String TOKEN) {
        return extractClaim(TOKEN,Claims::getSubject);
    }

    public <T> T extractClaim(String TOKEN, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(TOKEN);
        return claimResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String,Object> extractClaims,
            UserDetails userDetails
    ){
        return buildToken(extractClaims,userDetails,jwtExpiration);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }
    public String buildToken(
            Map<String,Object> extractClaims,
            UserDetails userDetails,
            long expiration
    ){
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String TOKEN, UserDetails userDetails){
        final String username = extractUsername(TOKEN);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(TOKEN);
    }

    private boolean isTokenExpired(String TOKEN) {
        return extractTokenExpiration(TOKEN).before(new Date());
    }

    private Date extractTokenExpiration(String TOKEN) {
        return extractClaim(TOKEN,Claims::getExpiration);
    }

    private Claims extractAllClaims(String TOKEN){
        return Jwts.parserBuilder().setSigningKey(getSignKey())
                .build().parseClaimsJws(TOKEN).getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
