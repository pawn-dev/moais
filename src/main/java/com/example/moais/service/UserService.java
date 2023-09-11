package com.example.moais.service;

import com.example.moais.UserClaims;
import com.example.moais.repo.UserRepo;
import com.example.moais.vo.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean existsUser(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    public UserVo signUp(String email, String nickname) {
        return userRepo.save(UserVo.builder().email(email).nickname(nickname).build());
    }

    public Optional<UserVo> signIn(String email) {
        return userRepo.findByEmail(email);
    }

    public UserVo login(UserClaims claims) {
        return userRepo.findById(claims.getUserId()).orElseThrow();
    }

    public UserVo refresh(UserClaims claims) {
        return userRepo.findById(claims.getUserId()).orElseThrow();
    }

    public void resign(UserClaims claims) {
        userRepo.deleteById(claims.getUserId());
    }
}
