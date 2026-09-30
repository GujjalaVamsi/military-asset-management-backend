package com.mams.web;

import com.mams.model.*;
import com.mams.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RefController {
    private final BaseRepo bases;
    private final EquipmentTypeRepo types;
    private final AuditLogRepo audit;

    @GetMapping("/bases") public List<Base> bases() { return bases.findAll(); }
    @GetMapping("/equipment-types") public List<EquipmentType> types() { return types.findAll(); }
    @GetMapping("/audit-logs") public List<AuditLog> logs() { return audit.findTop200ByOrderByIdDesc(); } // ADMIN only (SecurityConfig)
}
