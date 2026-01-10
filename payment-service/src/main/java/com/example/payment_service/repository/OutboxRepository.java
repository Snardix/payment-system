package com.example.payment_service.repository;

import com.example.payment_service.model.OutboxEvent;
import com.example.payment_service.enums.OutboxStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {

    @Query(
            value = """
        SELECT *
        FROM outbox_events
        WHERE status IN ('NEW', 'FAILED')
        ORDER BY created_at
        FOR UPDATE SKIP LOCKED
        LIMIT :limit
      """,
            nativeQuery = true
    )
    List<OutboxEvent> findForProcessing(@Param("limit") int limit);

    @Modifying
    @Query("""
  DELETE FROM OutboxEvent o
  WHERE
    (o.status = 'SENT' AND o.sentAt < :sentBefore)
    OR
    (o.status = 'DEAD' AND o.createdAt < :deadBefore)
""")
    void deleteOld(
            @Param("sentBefore") Instant sentBefore,
            @Param("deadBefore") Instant deadBefore
    );
}
