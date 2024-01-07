package com.ims.demo;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTest {
    public static void main(String[] args) {

        JwtBuilder jwtBuilder = Jwts.builder().setId("88").setSubject("zhen")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,"zhen");
        String token = jwtBuilder.compact();
        System.out.println(token);
    }
}
