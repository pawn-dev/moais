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
@Table(name = "user_todos")
public class TodoVo {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonProperty("userId")
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserVo user;

    @JsonProperty("todoContent")
    @Column(name = "todo_content", nullable = false)
    private String todoContent;

    @JsonProperty("status")
    @Column(name = "status", nullable = false)
    private String status;

    @JsonProperty("createDt")
    @CreationTimestamp
    @Column(name = "create_dt", nullable = false)
    private Timestamp createDt;

    @JsonProperty("updateDt")
    @Column(name = "update_dt")
    private Timestamp updateDt;
}
