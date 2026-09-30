package com.mams.security;

import com.mams.repo.AppUserRepo;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

/** Reads the Bearer token and puts the AppUser (with role) into the security context. */
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwt;
    private final AppUserRepo users;

    public JwtFilter(JwtUtil jwt, AppUserRepo users) { this.jwt = jwt; this.users = users; }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) {
            try {
                users.findByUsername(jwt.username(h.substring(7))).ifPresent(u -> SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(u, null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name())))));
            } catch (Exception ignored) { /* invalid/expired token -> stays anonymous */ }
        }
        chain.doFilter(req, res);
    }
}
