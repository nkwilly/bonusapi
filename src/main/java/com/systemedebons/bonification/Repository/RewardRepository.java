package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Reward;
import com.systemedebons.bonification.Entity.Rewards;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface RewardRepository extends MongoRepository<Reward, String> {
    @Query("{'client.userId': ?0}")
    List<Rewards> findByClientUserId(String userId);

    //Optional<Reward> findByUserIdAndPoints(String userId, int points);

    Optional<Reward> findByUserId(String userId);
}
