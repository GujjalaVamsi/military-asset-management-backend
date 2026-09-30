package com.mams.web;

import com.mams.model.Kind;
import java.time.LocalDate;

public class Dto {
    public record PurchaseReq(Long baseId, Long equipmentId, int quantity, LocalDate date) {}
    public record TransferReq(Long fromBaseId, Long toBaseId, Long equipmentId, int quantity, LocalDate date) {}
    public record AssignmentReq(Long baseId, Long equipmentId, String personnel, int quantity, Kind kind, LocalDate date) {}
}
