package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.*;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.TransactionRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.utils.Mapper;
import com.systemedebons.bonification.payload.exception.AccessTransactionException;
import com.systemedebons.bonification.payload.dto.TransactionDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private TransactionRepository transactionRepository;

    private PointService pointService;

    private RuleService ruleService;

    private HistoriqueService historiqueService;

    private ClientRepository clientRepository;

    private SecurityUtils securityUtils;

    private Mapper mapper;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public List<Transaction> getAllTransactions() {
        if (securityUtils.isCurrentUserAdmin())
            return transactionRepository.findAll();
        return transactionRepository.findByUserId(securityUtils.getCurrentUser().map(User::getId).orElseThrow(() -> new RuntimeException("User not logged in")));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Transaction> getAllTransactionsByUser(String userId){
        return transactionRepository.findByUserId(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Optional<Transaction> getTransactionById(String transactionId) {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (transaction.isEmpty())
            return Optional.empty();
        if (securityUtils.isClientOfCurrentUser(transaction.get().getClient().getId()) || securityUtils.isCurrentUserAdmin() )
            return transaction;
        throw new AccessTransactionException();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Transaction saveTransaction(TransactionDTO transactionDTO) {
        log.debug("bonjour le monde {}", transactionDTO);
        Transaction transaction = mapper.toTransaction(transactionDTO);
        if (!securityUtils.isClientOfCurrentUser(transaction.getClient().getId()) && !securityUtils.isCurrentUserAdmin())
            throw new AccessTransactionException();

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
        historique.setPoints(points);
        historique.setTransaction(savedTransaction);
        historique.setDescription("Transaction " + (points > 0 ? "éligible" : "non éligible") + " pour des points.");

        historiqueService.saveHistorique(historique);

        return savedTransaction;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public void deleteTransaction(String transcationId) {
        Transaction transaction = transactionRepository.findById(transcationId).orElse(new Transaction());
        if (securityUtils.isClientOfCurrentUser(transaction.getClient().getId()) || securityUtils.isCurrentUserAdmin())
            transactionRepository.deleteById(transcationId);
        throw new AccessTransactionException();
    }
}
