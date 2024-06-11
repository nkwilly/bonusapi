package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Point;
import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Repository.TransactionRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }

    public Transaction saveTransaction(Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);

        if (ruleService.estUneTransactionEligible(savedTransaction)) {
            int points = ruleService.calculerPoints(savedTransaction);

            if (points > 0) {
                Point point = new Point();
                point.setUser(savedTransaction.getUser()); // Set the user reference
                point.setNombre(points);
                point.setId(savedTransaction.getId());
                point.setDate(savedTransaction.getDate());
                pointService.savePoint(point);
            }
        }
        return savedTransaction;
    }

    public void deleteTransaction(String id) {
        transactionRepository.deleteById(id);
    }






}
