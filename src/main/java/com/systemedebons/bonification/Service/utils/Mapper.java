package com.systemedebons.bonification.Service.utils;

import com.systemedebons.bonification.Entity.*;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.HistoryRepository;
import com.systemedebons.bonification.Repository.TransactionRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.payload.dto.HistoryDTO;
import com.systemedebons.bonification.payload.dto.RuleDTO;
import com.systemedebons.bonification.payload.dto.TransactionHistoryDTO;
import com.systemedebons.bonification.payload.exception.EntityNotFound;
import com.systemedebons.bonification.payload.dto.TransactionDTO;
import com.systemedebons.bonification.payload.response.SavedTransactionResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class Mapper {
    private static final Logger log = LoggerFactory.getLogger(Mapper.class);

    private final SecurityUtils securityUtils;
    private final HistoryRepository historyRepository;
    private final TransactionRepository transactionRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public Transaction toTransaction(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setId(dto.getId());
        transaction.setAmount(dto.getAmount());
        transaction.setIsDebit(dto.getIsDebit());
        transaction.setStatus(dto.getStatus());
        transaction.setClientLogin(dto.getClientLogin());
        return transaction;
    }

    public Transaction toTransactionFromRepository(TransactionDTO dto) {
        return transactionRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFound("Transaction not found"));
    }

    public Transaction toTransactionFromRepository(SavedTransactionResponse str) {
        return transactionRepository.findById(str.getId()).orElseThrow(() -> new EntityNotFound("Transaction not found"));
    }

    public TransactionDTO toTransactionDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setIsDebit(transaction.getIsDebit());
        dto.setStatus(transaction.getStatus());
        dto.setClientLogin(transaction.getClientLogin());
        return dto;
    }

    public SavedTransactionResponse toSavedTransactionResponse(Transaction transaction, String message) {
        SavedTransactionResponse str = new SavedTransactionResponse();
        str.setId(transaction.getId());
        str.setAmount(transaction.getAmount());
        str.setIsDebit(transaction.getIsDebit());
        str.setClientId(transaction.getClientLogin());
        str.setIsDebit(transaction.getIsDebit());
        str.setMessage(message);
        return str;
    }

    public TransactionHistoryDTO toTransactionHistoryDTO(Transaction transaction) {
        TransactionHistoryDTO dto = new TransactionHistoryDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setIsDebit(transaction.getIsDebit());
        dto.setStatus(transaction.getStatus());
        dto.setClientLogin(transaction.getClientLogin());
        History history = historyRepository.findByTransactionId(dto.getId()).orElseThrow();
        dto.setDate(history.getDate());
        dto.setPoints(history.getPoints());
        return dto;
    }


    public Rule toRule(RuleDTO dto) {
        Rule rule = new Rule();
        rule.setDescription(dto.getDescription());
        rule.setAmountMin(dto.getAmountMin());
        rule.setAmountMax(dto.getAmountMax());
        rule.setPoints(dto.getPoints());
        rule.setAlwaysCredit(dto.getAlwaysCredit());
        rule.setMinDaysForIrregularClients(dto.getMinDaysForIrregularClients());
        Optional<User> optionalUser = securityUtils.getCurrentUser();
        if (optionalUser.isEmpty())
            return rule;
        rule.setUserId(optionalUser.get().getId());
        return rule;
    }

    public History toHistory(HistoryDTO dto) {
        History history = new History();
        history.setId(dto.getId());
        history.setDate(dto.getDate());
        history.setPoints(dto.getPoints());
        history.setTransactionId(dto.getTransactionId());
        history.setClientId(dto.getClientId());
        User user = securityUtils.getCurrentUser().orElseThrow();
        history.setUserId(user.getId());
        return history;
    }

    public HistoryDTO toHistoryDTO(History history) {
        HistoryDTO dto = new HistoryDTO();
        dto.setId(history.getId());
        dto.setDate(history.getDate());
        dto.setPoints(history.getPoints());
        dto.setTransactionId(history.getTransactionId());
        dto.setClientId(history.getClientId());
        return dto;
    }
}
