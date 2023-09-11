package com.example.moais.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    @JsonProperty("email")
    private String email;
}
