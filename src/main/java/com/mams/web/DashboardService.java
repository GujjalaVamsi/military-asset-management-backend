package com.mams.web;

import com.mams.model.*;
import com.mams.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final PurchaseRepo purchases;
    private final TransferRepo transfers;
    private final AssignmentRepo assignments;

    private int sumP(Long b, Long t, LocalDate f, LocalDate to) { return purchases.search(b, t, f, to).stream().mapToInt(Purchase::getQuantity).sum(); }
    private int sumIn(Long b, Long t, LocalDate f, LocalDate to) { return transfers.incoming(b, t, f, to).stream().mapToInt(Transfer::getQuantity).sum(); }
    private int sumOut(Long b, Long t, LocalDate f, LocalDate to) { return transfers.outgoing(b, t, f, to).stream().mapToInt(Transfer::getQuantity).sum(); }
    private int sumA(Long b, Long t, LocalDate f, LocalDate to, Kind k) {
        return assignments.search(b, t, f, to).stream().filter(a -> a.getKind() == k).mapToInt(Assignment::getQuantity).sum();
    }

    /** Stock on hand = Purchases + Transfers In - Transfers Out - Expended (within the date window). */
    public int balance(Long b, Long t, LocalDate f, LocalDate to) {
        return sumP(b, t, f, to) + sumIn(b, t, f, to) - sumOut(b, t, f, to) - sumA(b, t, f, to, Kind.EXPENDED);
    }

    public Map<String, Object> summary(Long b, Long t, LocalDate from, LocalDate to) {
        int opening = from == null ? 0 : balance(b, t, null, from.minusDays(1));
        int p = sumP(b, t, from, to), in = sumIn(b, t, from, to), out = sumOut(b, t, from, to);
        int assigned = sumA(b, t, from, to, Kind.ASSIGNED), expended = sumA(b, t, from, to, Kind.EXPENDED);
        int net = p + in - out;
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("openingBalance", opening);
        m.put("purchases", p);
        m.put("transferIn", in);
        m.put("transferOut", out);
        m.put("netMovement", net);
        m.put("assigned", assigned);
        m.put("expended", expended);
        m.put("closingBalance", opening + net - expended);
        return m;
    }
}
