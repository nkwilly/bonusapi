package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Historique;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueRepository extends MongoRepository<Historique, String> {
}
