package com.example.moais.service;

import com.example.moais.UserClaims;
import com.example.moais.repo.UserAuthRepo;
import com.example.moais.vo.UserAuthVo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.security.Key;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserAuthService {

    private final Logger logger = LoggerFactory.getLogger(UserAuthService.class);

    private final SecretKey accessTokenSecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode("p+AqXx9j3hZ/OcbHC46ees1SRh44dQQnb7RoHvGvjek="));

    private final SecretKey refreshTokenSecretKey =  Keys.hmacShaKeyFor(Base64.getDecoder().decode("kJdVYnqGazQ7ulaoP1pynCsRFDkfLHGRw3qhRzJa6mc="));

    private final int accessExpSec = 60 * 60;
    private final int refreshExpSec = 7 * 24 * 60 * 60;

    private final UserAuthRepo userAuthRepo;

    public UserAuthService(UserAuthRepo userAuthRepo) {
        this.userAuthRepo = userAuthRepo;
    }

    public String signAccessToken(Long userId) {
        Claims claims = Jwts.claims();

        claims.put("userId", userId);

        return Jwts.builder().setClaims(claims).signWith(accessTokenSecretKey).setExpiration(getAccessExpDate(new Date())).compact();
    }

    public String signRefreshToken(Long userId) {
        Claims claims = Jwts.claims();

        claims.put("userId", userId);

        String refreshToken = Jwts.builder().setClaims(claims).signWith(refreshTokenSecretKey).setExpiration(getRefreshExpDate(new Date())).compact();

        Optional<UserAuthVo> voOptional = userAuthRepo.findByUserId(userId);

        if(voOptional.isPresent()) {
            UserAuthVo vo = voOptional.get();
            vo.setToken(refreshToken);
            vo.setUpdateDt(new Timestamp(new Date().getTime()));
            userAuthRepo.save(vo);
        }
        else {
            userAuthRepo.save(UserAuthVo.builder().userId(userId).token(refreshToken).build());
        }

        return refreshToken;
    }

    public boolean isExistRefreshToken(String token) {
        return userAuthRepo.findByToken(token).isPresent();
    }

    public Optional<UserClaims> verifyAccessToken(String token) {
        return getClaims(token, accessTokenSecretKey);
    }

    public Optional<UserClaims> verifyRefreshToken(String token) {

        if(!isExistRefreshToken(token)) {
            return Optional.empty();
        }

        return getClaims(token, refreshTokenSecretKey);
    }

    public void invalidateRefreshToken(UserClaims userClaims) {
        UserAuthVo vo = userAuthRepo.findByUserId(userClaims.getUserId()).orElseThrow();
        vo.setToken("");
        userAuthRepo.save(vo);
    }

    public String[] getToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization").split(" ");
    }

    private Date getAccessExpDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, accessExpSec);
        return calendar.getTime();
    }

    private Date getRefreshExpDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, refreshExpSec);
        return calendar.getTime();
    }

    private Optional<UserClaims> getClaims(String token, Key secretKey) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            int userId = (int) claims.get("userId");

            return Optional.ofNullable(UserClaims.builder().userId(userId).build());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return Optional.empty();
    }
}
