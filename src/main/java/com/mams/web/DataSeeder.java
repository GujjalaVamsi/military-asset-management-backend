package com.mams.web;

import com.mams.model.*;
import com.mams.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Seeds demo bases, equipment types and users on first start. */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final BaseRepo bases;
    private final EquipmentTypeRepo types;
    private final AppUserRepo users;
    private final PasswordEncoder enc;

    @Override
    public void run(String... args) {
        if (users.count() > 0) return;
        Base alpha = base("Alpha Base", "North"), bravo = base("Bravo Base", "East"), charlie = base("Charlie Base", "South");
        type("Humvee", "VEHICLE"); type("M4 Rifle", "WEAPON"); type("5.56mm Ammunition", "AMMUNITION");
        user("admin", "admin123", Role.ADMIN, null);
        user("commander", "cmd123", Role.BASE_COMMANDER, alpha);
        user("logistics", "log123", Role.LOGISTICS_OFFICER, alpha);
    }

    private Base base(String n, String l) { Base b = new Base(); b.setName(n); b.setLocation(l); return bases.save(b); }
    private void type(String n, String c) { EquipmentType t = new EquipmentType(); t.setName(n); t.setCategory(c); types.save(t); }
    private void user(String n, String p, Role r, Base b) {
        AppUser u = new AppUser(); u.setUsername(n); u.setPassword(enc.encode(p)); u.setRole(r); u.setBase(b); users.save(u);
    }
}
