package com.ainexus.config;

import com.ainexus.model.User;
import com.ainexus.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    public static final String USER = "AUTH_USER";

    private final AuthService auth;

    public AuthInterceptor(AuthService auth) {
        this.auth = auth;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String path = req.getRequestURI();

        if (!path.startsWith("/api/")
                || path.equals("/api/health")
                || path.equals("/api/auth/login")
                || path.equals("/api/auth/register")
                || path.equals("/api/auth/logout")) {
            return true;
        }

        try {
            User user = auth.authenticate(req.getHeader("Authorization"));
            req.setAttribute(USER, user);
            return true;
        } catch (IllegalArgumentException e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            String message = e.getMessage() == null ? "Authentication required." : e.getMessage();
            res.getWriter().write("{\"message\":\"" + message.replace("\\", "\\\\").replace("\"", "'") + "\"}");
            return false;
        }
    }
}
