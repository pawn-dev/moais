package com.example.moais.repo;

import com.example.moais.vo.EmailVerifVo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerifRepo extends JpaRepository<EmailVerifVo, Long> {
    EmailVerifVo findByEmail(String email);
    Integer deleteByEmailAndCode(String email, String code);
}
