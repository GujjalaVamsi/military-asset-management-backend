package com.mams.web;

import com.mams.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public final class Util {
    private Util() {}

    public static AppUser user() { return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); }
    public static boolean isAdmin() { return user().getRole() == Role.ADMIN; }

    /** Admin may pick any base (or all = null); everyone else is locked to their own base. */
    public static Long scope(Long requested) { return isAdmin() ? requested : user().getBase().getId(); }

    public static void requireBase(Long baseId) {
        if (!isAdmin() && !user().getBase().getId().equals(baseId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only operate on your own base");
    }

    public static ResponseStatusException bad(String msg) { return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg); }
}
