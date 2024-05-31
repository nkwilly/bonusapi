package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Point;
import com.systemedebons.bonification.Entity.Reward;
import com.systemedebons.bonification.Repository.PointRepository;
import com.systemedebons.bonification.Repository.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RewardService {

    @Autowired
    private RewardRepository rewardRepository;
    @Autowired
    private PointRepository pointRepository;

    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }

    public Optional<Reward> getRewardById(String  id) {
        return rewardRepository.findById(id);
    }

    public Reward saveReward(Reward reward) {
        return rewardRepository.save(reward);

    }

    public void deleteReward(String id) {
        rewardRepository.deleteById(id);
    }

    public boolean exchangePoints(String UserId, String id){
        Optional<Reward> rewardOptional = rewardRepository.findById(id);
        if (rewardOptional.isPresent()) {
            Reward reward = rewardOptional.get();
            int solde = pointRepository.findByUserId(UserId)
                                       .stream().mapToInt(Point::getNombre).sum();

            if(solde >= reward.getPoints()){
                Point point = new Point();
                point.setUserId(UserId);
                point.setNombre(-reward.getPoints());
                pointRepository.save(point);
                return true;
            }

        }
        return false;
    }





}
