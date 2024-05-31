package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends MongoRepository<Reward, String> {
}
