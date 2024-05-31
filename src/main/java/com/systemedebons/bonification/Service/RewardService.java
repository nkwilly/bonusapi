package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Reward;
import com.systemedebons.bonification.Repository.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RewardService {

    @Autowired
    private RewardRepository rewardRepository;

    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }

    public Optional<Reward> getRewardById(String  id) {
        return rewardRepository.findById(id);
    }

    public Reward saveReward(Reward reward) {
        return rewardRepository.save(reward);

    }

    public void deleteReward(Reward reward) {
        rewardRepository.delete(reward);
    }




}
