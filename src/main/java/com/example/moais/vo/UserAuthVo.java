package com.example.moais.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_auths")
public class UserAuthVo {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonProperty("userId")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @JsonProperty("token")
    @Column(name = "token", nullable = false)
    private String token;

    @JsonProperty("createDt")
    @CreationTimestamp
    @Column(name = "create_dt", nullable = false)
    private Timestamp createDt;

    @JsonProperty("updateDt")
    @Column(name = "update_dt")
    private Timestamp updateDt;
}
