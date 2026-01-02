package com.example.payment_service.repository;

import com.example.payment_service.model.OutboxEvent;
import com.example.payment_service.enums.OutboxStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {

    @Query("""
        select o from OutboxEvent o
        where o.status in ('NEW', 'FAILED')
        order by o.createdAt
    """)
    List<OutboxEvent> findForProcessing(Pageable pageable);
}
