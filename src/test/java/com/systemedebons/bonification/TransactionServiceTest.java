package com.systemedebons.bonification;

import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Repository.TransactionRepository;
import com.systemedebons.bonification.Service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class TransactionServiceTest {


    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransactionById() {
        Transaction transaction = new Transaction();
        transaction.setId("1");
        when(transactionRepository.findById("1")).thenReturn(Optional.of(transaction));

        Optional<Transaction> foundTransaction = transactionService.getTransactionById("1");
        assertTrue(foundTransaction.isPresent());
        assertEquals(transaction.getId(), foundTransaction.get().getId());
    }

    @Test
    void testSaveTransaction() {
        Transaction transaction = new Transaction();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        assertNotNull(savedTransaction);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testDeleteTransaction() {
        doNothing().when(transactionRepository).deleteById("1");

        transactionService.deleteTransaction("1");
        verify(transactionRepository, times(1)).deleteById("1");
    }

}
