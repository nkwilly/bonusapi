package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.History;
import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.HistoryRepository;
import com.systemedebons.bonification.Repository.TransactionRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.utils.Mapper;
import com.systemedebons.bonification.Service.utils.Utils;
import com.systemedebons.bonification.payload.dto.HistoryDTO;
import com.systemedebons.bonification.payload.exception.AccessHistoryException;
import com.systemedebons.bonification.payload.exception.EntityNotFound;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HistoryService {

    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    private final Mapper mapper;
    private HistoryRepository historyRepository;

    private SecurityUtils securityUtils;

    private Utils utils;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<History> getAllHistory() {
        return historyRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Optional<History> getHistoryById(String historyId) {
        History history = historyRepository.findById(historyId).orElseThrow(()-> new EntityNotFound(historyId));
        Transaction transaction = transactionRepository.findById(history.getTransactionId()).orElseThrow();
        if (securityUtils.isClientOfCurrentUser(transaction.getClientLogin()))
            return historyRepository.findById(historyId);
        throw new AccessHistoryException();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<HistoryDTO> getHistoryByUserId(String userId) {
        Optional<User> user = utils.getUser(userId);
        if (user.isPresent())
            return historyRepository.findByUserId(userId).stream().map(mapper::toHistoryDTO).toList();
        throw new EntityNotFound(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public List<HistoryDTO> getHistoryByUserId() {
        Optional<User> user = securityUtils.getCurrentUser();
        return user.map(value -> historyRepository.findByUserId(value.getId()).stream().map(mapper::toHistoryDTO).toList()).orElseGet(List::of);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public HistoryDTO saveHistory(HistoryDTO historyDTO) {
        Transaction transaction = transactionRepository.findById(historyDTO.getTransactionId()).orElseThrow();
        if (securityUtils.isClientOfCurrentUser(transaction.getClientLogin()) || securityUtils.isCurrentUserAdmin()) {
            return mapper.toHistoryDTO(historyRepository.save(mapper.toHistory(historyDTO)));
        }
        throw new AccessHistoryException();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteHistory(String historyId) {
        History history = historyRepository.findById(historyId).orElse(new History());
        Transaction transaction = transactionRepository.findById(history.getTransactionId()).orElseThrow();
        if (securityUtils.isClientOfCurrentUser(transaction.getClientLogin()) || securityUtils.isCurrentUserAdmin())
            historyRepository.deleteById(historyId);
        throw new AccessHistoryException();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<HistoryDTO> getHistoryByClientId(String clientLogin) {
        if (securityUtils.isClientOfCurrentUser(clientLogin) || securityUtils.isCurrentUserAdmin()) {
            Client client = clientRepository.findByLogin(clientLogin).orElseThrow();

            return historyRepository.findByClientId(client.getId()).stream().map(mapper::toHistoryDTO).toList();
        }
        throw new AccessHistoryException();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Optional<HistoryDTO> getHistoryByTransactionId(String transactionId) {
        Optional<History> history = historyRepository.findById(transactionId);
        return history.map(mapper::toHistoryDTO);
    }
}
