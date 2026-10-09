package com.ainexus.config;

import com.ainexus.model.User;

public final class RoleGuard {
    private RoleGuard() {}

    public static void requireFacultyOrAdmin(User user) {
        if (user == null || !("ADMIN".equals(user.getRole()) || "FACULTY".equals(user.getRole()))) {
            throw new IllegalArgumentException("Admin or faculty access required.");
        }
    }
}
