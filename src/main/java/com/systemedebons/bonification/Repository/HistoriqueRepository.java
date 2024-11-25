package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Historique;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueRepository extends MongoRepository<Historique, String> {

    List<Historique> findByClientId(String clientId);

    @Query("{'client.userId': ?0}")
    List<Historique> findByUserId(String userId);
}
