package com.systemedebons.bonification;

import com.systemedebons.bonification.Controller.TransactionController;
import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Service.TransactionService;
import com.systemedebons.bonification.Service.utils.Mapper;
import com.systemedebons.bonification.payload.dto.TransactionDTO;
import com.systemedebons.bonification.payload.response.SavedTransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private Mapper mapper;

    @Mock
    private TransactionService transactionService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testGetAllTransactions() throws Exception {
        when(transactionService.getAllTransactions()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetTransactionById() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setId("1");
        when(transactionService.getTransactionById("1")).thenReturn(Optional.of(transaction));

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void testCreateTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setId("1");
        SavedTransactionResponse str = mapper.toSavedTransactionResponse(transaction, "Test message");
        when(transactionService.saveTransaction(any(TransactionDTO.class))).thenReturn(str);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clientLogin\":\"berlus\",\"amount\":100.0, \"isDebit\":\"true\", \"status,\":\"COMPLETE\"}"))
                .andExpect(status().isOk())
                .andDo(print());
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("amount").value(100.0));
    }

    @Test
    void testDeleteTransaction() throws Exception {
        doNothing().when(transactionService).deleteTransaction("1");

        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isNoContent());
    }

}
