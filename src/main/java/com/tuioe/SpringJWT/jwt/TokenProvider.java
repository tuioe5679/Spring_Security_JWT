package com.tuioe.SpringJWT.jwt;

import com.tuioe.SpringJWT.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Log4j2
public class TokenProvider {

    // 토큰 생성후 검증값
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";

    // 토큰 만료 시간 30분
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private final Key key;

    // secret key 값을 받고 decode 이후 key값으로 정함
    public TokenProvider(@Value("${jwt.secret}") String secretKey){
        byte[] keyBytes = secretKey.getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰을 생성하는 메소드
    public TokenDto tokenCreate(Authentication authentication){

        // Authentication 값을 String 으로 변환
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 현재 시간
        long nowTime = (new Date()).getTime();

        // 만료 시간
        Date tokenExpiresIn = new Date(nowTime + ACCESS_TOKEN_EXPIRE_TIME);

        // Jwts 토큰 생성
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY,authorities)
                .setExpiration(tokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // TokenDto는 토큰 검증값,토큰,만료 시간을 포함
        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(token)
                .tokenExpiresIn(tokenExpiresIn.getTime())
                .build();
    }

    // 토큰을 받았을때 토큰의 인증을 꺼내는 메소드
    public Authentication getAuthentication(String accessToken){

        // String 토큰을 Claims 형태로 생성
        Claims claims = parseToken(accessToken);

        if(claims.get(AUTHORITIES_KEY) == null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다");
        }

        // 인가 정보를 stream을 사용하여 토큰을 정렬후 List 생성
        Collection<? extends GrantedAuthority> authorities =
                 Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 유저의 정보를 담고 있는 객체 (토큰에서 가져온 정보,인가 정보)
        UserDetails principal = new User(claims.getSubject(), "" ,authorities);

        return new UsernamePasswordAuthenticationToken(principal, "" , authorities);

    }

    // 토큰 검증 메소드
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    //토큰을 claims 형태로 생성하는 메소드 (권한 정보 확인 가능)
    private Claims parseToken(String accessToken){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
}
