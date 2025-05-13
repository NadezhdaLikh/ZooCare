package com.spring.zoocare.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "450WBZeNp2ayCqdddPHPmuqTe8JquNMF5x1vNsqUWpxTcjsOe6btxhY23tlIOSUh";

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signs the constructed JWT with the specified key using the specified algorithm, producing a JWS.
                .compact(); // Returns a compact URL-safe JWT string
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Claims::getSubject returns the JWT sub (subject) value or null if not present.
    }

    public <T> T extractClaim(String  token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey()) // Sets the signing key used to verify any discovered JWS digital signature.
                .build() // Builds the JwtParser instance after setting any necessary configuration (like the signing key).
                .parseClaimsJws(token) // A Claims JWS is a JWT with a Claims body that has been cryptographically signed.
                .getBody(); // Returns the JWT body, either a String or a Claims instance.
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes); // Creates a new SecretKey instance for use with HMAC-SHA algorithms based on the specified key byte array.
    }
}


