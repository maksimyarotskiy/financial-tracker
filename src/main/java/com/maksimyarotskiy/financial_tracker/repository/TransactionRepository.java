package com.maksimyarotskiy.financial_tracker.repository;

import com.maksimyarotskiy.financial_tracker.model.Transaction;
import com.maksimyarotskiy.financial_tracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByType(TransactionType transactionType);
    List<Transaction> findByAmountGreaterThanEqual(BigDecimal amount);

}
