package com.example.payment_service.repository;

import com.example.payment_service.enums.AccountStatus;
import com.example.payment_service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findAllByClientId(UUID clientId);

    Optional<Account> findByIdAndStatus(UUID id, AccountStatus status);
}
