package com.project.tracker.authentication.jwtService;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Value("${JWT_SECRETE_KEY}") //loads raw key from .env file
    private String secreteKeyRaw;

    private SecretKey secreteKeyEncoded;

    @PostConstruct
    public void init(){
        byte[] keyBytes = Base64.getDecoder().decode(secreteKeyRaw);
        //encode the secrete key using HMACK SHA256
        this.secreteKeyEncoded = Keys.hmacShaKeyFor(keyBytes);
    }

    //returns secret key on demand
    public SecretKey getSecreteKey(){
        return this.secreteKeyEncoded;
    }
}
