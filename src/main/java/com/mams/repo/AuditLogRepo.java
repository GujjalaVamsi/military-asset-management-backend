package com.mams.repo;

import com.mams.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.*;

public interface AuditLogRepo extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findTop200ByOrderByIdDesc();
}
