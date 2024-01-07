package com.ims.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class ParseJwt {
    public static void main(String[] args) {
        String
                token="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDYzNzA1NDgyOTM5NzMxOTY4Iiwic3ViIjoiY2d4IiwiaWF0IjoxNzA1MDkzNzc1LCJjb21wYW55SWQiOiIiLCJhcGlzIjoiIiwiY29tcGFueU5hbWUiOiIiLCJleHAiOjE3MDUwOTQxMzV9.rclqid88HlSNb9hEsFxOeByZ6mlHSjb27I19bdIGjp4";
        Claims claims =
                Jwts.parser().setSigningKey("LZY-ims").parseClaimsJws(token).getBody();
        System.out.println("id:"+claims.getId());
        System.out.println("subject:"+claims.getSubject());
        System.out.println("IssuedAt:"+claims.getIssuedAt());
    }

}
