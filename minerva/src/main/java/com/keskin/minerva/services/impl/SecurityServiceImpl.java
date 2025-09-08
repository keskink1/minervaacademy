package com.keskin.minerva.services.impl;

import com.keskin.minerva.services.ISecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements ISecurityService {
    @Override
    public boolean currentUserHasRoleAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()){
            return false;
        }
        return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public boolean currentUserHasRoleTeacher() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()){
            return false;
        }
        return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TEACHER"));    }

    @Override
    public boolean currentUserHasRoleStudent() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()){
            return false;
        }
        return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"));    }
}
