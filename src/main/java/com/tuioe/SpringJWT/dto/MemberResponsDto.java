package com.tuioe.SpringJWT.dto;

import com.tuioe.SpringJWT.enity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponsDto {
    private String email;
    private String nickname;

    public static MemberResponsDto of(Member member){
        return MemberResponsDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}