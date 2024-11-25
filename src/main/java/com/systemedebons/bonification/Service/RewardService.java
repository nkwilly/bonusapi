package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.Point;
import com.systemedebons.bonification.Entity.Reward;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.PointRepository;
import com.systemedebons.bonification.Repository.RewardRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.utils.Mapper;
import com.systemedebons.bonification.payload.dto.RewardDTO;
import com.systemedebons.bonification.payload.exception.AccessRewardException;
import com.systemedebons.bonification.payload.exception.EntityNotFound;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RewardService {

    private RewardRepository rewardRepository;

    private PointRepository pointRepository;

    private ClientRepository clientRepository;

    private SecurityUtils securityUtils;

    private Mapper mapper;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Reward> getAllRewardsByUserId(String userId) {
        return rewardRepository.findByClientUserId(userId);
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
        if (securityUtils.isUserOfCurrentUser(reward.getUser().getId()) || securityUtils.isCurrentUserAdmin() )
            return rewardRepository.save(reward);
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
    public boolean exchangePoints(String userId, String rewardId) {
        Optional<Reward> rewardOptional = rewardRepository.findById(rewardId);
        if (rewardOptional.isPresent()) {
            Reward reward = rewardOptional.get();
            if (!securityUtils.isUserOfCurrentUser(reward.getUser().getId()) && !securityUtils.isCurrentUserAdmin())
                return false;
            int solde = pointRepository.findByClientId(userId)
                    .stream().mapToInt(Point::getNombre).sum();
            if (solde >= reward.getPoints()) {
                Optional<Client> clientOptional = clientRepository.findById(userId);
                if (clientOptional.isPresent()) {
                    Client client = clientOptional.get();
                    Point point = new Point();
                    point.setClient(client);
                    point.setNombre(-reward.getPoints());
                    point.setDate(LocalDate.now());
                    pointRepository.save(point);
                    return true;
                }
            }
        }
        return false;
    }
}
