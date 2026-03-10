package org.mynthon.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.mynthon.dto.TokenResponse;
import java.time.Instant;

@ApplicationScoped
public class SecurityService {

     public TokenResponse saveToken(String login,Integer connectionId){
         return new TokenResponse(generateToken(login,connectionId));
     }

     private String generateToken(String login, Integer connectionId){
         return Jwt.claims()
                 .issuer("your-issuer")
                 .subject(login)
                 .claim("connectionId", connectionId)
                 .issuedAt(Instant.now())
                 .expiresAt(Instant.now().plusSeconds(3600))
                 .sign();
     }
}
