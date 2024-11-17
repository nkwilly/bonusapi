package com.systemedebons.bonification.Service;


import com.systemedebons.bonification.Security.Jwt.JwtUtils;
import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtils jwtUtils;


    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(String id) {

        return clientRepository.findById(id);

    }


    public  Client saveClient(Client client) {

        // Vérifier si l'utilisateur (API) existe
       // Optional<User> optionalUser = userRepository.findByUserId(userId);
        //if (!optionalUser.isPresent()) {
        //    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API utilisateur introuvable !");
        //}

        // Vérifier si le client existe déjà
        //if (clientRepository.existsByClientId(clientId)) {
        //    return ResponseEntity.status(HttpStatus.CONFLICT).body("Client déjà existant !");
        //}

        Optional<Client> existingClient = clientRepository.findById(client.getId());
        if (existingClient.isPresent()) {
            throw new IllegalArgumentException("Le client existe deja !");
        }

        if(client.getUserId() != null && !client.getUserId().isEmpty()) {
            Client clientSaved = clientRepository.save(client);
            return clientSaved;
        } else{
            throw new IllegalArgumentException("id de l'API qui a stocke ce client ne peut pas être null ou  vide");
        }
    }


    public void deleteClient(String id) {
        clientRepository.deleteById(id);
    }



    public Optional<Client> updateClient(String id, Client client) {
        Optional<Client> existingClient = clientRepository.findById(id);
        if (existingClient.isPresent()) {
            Client updatedClient = existingClient.get();
            updatedClient.setUserId(client.getUserId());
            updatedClient.setUsername(client.getUsername());
            clientRepository.save(updatedClient);
            return Optional.of(updatedClient);
        } else {
            return Optional.empty();
        }
    }


  }
