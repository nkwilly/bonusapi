package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointRepository extends MongoRepository<Point, String> {
    //List<Point> findByClient_Login(String clientLogin);

    @Query("{'client.id':  ?0}")
    List<Point> getAllClientsForUser(String userId);

   Optional<Point> findByClient_Id(String transactionClientId);

   Optional<Point> findByClientId(String id);
}
