package com.spring.zoocare.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new IllegalStateException("В контексте безопасности не найдено ни одного аутентифицированного пользователя.");
        }

        return authentication.getName();
    }
}
