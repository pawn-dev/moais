package com.example.moais.repo;

import com.example.moais.vo.UserVo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserVo, Long> {
    Optional<UserVo> findByEmail(String email);
}
