package com.mams.repo;

import com.mams.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.*;

public interface PurchaseRepo extends JpaRepository<Purchase, Long> {
    @Query("select p from Purchase p where (:baseId is null or p.base.id = :baseId) and (:typeId is null or p.equipment.id = :typeId) and (:fromDate is null or p.eventDate >= :fromDate) and (:toDate is null or p.eventDate <= :toDate) order by p.eventDate desc, p.id desc")
    List<Purchase> search(@Param("baseId") Long baseId, @Param("typeId") Long typeId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
