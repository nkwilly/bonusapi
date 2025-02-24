package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.History;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.HistoryRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.utils.Utils;
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
        if (securityUtils.isClientOfCurrentUser(history.getTransaction().getClient().getLogin()))
            return historyRepository.findById(historyId);
        throw new AccessHistoryException();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<History> getHistoryByUserId(String userId) {
        Optional<User> user = utils.getUser(userId);
        if (user.isPresent())
            return historyRepository.findByUserId(userId);
        throw new EntityNotFound(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public List<History> getHistoryByUserId() {
        Optional<User> user = securityUtils.getCurrentUser();
        if (user.isPresent())
            return historyRepository.findByUserId(user.get().getId());
        return List.of();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public History saveHistory(History history) {
        if (securityUtils.isClientOfCurrentUser(history.getTransaction().getClient().getLogin()) || securityUtils.isCurrentUserAdmin())
            return historyRepository.save(history);
        throw new AccessHistoryException();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteHistory(String historyId) {
        History history = historyRepository.findById(historyId).orElse(new History());
        if (securityUtils.isClientOfCurrentUser(history.getTransaction().getClient().getLogin()) || securityUtils.isCurrentUserAdmin())
            historyRepository.deleteById(historyId);
        throw new AccessHistoryException();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<History> getHistoryByclientLogin(String clientLogin) {
        if (securityUtils.isClientOfCurrentUser(clientLogin) || securityUtils.isCurrentUserAdmin()) {
            Client client = clientRepository.findByLogin(clientLogin).orElseThrow();
            return historyRepository.findByTransaction_ClientId(client.getId());
        }
        throw new AccessHistoryException();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Optional<History> getHistoryByTransactionId(String transactionId) {
        return historyRepository.findByTransactionId(transactionId);
    }
}
