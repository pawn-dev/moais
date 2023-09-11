package com.example.moais.dto;

import com.example.moais.vo.UserVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @JsonProperty("userInfo")
    private UserVo userInfo;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("accessToken")
    private String accessToken;
}
