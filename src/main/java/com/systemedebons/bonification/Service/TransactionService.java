package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Historique;
import com.systemedebons.bonification.Entity.Point;
import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Repository.TransactionRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }

    public Transaction saveTransaction(Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        int points = 0;

        if (ruleService.estUneTransactionEligible(savedTransaction)) {
            points = ruleService.calculerPoints(savedTransaction);

            if (points > 0) {
                Point point = new Point();
                point.setUser(savedTransaction.getUser()); // Set the user reference
                point.setNombre(points);
                point.setId(savedTransaction.getId());
                point.setDate(savedTransaction.getDate());
                pointService.savePoint(point);
            }
        }
        Historique historique = new Historique();
        historique.setUser(savedTransaction.getUser());
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
