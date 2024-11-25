package com.systemedebons.bonification.Service.utils;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.Reward;
import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.TransactionRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.payload.dto.RewardDTO;
import com.systemedebons.bonification.payload.exception.EntityNotFound;
import com.systemedebons.bonification.payload.dto.TransactionDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Mapper {
    TransactionRepository transactionRepository;

    ClientRepository clientRepository;

    UserRepository userRepository;

    public Transaction toTransaction(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setMontant(dto.getMontant());
        transaction.setType(dto.getType());
        transaction.setStatut(dto.getStatus());
        Client client = clientRepository.findById(dto.getClientId()).orElseThrow(() -> new EntityNotFound("Client not found"));
        transaction.setClient(client);
        return transaction;
    }

    public Reward toReward(RewardDTO dto) {
        Reward reward = new Reward();
        reward.setNom(dto.getNom());
        reward.setDescription(dto.getDescription());
        reward.setPoints(dto.getPoints());
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new EntityNotFound("Client not found"));
        reward.setUser(user);
        return reward;
    }
}
