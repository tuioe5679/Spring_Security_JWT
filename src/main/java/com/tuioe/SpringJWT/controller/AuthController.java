package com.tuioe.SpringJWT.controller;

import com.tuioe.SpringJWT.dto.MemberRequestDto;
import com.tuioe.SpringJWT.dto.MemberResponsDto;
import com.tuioe.SpringJWT.dto.TokenDto;
import com.tuioe.SpringJWT.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/singup")
    public ResponseEntity<MemberResponsDto> stingUp(@RequestBody MemberRequestDto requestDto){
        return ResponseEntity.ok(authService.signup(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto requestDto){
        return ResponseEntity.ok(authService.login(requestDto));
    }
}
