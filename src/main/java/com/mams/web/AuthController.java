package com.mams.web;

import com.mams.model.AppUser;
import com.mams.repo.AppUserRepo;
import com.mams.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AppUserRepo users;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        AppUser u = users.findByUsername(body.get("username"))
                .filter(x -> encoder.matches(String.valueOf(body.get("password")), x.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("token", jwt.generate(u.getUsername()));
        m.put("username", u.getUsername());
        m.put("role", u.getRole().name());
        m.put("baseId", u.getBase() == null ? null : u.getBase().getId());
        m.put("baseName", u.getBase() == null ? "All bases" : u.getBase().getName());
        return m;
    }
}
