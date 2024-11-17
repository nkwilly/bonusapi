package com.systemedebons.bonification.Controller;


import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.Historique;
import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.HistoriqueRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/all-transaction")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("create-transcation")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create-me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction, Authentication authentication) {
        String username = authentication.getName();
        Client client = clientRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        transaction.setClient(client);
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }



}
