package com.ims.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Data
@ConfigurationProperties("jwt.config")
public class JwtUtils {
    private String key;
    private long ttl;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public long getTtl() {
        return ttl;
    }
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
    /**
     * 签发 token
     */
    public String createJwt(String id, String username, Map<String,Object> map){
        long now=System.currentTimeMillis();
        long exp=now+ttl;
        JwtBuilder jwtBuilder = Jwts.builder().setId(id)
                .setSubject(username).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key);
        jwtBuilder.addClaims(map);
        jwtBuilder.setExpiration(new Date(exp));
        String token = jwtBuilder.compact();
        return token;
    }
    public Claims parseJwt(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        }catch (Exception e){
        }
        return claims;
    }


}
