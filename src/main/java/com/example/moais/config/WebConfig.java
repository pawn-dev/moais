package com.example.moais.config;

import com.example.moais.interceptor.AuthAccessInterceptor;
import com.example.moais.interceptor.AuthRefreshInterceptor;
import com.example.moais.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final UserAuthService jwtService;

    @Override
    public void addInterceptors(InterceptorRegistry reg) {
        reg.addInterceptor(new AuthAccessInterceptor(jwtService)).order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/users/refresh")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/v3/**");
        reg.addInterceptor(new AuthRefreshInterceptor(jwtService)).order(1)
                .addPathPatterns("/users/refresh")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/v3/**");
    }
}
