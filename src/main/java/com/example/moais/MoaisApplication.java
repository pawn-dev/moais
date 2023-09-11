package com.example.moais;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

@SpringBootApplication
public class MoaisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoaisApplication.class, args);
    }
}
