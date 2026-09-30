package com.mams.web;

import com.mams.model.*;
import com.mams.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentRepo repo;
    private final BaseRepo bases;
    private final EquipmentTypeRepo types;
    private final DashboardService dash;

    @GetMapping
    public List<Assignment> list(@RequestParam(required = false) Long baseId, @RequestParam(required = false) Long typeId,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate from,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate to) {
        return repo.search(Util.scope(baseId), typeId, from, to);
    }

    @PostMapping
    public Assignment create(@RequestBody Dto.AssignmentReq r) {
        Util.requireBase(r.baseId());
        if (r.quantity() <= 0) throw Util.bad("Quantity must be positive");
        if (r.kind() == null) throw Util.bad("Kind (ASSIGNED or EXPENDED) is required");
        if (r.kind() == Kind.ASSIGNED && (r.personnel() == null || r.personnel().isBlank())) throw Util.bad("Personnel is required");
        int available = dash.balance(r.baseId(), r.equipmentId(), null, null);
        if (r.quantity() > available) throw Util.bad("Insufficient stock at base (available: " + available + ")");
        Assignment a = new Assignment();
        a.setBase(bases.findById(r.baseId()).orElseThrow(() -> Util.bad("Unknown base")));
        a.setEquipment(types.findById(r.equipmentId()).orElseThrow(() -> Util.bad("Unknown equipment type")));
        a.setPersonnel(r.personnel());
        a.setQuantity(r.quantity());
        a.setKind(r.kind());
        a.setEventDate(r.date() == null ? LocalDate.now() : r.date());
        a.setCreatedBy(Util.user().getUsername());
        return repo.save(a);
    }
}
