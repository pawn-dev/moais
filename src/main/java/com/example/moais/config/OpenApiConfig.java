package com.example.moais.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {


        Info info = new Info()
                .title("Moais")
                .version("0.0.1")
                .description(" Moais 과제 제출 문서입니다.\n\n"
                        + "회원 가입은 메일 인증만으로 진행하며 별도의 패스워드를 받지 않습니다. 메일 인증을 통해 토큰을 발급받아 활동하는 방법을 사용합니다\n\n."
                        + "1. /email API를 사용하여 메일 인증번호를 발송합니다. (smtp 이슈로 메일 전송까지 12~17초 정도 소요됩니다. 로딩 기다리시면 됩니다). \n\n"
                        + "2-1. /sign-up API를 사용하여 회원 가입을 진행합니다. (중복 가입 불가능합니다.)\n\n"
                        + "2-2. /sign-in API를 사용하여 최초 로그인을 진행합니다.\n\n\n\n\n\n\n\n"
                        + "-- 아래부터는 인증이 필요한 항목들에 대한 설명입니다. (우측 하단 자물쇠 버튼을 눌러 access token을 입력해야 작동하는 API 목록입니다)\n\n"
                        + "access token: 유저의 정보 접근에 사용되는 토큰.\n\n"
                        + "refresh token: access token이 만료되었을 때 재발급 시 이용하는 토큰.\n\n"
                        + "먼저 인증 방식에 대해 설명 드리겠습니다. 위 절차대로 진행하시면 access token, refresh token을 결과로 받습니다. refresh token은 DB에 저장되며, refresh token 관련 인증을 진행할 때 DB와 항상 대조합니다.\n\n"
                        + "우측 하단에 자물쇠 아이콘 버튼을 눌러주시고 access token을 입력하시면 인증 필요 API들을 이용하실 수 있습니다.\n\n"
                        + "access token의 만료시간은 1시간, refresh token의 만료시간은 7일입니다.\n\n"
                        + "access token이 만료되어 재발급이 필요할 경우 refresh token을 이용합니다.\n\n"
                        + "refresh token은 만료되기 전 /refresh API를 이용하여 재발급 받아야 합니다. 재발급 받을 경우 기존 refresh token은 효력을 잃습니다.\n\n\n\n\n\n\n\n"
                        + "3. /login API를 이용하여 회원 정보를 조회합니다.\n\n"
                        + "4. /sign-out API를 이용하여 로그아웃 진행합니다. DB에 저장된 refresh token 필드 값을 공백으로 업데이트 합니다. 이로써 refresh token은 DB와 대조할 수 없어 효력을 잃습니다. access token을 발급하지 못하게 됩니다.\n\n"
                        + "5. /resign API를 이용하여 회원 탈퇴 진행합니다. 회원 관련 모든 정보가 삭제됩니다.\n\n"
                        + "6. /refresh API를 통해 refreshToken을 재발급 받을 수 있습니다. 이 때 우측 하단 자물쇠 버튼을 눌러 refresh token을 입력해주셔야 합니다.\n\n"
                        + "Todo 관련 기능은 간단하여 자세한 설명은 생략하였습니다. todo의 상태값은 할일(todo), 진행중(run), 완료(end)로 3개입니다.\n\n"
                        + "refresh token을 사용한 이유: access token이 재발급 권한까지 가지게 되면, 해커에게 토큰을 탈취 당했을 때 해커 또한 무제한으로 재발급이 가능해집니다. 그래서 refresh token에 발급 권한을 부여하고 DB에서 관리하였습니다. DB에서 토큰을 제거할 경우 효력을 잃기 때문에 토큰을 탈취 당하더라도 상대적으로 안전합니다.\n\n"
                        +"refresh token은 DB에서 삭제될 경우 즉시 효력을 잃으며 access token은 1시간 이내로 효력을 잃습니다.\n\n"
                        +"(access token을 DB에서 관리할 경우는, API 접근마다 DB와 대조하게 되기 때문에 DB 부하를 다루기 힘들며 세션을 저장하는 것과 비교하여 매리트가 없습니다.)");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Collections.singletonList(securityRequirement))
                .info(info);
    }
}