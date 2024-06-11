package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends MongoRepository<Reward, String> {
    List<Reward> findByUserId(String userId);
}
