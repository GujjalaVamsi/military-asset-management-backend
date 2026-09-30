package com.mams.repo;

import com.mams.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.*;

public interface TransferRepo extends JpaRepository<Transfer, Long> {
    @Query("select t from Transfer t where (:baseId is null or t.fromBase.id = :baseId or t.toBase.id = :baseId) and (:typeId is null or t.equipment.id = :typeId) and (:fromDate is null or t.eventDate >= :fromDate) and (:toDate is null or t.eventDate <= :toDate) order by t.eventDate desc, t.id desc")
    List<Transfer> search(@Param("baseId") Long baseId, @Param("typeId") Long typeId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
    @Query("select t from Transfer t where (:baseId is null or t.toBase.id = :baseId) and (:typeId is null or t.equipment.id = :typeId) and (:fromDate is null or t.eventDate >= :fromDate) and (:toDate is null or t.eventDate <= :toDate) order by t.eventDate desc, t.id desc")
    List<Transfer> incoming(@Param("baseId") Long baseId, @Param("typeId") Long typeId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
    @Query("select t from Transfer t where (:baseId is null or t.fromBase.id = :baseId) and (:typeId is null or t.equipment.id = :typeId) and (:fromDate is null or t.eventDate >= :fromDate) and (:toDate is null or t.eventDate <= :toDate) order by t.eventDate desc, t.id desc")
    List<Transfer> outgoing(@Param("baseId") Long baseId, @Param("typeId") Long typeId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
