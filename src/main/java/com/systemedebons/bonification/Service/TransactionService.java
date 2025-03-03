package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.*;
import com.systemedebons.bonification.Repository.*;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.utils.Mapper;
import com.systemedebons.bonification.payload.dto.TransactionHistoryDTO;
import com.systemedebons.bonification.payload.exception.AccessTransactionException;
import com.systemedebons.bonification.payload.dto.TransactionDTO;
import com.systemedebons.bonification.payload.response.SavedTransactionResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final PointRepository pointRepository;

    private final HistoryRepository historyRepository;

    private final TransactionRepository transactionRepository;

    private final RuleService ruleService;

    private final SecurityUtils securityUtils;

    private final Mapper mapper;
    private final RuleRepository ruleRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Transaction> getAllTransactions() {
        if (securityUtils.isCurrentUserAdmin())
            return transactionRepository.findAll();
        List<Transaction> transactions = transactionRepository.findAll();
        String currentUsername = securityUtils.getCurrentUser().orElseThrow().getLogin();
        log.info("first transactions = {}", transactions);
        transactions = transactions.stream().filter(transaction ->  {
            Client client = clientRepository.findById(transaction.getClientLogin()).orElseThrow();
            User user = userRepository.findById(transaction.getUserId()).orElseThrow();
            return Objects.equals(user.getLogin(), currentUsername);
        }).collect(Collectors.toList());
        log.info("transactions: {}", transactions);
        log.info("username = {}", securityUtils.getCurrentUser().get().getLogin());
        return transactions;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TransactionHistoryDTO> getAllTransactionsByUser(String userId){
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        return transactions.stream().map(mapper::toTransactionHistoryDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Optional<Transaction> getTransactionById(String transactionId) {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (transaction.isEmpty())
            return Optional.empty();
        if (securityUtils.isClientOfCurrentUser(transaction.get().getClientLogin()) || securityUtils.isCurrentUserAdmin() )
            return transaction;
        throw new AccessTransactionException();
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public SavedTransactionResponse saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = mapper.toTransaction(transactionDTO);
        if (!securityUtils.isClientOfCurrentUser(transaction.getClientLogin()) && !securityUtils.isCurrentUserAdmin())
            throw new AccessTransactionException();

        // Création of point's instance
        Optional<Point> getPoint = pointRepository.findByClientId(transaction.getClientLogin());
        Point point = new Point();
        if (getPoint.isEmpty()) {
            point.setNumber(0);
            point.setClientId(transaction.getClientLogin());
        }
        else point = getPoint.get();

        // Get Rule that's correspond to the amount of transaction.
        Rule rule;
        log.info("clientLogin : {}", transaction.getClientLogin());
        Client client = clientRepository.findByLogin(transaction.getClientLogin()).orElseThrow();
        User user = userRepository.findById(client.getUserId()).orElseThrow();

        Optional<Rule> optionalRule = ruleRepository.findFirstByUserIdAndAmountMinLessThan(user.getId(), transaction.getAmount(), 5);
        if (optionalRule.isEmpty()) {
            rule = new Rule();
            rule.setAmountMin(0);
            rule.setAlwaysCredit(false);
            rule.setPoints(0);
            rule.setDescription("Default rule");
            rule.setMinDaysForIrregularClients(0);
        }
        else rule = optionalRule.get();

        History history = new History();
        history.setDate(LocalDate.now());
        StringBuilder message = new StringBuilder();

        // Check if the amount will be debited or credited from the customer's account
        if (transaction.getIsDebit()) {
            int initialPoint = point.getNumber();
            double recover = ruleService.getAmountOfPoints(point.getNumber());
            double amount = transaction.getAmount();
            double rest = recover - amount;
            // compute date between this transaction and the last transaction :
            if (initialPoint == 0){
                int points = ruleService.computePoints(transaction.getAmount());
                point.setNumber(points + point.getNumber());
                history.setPoints(points);
                message.append("If the customer has 0 points, we can't debit his account, instead, we credit the account.\n");
            }
            else if (rest > 0) {
                point.setNumber(ruleService.computePoints(rest));
                history.setPoints(-(initialPoint - point.getNumber()));
                transaction.setAmount(0);
            }
            else {
                rest = -rest;
                log.debug("rest = {}", rest);
                transaction.setAmount(rest);
                history.setPoints(-ruleService.computePoints(rest));
                point.setNumber(0);
            }
            //Check if the amount will be debited from the customer's account
            if (rule.getAlwaysCredit()) {
                int points = ruleService.computePoints(transaction.getAmount());
                point.setNumber(points + point.getNumber());
                history.setPoints(points);
                message.append("This rule activates the always credit option, the account has been credited with ").append(points).append(" points.\n");
            }
        }
        else {

            Optional<History> optionalHistory = historyRepository.findTopByClientIdOrderByDateDesc(client.getId());
            if (optionalHistory.isPresent()) {
                long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), optionalHistory.get().getDate());
                if (daysBetween - rule.getMinDaysForIrregularClients() < 0) {
                    int points = ruleService.computePoints(transaction.getAmount());
                    point.setNumber(points + point.getNumber());
                    history.setPoints(points);
                    message.append(points).append(" points have been added.\n");
                } else {
                    message.append("The customer has gone too many days without paying.\n");
                }
            }
            else {
                int points = ruleService.computePoints(transaction.getAmount());
                point.setNumber(points + point.getNumber());
                history.setPoints(points);
                message.append(points).append(" points have been added.\n");
            }
        }
        transaction.setId(UUID.randomUUID().toString());
        transaction.setUserId(user.getId());
        Transaction savedTransaction = transactionRepository.save(transaction);
        history.setTransactionId(savedTransaction.getId());
        history.setUserId(user.getId());
        history.setId(UUID.randomUUID().toString());
        history.setClientId(client.getId());
        History savedHistory = historyRepository.save(history);
        point.setUserId(user.getId());
        point.setId(UUID.randomUUID().toString());
        Point savedPoint = pointRepository.save(point);

        log.debug("savedTransaction = {}", savedTransaction);
        log.debug("savedHistory = {}", savedHistory);
        log.debug("savedPoint = {}", savedPoint);
        return mapper.toSavedTransactionResponse(savedTransaction, message.toString());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteTransaction(String transcationId) {
        Transaction transaction = transactionRepository.findById(transcationId).orElse(new Transaction());
        if (securityUtils.isClientOfCurrentUser(transaction.getClientLogin()) || securityUtils.isCurrentUserAdmin())
            transactionRepository.deleteById(transcationId);
        throw new AccessTransactionException();
    }
}
