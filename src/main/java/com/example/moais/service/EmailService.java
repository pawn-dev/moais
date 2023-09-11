package com.example.moais.service;

import com.example.moais.repo.EmailVerifRepo;
import com.example.moais.vo.EmailVerifVo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailVerifRepo emailVerifRepo;

    public EmailService(JavaMailSender javaMailSender, EmailVerifRepo emailVerifRepo) {
        this.javaMailSender = javaMailSender;
        this.emailVerifRepo = emailVerifRepo;
    }

    public String sendMail(String to) {
        String authCode = createCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("인증번호 안내 드립니다.");
        message.setText("인증번호는 "+authCode+" 입니다.");
        javaMailSender.send(message);

        return authCode;
    }

    public void setMailVerif(EmailVerifVo emailVerifVo) {
        EmailVerifVo vo = emailVerifRepo.findByEmail(emailVerifVo.getEmail());

        if(vo != null) {
            vo.setCode(emailVerifVo.getCode());
            emailVerifRepo.save(vo);
        }
        else {
            emailVerifRepo.save(emailVerifVo);
        }
    }

    public boolean checkEmailVerif(String email, String code) {
        return emailVerifRepo.deleteByEmailAndCode(email, code) != 0;
    }

    private String createCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}