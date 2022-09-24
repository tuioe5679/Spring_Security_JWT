package com.tuioe.SpringJWT.service;

import com.tuioe.SpringJWT.dto.MemberRequestDto;
import com.tuioe.SpringJWT.dto.MemberResponsDto;
import com.tuioe.SpringJWT.dto.TokenDto;
import com.tuioe.SpringJWT.enity.Member;
import com.tuioe.SpringJWT.jwt.TokenProvider;
import com.tuioe.SpringJWT.repositroy.MemberRepositroy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepositroy memberRepositroy;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public MemberResponsDto signup(MemberRequestDto requestDto){
        if(memberRepositroy.existsByEmail(requestDto.getEmail())){
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = requestDto.createMember(passwordEncoder);
        return MemberResponsDto.of(memberRepositroy.save(member));
    }

    public TokenDto login(MemberRequestDto memberRequestDto){
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.authentication();

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.tokenCreate(authentication);
    }
}
