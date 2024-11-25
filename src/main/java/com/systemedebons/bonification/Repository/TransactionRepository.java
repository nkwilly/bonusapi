package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findByClientId(String clientId);

    @Query("{client.userId: ?0}")
    List<Transaction> findByUserId(String userId);
}
