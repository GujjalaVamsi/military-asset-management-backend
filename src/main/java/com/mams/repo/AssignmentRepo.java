package com.mams.repo;

import com.mams.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.*;

public interface AssignmentRepo extends JpaRepository<Assignment, Long> {
    @Query("select a from Assignment a where (:baseId is null or a.base.id = :baseId) and (:typeId is null or a.equipment.id = :typeId) and (:fromDate is null or a.eventDate >= :fromDate) and (:toDate is null or a.eventDate <= :toDate) order by a.eventDate desc, a.id desc")
    List<Assignment> search(@Param("baseId") Long baseId, @Param("typeId") Long typeId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
