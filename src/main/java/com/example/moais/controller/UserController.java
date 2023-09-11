package com.example.moais.controller;

import com.example.moais.UserClaims;
import com.example.moais.dto.*;
import com.example.moais.service.EmailService;
import com.example.moais.service.UserService;
import com.example.moais.service.UserAuthService;
import com.example.moais.utils.NoAuth;
import com.example.moais.vo.EmailVerifVo;
import com.example.moais.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final UserAuthService userAuthService;

    public UserController(UserService userService, EmailService emailService, UserAuthService userAuthService) {
        this.userService = userService;
        this.emailService = emailService;
        this.userAuthService = userAuthService;
    }

    @NoAuth
    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto) {

        if(userService.existsUser(signUpDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("이미 존재하는 유저입니다. sign-in을 시도해주세요."));
        }

        if (emailService.checkEmailVerif(signUpDto.getEmail(), signUpDto.getCode())) {

            UserVo vo = userService.signUp(signUpDto.getEmail(), signUpDto.getNickname());
            String accessToken = userAuthService.signAccessToken(vo.getId());
            String refreshToken = userAuthService.signRefreshToken(vo.getId());
            UserDto dto = UserDto.builder().userInfo(vo).accessToken(accessToken).refreshToken(refreshToken).build();

            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("이메일 또는 인증번호가 올바르지 않습니다."));
        }
    }

    @NoAuth
    @PostMapping("/email")
    public ResponseEntity<MsgDto> sendJoinMail(@RequestBody EmailDto emailDto) {

        String code = emailService.sendMail(emailDto.getEmail());

        MsgDto responseDto = new MsgDto();
        responseDto.setMessage("메일 인증 번호를 발송 했습니다. 확인 후 회원가입 진행해주세요.");

        EmailVerifVo emailVerifVo = new EmailVerifVo();
        emailVerifVo.setEmail(emailDto.getEmail());
        emailVerifVo.setCode(code);

        emailService.setMailVerif(emailVerifVo);

        return ResponseEntity.ok(responseDto);
    }

    @NoAuth
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInDto signInDto) {

        if(!userService.existsUser(signInDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("존재하지 않는 유저입니다."));
        }

        if (emailService.checkEmailVerif(signInDto.getEmail(), signInDto.getCode())) {

            UserVo vo = userService.signIn(signInDto.getEmail()).orElseThrow();

            String accessToken = userAuthService.signAccessToken(vo.getId());
            String refreshToken = userAuthService.signRefreshToken(vo.getId());
            UserDto dto = UserDto.builder().userInfo(vo).accessToken(accessToken).refreshToken(refreshToken).build();

            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MsgDto("이메일 또는 인증번호가 올바르지 않습니다."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(HttpServletRequest request) {

        UserVo vo = userService.login((UserClaims) request.getAttribute("claims"));

        String accessToken = userAuthService.signAccessToken(vo.getId());

        UserDto dto = UserDto.builder().userInfo(vo).accessToken(accessToken).refreshToken(null).build();

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserDto> refresh(HttpServletRequest request) {

        UserVo vo = userService.refresh((UserClaims) request.getAttribute("claims"));

        String accessToken = userAuthService.signAccessToken(vo.getId());
        String refreshToken = userAuthService.signRefreshToken(vo.getId());

        UserDto dto = UserDto.builder().userInfo(vo).accessToken(accessToken).refreshToken(refreshToken).build();

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @RequestMapping(value = "/sign-out", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MsgDto> signOut(HttpServletRequest request) {

        userAuthService.invalidateRefreshToken((UserClaims) request.getAttribute("claims"));

        return ResponseEntity.ok(MsgDto.builder().message("OK").build());
    }

    @RequestMapping(value = "/resign", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MsgDto> resign(HttpServletRequest request) {

        userService.resign((UserClaims) request.getAttribute("claims"));

        return ResponseEntity.ok(MsgDto.builder().message("OK").build());
    }
}
