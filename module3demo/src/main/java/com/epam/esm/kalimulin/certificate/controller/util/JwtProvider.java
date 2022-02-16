package com.epam.esm.kalimulin.certificate.controller.util;

import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.JWT_PROPERTY_FILE;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.JWT_SECRET;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.JWT_EXPIRATION_MS;

import java.util.Date;
import java.util.ResourceBundle;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.epam.esm.kalimulin.certificate.bean.user.UserPrincipal;
import com.epam.esm.kalimulin.certificate.controller.exception.JwtException;
import com.epam.esm.kalimulin.certificate.service.util.TextEncryptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {
	
    private final ResourceBundle jwtPropBundle = ResourceBundle.getBundle(JWT_PROPERTY_FILE);			
    private final String jwtSecret = TextEncryptor.decrypt(jwtPropBundle.getString(JWT_SECRET));
    private final int jwtExpirationInMs = Integer.valueOf(jwtPropBundle.getString(JWT_EXPIRATION_MS));
    
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
            .setSubject(userPrincipal.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }
    
    public String getUserUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }
    
    public boolean validateToken(String authToken) throws JwtException {
        try {
            Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException 
        		| MalformedJwtException 
        		| ExpiredJwtException 
        		| UnsupportedJwtException 
        		| IllegalArgumentException exception) { 		
        	throw new JwtException(exception);
        } 
    }
}
