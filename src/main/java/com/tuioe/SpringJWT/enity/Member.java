package com.tuioe.SpringJWT.enity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity // Entity 클래스 지정(DB와 매핑)
@Getter // Get 메소드 생성
@NoArgsConstructor // 기본 생성자
public class Member {

    @Id // PK (기본키)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) { this.password = password; }

    @Builder
    public Member(Long id, String email, String password, String nickname, Authority authority) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.authority = authority;
    }
}
