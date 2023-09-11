package com.example.moais.repo;

import com.example.moais.vo.UserAuthVo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthRepo extends JpaRepository<UserAuthVo, Long> {

    Optional<UserAuthVo> findByUserId(Long userId);

    Optional<UserAuthVo> findByToken(String token);
}
