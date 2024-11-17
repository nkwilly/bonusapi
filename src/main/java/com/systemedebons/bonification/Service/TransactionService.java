package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.Historique;
import com.systemedebons.bonification.Entity.Point;
import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PointService pointService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private HistoriqueService historiqueService;

    //@Autowired
    //private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;


    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }

    public Transaction saveTransaction(Transaction transaction) {
        // Get the current authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Client> userOptional = clientRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            transaction.setClient(userOptional.get());
        } else {
            throw new RuntimeException("User not found");
        }

        transaction.setDate(LocalDate.now()); // Set current date
        Transaction savedTransaction = transactionRepository.save(transaction);
        int points = 0;

        if (ruleService.estUneTransactionEligible(savedTransaction)) {
            points = ruleService.calculerPoints(savedTransaction);

            if (points > 0) {
                Point point = new Point();
                point.setClient(savedTransaction.getClient());
                point.setNombre(points);
                point.setId(savedTransaction.getId());
                point.setDate(LocalDate.now());
                pointService.savePoint(point);
            }
        }
        Historique historique = new Historique();
        historique.setClient(savedTransaction.getClient());
        historique.setDate(LocalDate.now());
        historique.setType(savedTransaction.getType());
        historique.setPoints(points);
        historique.setMontantTransaction(savedTransaction.getMontant());
        historique.setDescription("Transaction " + (points > 0 ? "éligible" : "non éligible") + " pour des points.");

        historiqueService.saveHistorique(historique);

        return savedTransaction;
    }

    public void deleteTransaction(String id) {
        transactionRepository.deleteById(id);
    }






}
