package com.systemedebons.bonification.Service;


import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.payload.exception.AccessClientException;
import com.systemedebons.bonification.payload.exception.DuplicateException;
import com.systemedebons.bonification.payload.exception.UsernameNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
    private ClientRepository clientRepository;

    private SecurityUtils securityUtils;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public List<Client> getAllClients() {
        if (securityUtils.isCurrentUserAdmin())
            return clientRepository.findAll();
        User currentUser = securityUtils.getCurrentUser().orElseThrow(() -> new AccessDeniedException("You are not logged in"));
        return clientRepository.findByUserId(currentUser.getId());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Client> getAllClientsByUser(String userId) {
        return clientRepository.findByUserId(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Client> getAllClientsByUser() {
        User user = securityUtils.getCurrentUser().orElseThrow(UsernameNotFoundException::new);
        return clientRepository.findByUserId(user.getId());
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Optional<Client> getClientById(String clientId) {
        if (securityUtils.isClientOfCurrentUser(clientId) || securityUtils.isCurrentUserAdmin())
            return clientRepository.findById(clientId);
        throw new AccessClientException();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Optional<Client> createClient(String clientName) {
        Client client = new Client();
        client.setClientName(clientName);
        String userId = securityUtils.getCurrentUser().orElseThrow(UsernameNotFoundException::new).getId();
        client.setUserId(userId);
        if (clientRepository.findByClientNameAndUserId(clientName, userId).isEmpty())
            return Optional.of(clientRepository.save(client));
        throw new DuplicateException("clientName : " + clientName + ", for userId : " + userId);
    }

    /*
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public  Client saveClient(Client client) {
        if (!securityUtils.isClientOfCurrentUser(client.getId()) && !securityUtils.isCurrentUserAdmin())
            throw new AccessClientException();
        Optional<Client> existingClient = clientRepository.findById(client.getId());
        if(existingClient.isPresent()) {
            throw new IllegalArgumentException("Le client existe deja !");
        }
        if(client.getUserId() != null && !client.getUserId().isEmpty()) {
            return clientRepository.save(client);
        } else{
            throw new IllegalArgumentException("id de l'API qui a stocke ce client ne peut pas être null ou  vide");
        }
    }
     */

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public void delete(String clientId) {
        if (securityUtils.isClientOfCurrentUser(clientId) || securityUtils.isCurrentUserAdmin())
            clientRepository.deleteById(clientId);
        else throw new AccessClientException();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Optional<Client> update(Client client) {
        if (!securityUtils.isClientOfCurrentUser(client.getId()) && !securityUtils.isCurrentUserAdmin())
            throw new AccessClientException();
        Optional<Client> existingClient = clientRepository.findById(client.getId());
        if (existingClient.isPresent()) {
            Client updatedClient = existingClient.get();
            updatedClient.setClientName(client.getClientName());
            clientRepository.save(updatedClient);
            return Optional.of(updatedClient);
        }
        else return Optional.empty();
    }
}