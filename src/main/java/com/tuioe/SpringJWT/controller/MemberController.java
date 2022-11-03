package com.tuioe.SpringJWT.controller;

import com.tuioe.SpringJWT.dto.MemberResponseDto;
import com.tuioe.SpringJWT.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyMember(){
        MemberResponseDto memberResponseDto = memberService.getMyInfoBySecurity();
        return ResponseEntity.ok((memberResponseDto));
    }
}
