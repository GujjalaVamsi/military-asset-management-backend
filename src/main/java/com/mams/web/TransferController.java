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
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferRepo repo;
    private final BaseRepo bases;
    private final EquipmentTypeRepo types;
    private final DashboardService dash;

    @GetMapping
    public List<Transfer> list(@RequestParam(required = false) Long baseId, @RequestParam(required = false) Long typeId,
                               @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate from,
                               @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate to) {
        return repo.search(Util.scope(baseId), typeId, from, to);
    }

    @PostMapping
    public Transfer create(@RequestBody Dto.TransferReq r) {
        Util.requireBase(r.fromBaseId()); // you may only send stock out of your own base
        if (r.quantity() <= 0) throw Util.bad("Quantity must be positive");
        if (r.fromBaseId().equals(r.toBaseId())) throw Util.bad("Source and destination base must differ");
        int available = dash.balance(r.fromBaseId(), r.equipmentId(), null, null);
        if (r.quantity() > available) throw Util.bad("Insufficient stock at source base (available: " + available + ")");
        Transfer t = new Transfer();
        t.setFromBase(bases.findById(r.fromBaseId()).orElseThrow(() -> Util.bad("Unknown source base")));
        t.setToBase(bases.findById(r.toBaseId()).orElseThrow(() -> Util.bad("Unknown destination base")));
        t.setEquipment(types.findById(r.equipmentId()).orElseThrow(() -> Util.bad("Unknown equipment type")));
        t.setQuantity(r.quantity());
        t.setEventDate(r.date() == null ? LocalDate.now() : r.date());
        t.setCreatedBy(Util.user().getUsername());
        return repo.save(t);
    }
}
