package com.mams.security;

import com.mams.model.*;
import com.mams.repo.AuditLogRepo;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/** API logging: every /api call (user, method, path, status) is stored in audit_logs. */
public class AuditFilter extends OncePerRequestFilter {
    private final AuditLogRepo repo;

    public AuditFilter(AuditLogRepo repo) { this.repo = repo; }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest r) { return !r.getRequestURI().startsWith("/api/"); }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        try {
            chain.doFilter(req, res);
        } finally {
            Authentication a = SecurityContextHolder.getContext().getAuthentication();
            AuditLog l = new AuditLog();
            l.setUsername(a != null && a.getPrincipal() instanceof AppUser u ? u.getUsername() : "anonymous");
            l.setMethod(req.getMethod());
            String q = req.getQueryString();
            l.setPath(req.getRequestURI() + (q == null ? "" : "?" + q));
            l.setStatus(res.getStatus());
            repo.save(l);
        }
    }
}
