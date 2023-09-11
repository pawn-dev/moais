package com.example.moais.interceptor;

import com.example.moais.UserClaims;
import com.example.moais.service.UserAuthService;
import com.example.moais.utils.NoAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthAccessInterceptor implements HandlerInterceptor {
    private final UserAuthService jwtService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {

        if (hasAnnotationNoAuth(handler)) {
            return true;
        }

        try {

            String[] tokenInfo = jwtService.getToken();
            if(!tokenInfo[0].equals("Bearer")) {
                response.sendError(401, "Unauthorized");
                return false;
            }

            Optional<UserClaims> claimsOptional = jwtService.verifyAccessToken(tokenInfo[1]);

            if (claimsOptional.isPresent()) {
                request.setAttribute("claims", claimsOptional.get());
                return true;
            } else {
                response.sendError(401, "Unauthorized");
                return false;
            }
        } catch (Exception ignored) {}

        response.sendError(401, "Unauthorized");
        return false;
    }

    private boolean hasAnnotationNoAuth(Object handler) {
        return ((HandlerMethod) handler).getMethodAnnotation(NoAuth.class) != null;
    }
}