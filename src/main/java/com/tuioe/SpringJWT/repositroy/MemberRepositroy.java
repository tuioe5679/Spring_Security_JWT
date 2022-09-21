package com.tuioe.SpringJWT.repositroy;

import com.tuioe.SpringJWT.enity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepositroy extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);// email로 member를 찾는 쿼리 메소드
    boolean existsByEmail(String email);// email이 존재하는지 판별하는 쿼리 메소드 
}
