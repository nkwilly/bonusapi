package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.*;
import com.systemedebons.bonification.Repository.*;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.utils.Mapper;
import com.systemedebons.bonification.payload.dto.RewardDTO;
import com.systemedebons.bonification.payload.exception.AccessRewardException;
import com.systemedebons.bonification.payload.exception.AccessTransactionException;
import com.systemedebons.bonification.payload.exception.EntityNotFound;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RewardService {

    private final TransactionRepository transactionRepository;
    
    private final RewardRepository rewardRepository;

    private final PointRepository pointRepository;

    private final ClientRepository clientRepository;

    private final SecurityUtils securityUtils;

    private final Mapper mapper;

    private final UserRepository userRepository;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Reward> getAllReward() {
        return rewardRepository.findAll();
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<Reward> getRewardById(String  rewardId) {
        Reward reward = rewardRepository.findById(rewardId).orElseThrow(() -> new EntityNotFound(rewardId));
        if (securityUtils.isUserOfCurrentUser(reward.getUser().getId()) || securityUtils.isCurrentUserAdmin() )
            return rewardRepository.findById(rewardId);
        throw new AccessRewardException();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Reward saveReward(RewardDTO dto) {
        Reward reward = mapper.toReward(dto);
        if (securityUtils.isUserOfCurrentUser(reward.getUser().getId()) || securityUtils.isCurrentUserAdmin()) {
            Optional<Reward> existReward = rewardRepository.findByUserId(reward.getUser().getId());
            if (existReward.isPresent()) {
                Reward newReward = existReward.get();
                newReward.setValue(reward.getValue());
                return rewardRepository.save(newReward);
            }
        }
        throw new AccessRewardException();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteReward(String rewardId) {
        Reward reward = rewardRepository.findById(rewardId).orElseThrow(() -> new EntityNotFound(rewardId));
        if (securityUtils.isUserOfCurrentUser(reward.getUser().getId()) || securityUtils.isCurrentUserAdmin() )
            rewardRepository.deleteById(rewardId);
        throw new AccessRewardException();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<Reward> exchangePoints(String userId, String rewardId) {
        Optional<Reward> rewardOptional = rewardRepository.findById(rewardId);
        if (rewardOptional.isPresent()) {
            Reward reward = rewardOptional.get();
            if (!securityUtils.isUserOfCurrentUser(reward.getUser().getId()) && !securityUtils.isCurrentUserAdmin())
                return Optional.empty();
            //int solde = pointRepository.findByClient_Login(userId)
              //      .stream().mapToInt(Point::getNumber).sum();
            int solde = 10;
            if (solde >= reward.getValue()) {
                Optional<Client> clientOptional = clientRepository.findById(userId);
                if (clientOptional.isPresent()) {
                    Transaction transaction = new Transaction();
                    transaction.setClient(clientOptional.get());
                    transaction.setStatus(Statuts.COMPLETE);
                    transaction.setAmount(0);
                    Client client = clientOptional.get();
                    Point point = new Point();
                    pointRepository.save(point);
                    return Optional.of(reward);
                }
            }
        }
        return Optional.empty() ;
    }
}
