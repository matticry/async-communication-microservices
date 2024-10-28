package com.matticry.microservicesaccount.repository;

import com.matticry.microservicesaccount.models.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    Optional<Movement> findTopByAccountIdOrderByDateMovDesc(Integer accountId);

    @Query("SELECT m FROM Movement m " +
            "JOIN Account a ON m.accountId = a.idAccount " +
            "WHERE DATE(m.dateMov) BETWEEN :startDate AND :endDate " +
            "AND a.clientId = :clientId " +
            "ORDER BY m.dateMov ASC")
    List<Movement> findMovementsByDateRangeAndClientId(
            LocalDate startDate,
            LocalDate endDate,
            Integer clientId
    );

    List<Movement> findByAccountId(Integer accountId);



}
