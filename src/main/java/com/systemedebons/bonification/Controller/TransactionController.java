package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.TransactionService;
import com.systemedebons.bonification.Service.utils.Mapper;
import com.systemedebons.bonification.payload.dto.TransactionDTO;
import com.systemedebons.bonification.payload.dto.TransactionHistoryDTO;
import com.systemedebons.bonification.payload.response.SavedTransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Controller", description = "Endpoints for managing transactions")
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private final SecurityUtils securityUtils;
    private final Mapper mapper;
    private TransactionService transactionService;

    @Operation(summary = "Retrieve all transactions", description = "Returns a list of all transactions.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions")
    @GetMapping("/all-transaction")
    public List<TransactionHistoryDTO> getAllTransactions() {
        return transactionService.getAllTransactions().stream().map(mapper::toTransactionHistoryDTO).toList();
    }

    @Operation(summary = "Retrieve a transaction by ID", description = "Fetches a transaction based on its unique ID.")
    @ApiResponse(responseCode = "200", description = "Transaction found")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new transaction", description = "Saves a new transaction to the database.")
    @ApiResponse(responseCode = "200", description = "Transaction successfully created")
    @PostMapping
    public ResponseEntity<SavedTransactionResponse> createTransaction(@RequestBody TransactionDTO transaction) {
        SavedTransactionResponse savedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    @Operation(summary = "Delete a transaction", description = "Deletes a transaction using its ID.")
    @ApiResponse(responseCode = "204", description = "Transaction successfully deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieve transactions of the current user", description = "Fetches all transactions associated with the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of user transactions")
    @GetMapping("/user")
    public ResponseEntity<List<TransactionHistoryDTO>> getTransactionByUser() {
        String username = securityUtils.getCurrentUsername().orElseThrow();
        return ResponseEntity.ok(transactionService.getAllTransactionsByUser(username));
    }
}