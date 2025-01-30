package com.maksimyarotskiy.financial_tracker.service;


import com.maksimyarotskiy.financial_tracker.model.Transaction;
import com.maksimyarotskiy.financial_tracker.model.TransactionType;
import com.maksimyarotskiy.financial_tracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;


    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + id));
    }

    public Transaction saveTransaction(Transaction transaction) {
        validateTransaction(transaction);
        return transactionRepository.save(transaction);
    }


    private void validateTransaction(Transaction transaction) {
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount cannot be empty");
        }
        if (transaction.getDescription() == null || transaction.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        if (transaction.getType() == null) {
            throw new IllegalArgumentException("Transaction type must be specified.");
        }
    }

    public Transaction updateTransaction(Long id, Transaction updateTransaction) {
        Transaction existingTransaction = getTransactionById(id);

        existingTransaction.setDescription(updateTransaction.getDescription());
        existingTransaction.setAmount(updateTransaction.getAmount());
        existingTransaction.setType(updateTransaction.getType());
        existingTransaction.setDate(updateTransaction.getDate());

        validateTransaction(existingTransaction);
        return transactionRepository.save(existingTransaction);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<Transaction> getTransactionByType(TransactionType transactionType) {
        return transactionRepository.findByType(transactionType);
    }

    public List<Transaction> getTransactionByAmountGreaterThan(BigDecimal amount) {
        return transactionRepository.findByAmountGreaterThanEqual(amount);
    }

}
