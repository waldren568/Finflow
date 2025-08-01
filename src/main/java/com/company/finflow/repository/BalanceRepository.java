package com.company.finflow.repository;

import com.company.finflow.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    default Balance getBalance() {
        return findById(1L).orElseGet(() -> {
            Balance balance = new Balance();
            save(balance);
            return balance;
        });
    }
}