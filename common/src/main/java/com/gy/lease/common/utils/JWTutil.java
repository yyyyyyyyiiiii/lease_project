package com.gy.lease.common.utils;

import com.gy.lease.common.exception.LeaseException;
import com.gy.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Date;

public class JWTutil {

    private static SecretKey secretKey = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());
    public static String createToken(Long userId,String userName){
        String jwt = Jwts.builder().
                setExpiration(new Date(System.currentTimeMillis() + 3600000L*100)).
                claim("userId", userId)
                .claim("userName", userName)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    public static Claims parse(String token){

        if(token == null){
            throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            return body;
        } catch (ExpiredJwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        } catch (MalformedJwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        } catch (SignatureException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        } catch (IllegalArgumentException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }
    }

    public static void main(String[] args) {
        System.out.println(JWTutil.createToken(1L,"13888888888"));
    }
}
