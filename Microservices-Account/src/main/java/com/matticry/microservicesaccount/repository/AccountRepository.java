package com.matticry.microservicesaccount.repository;

import com.matticry.microservicesaccount.enums.TypeAccount;
import com.matticry.microservicesaccount.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByClientIdAndTypeAccount(Long clientId, TypeAccount typeAccount);
    @Query("SELECT a FROM Account a WHERE a.clientId = :clientId")
    List<Account> findByClientId(Integer clientId);




}
