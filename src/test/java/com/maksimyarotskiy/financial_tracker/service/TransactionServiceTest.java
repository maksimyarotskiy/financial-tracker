package com.maksimyarotskiy.financial_tracker.service;

import com.maksimyarotskiy.financial_tracker.model.Transaction;
import com.maksimyarotskiy.financial_tracker.model.TransactionType;
import com.maksimyarotskiy.financial_tracker.repository.TransactionRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTransactions_ShouldReturnEmptyList_WhenNoTransactions() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        List<Transaction> result = transactionService.getAllTransactions();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getAllTransactions_ShouldReturTransactions_WhenTransactionExists() {
        List<Transaction> transactions = List.of(
                new Transaction( "Salary", BigDecimal.valueOf(1000), TransactionType.INCOME, LocalDate.now()),
                new Transaction( "Groceries", BigDecimal.valueOf(50), TransactionType.EXPENSE, LocalDate.now())
        );

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.getAllTransactions();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Salary", result.get(0).getDescription());
        assertEquals("Groceries", result.get(1).getDescription());

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getTransactionById_ShouldReturnTransaction_WhenTransactionExists() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction("Test transaction", new BigDecimal("200.00"), TransactionType.EXPENSE, LocalDate.now());

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getTransactionById(transactionId);

        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getDescription(), result.getDescription());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getType(), result.getType());

        verify(transactionRepository, times(1)).findById(transactionId);
    }

    @Test
    void getTransactionById_ShouldThrowException_WhenTransactionNotFound() {
        Long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> transactionService.getTransactionById(transactionId));

        verify(transactionRepository, times(1)).findById(transactionId);
    }

    @Test
    void saveTransaction_ShouldSaveTransaction_WhenValidTransaction() {
        Transaction transaction = new Transaction(
                "Test description",
                BigDecimal.valueOf(200),
                TransactionType.EXPENSE,
                LocalDate.now()
        );

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        assertNotNull(savedTransaction);
        assertEquals(transaction.getAmount(), savedTransaction.getAmount());
        assertEquals(transaction.getAmount(), savedTransaction.getAmount());

        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void saveTransaction_ShouldThrowException_WhenInvalidTransaction() {
        Transaction invalidTransaction = new Transaction(
                "Test description",
                BigDecimal.valueOf(-200),
                TransactionType.EXPENSE,
                LocalDate.now()
        );


        assertThrows(IllegalArgumentException.class, () -> transactionService.saveTransaction(invalidTransaction));
    }

}
