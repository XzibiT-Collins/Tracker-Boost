package com.project.tracker.authentication.jwtService;

import com.project.tracker.exceptions.customExceptions.ExpiredJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

@Service
public class JwtService {

    private final Logger logger = Logger.getLogger(JwtService.class.getName());
    private final JwtConfig jwtConfig;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();

        SecretKey secreteKey = jwtConfig.getSecreteKey(); // retrieve secret key from config
        Date now = new Date(System.currentTimeMillis());
        Date exp = new Date(System.currentTimeMillis() + (60 * 60 * 1000 * 15)); //15 mins validation period

        logger.info("Generating token for user: " + email + "\n");
        //return generated token
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(now)
                .expiration(exp)
                .and()
                .signWith(secreteKey) //key generation
                .compact();
    }

    public String extractUserName(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(jwtConfig.getSecreteKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtTokenException("Token Expired: " + e.getMessage());
        }catch (Exception e){
            throw new JwtException(e.getMessage());
        }
        return claims;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
