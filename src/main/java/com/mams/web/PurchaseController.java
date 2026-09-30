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
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseRepo repo;
    private final BaseRepo bases;
    private final EquipmentTypeRepo types;

    @GetMapping
    public List<Purchase> list(@RequestParam(required = false) Long baseId, @RequestParam(required = false) Long typeId,
                               @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate from,
                               @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate to) {
        return repo.search(Util.scope(baseId), typeId, from, to);
    }

    @PostMapping
    public Purchase create(@RequestBody Dto.PurchaseReq r) {
        Util.requireBase(r.baseId());
        if (r.quantity() <= 0) throw Util.bad("Quantity must be positive");
        Purchase p = new Purchase();
        p.setBase(bases.findById(r.baseId()).orElseThrow(() -> Util.bad("Unknown base")));
        p.setEquipment(types.findById(r.equipmentId()).orElseThrow(() -> Util.bad("Unknown equipment type")));
        p.setQuantity(r.quantity());
        p.setEventDate(r.date() == null ? LocalDate.now() : r.date());
        p.setCreatedBy(Util.user().getUsername());
        return repo.save(p);
    }
}
