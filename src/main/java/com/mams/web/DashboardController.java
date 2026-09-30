package com.mams.web;

import com.mams.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService svc;
    private final PurchaseRepo purchases;
    private final TransferRepo transfers;

    @GetMapping
    public Map<String, Object> summary(@RequestParam(required = false) Long baseId, @RequestParam(required = false) Long typeId,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate from,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate to) {
        return svc.summary(Util.scope(baseId), typeId, from, to);
    }

    /** Pop-up data for "Net Movement". */
    @GetMapping("/net-movement")
    public Map<String, Object> netMovement(@RequestParam(required = false) Long baseId, @RequestParam(required = false) Long typeId,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate from,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DATE) LocalDate to) {
        Long b = Util.scope(baseId);
        return Map.of("purchases", purchases.search(b, typeId, from, to),
                      "transfersIn", transfers.incoming(b, typeId, from, to),
                      "transfersOut", transfers.outgoing(b, typeId, from, to));
    }
}
