package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByClient_User_Id(String clientUserId);

    @Query("{ 'client.user' : ?0 }")
    List<Transaction> findByClient_User(User user);

    @Query("{'client.user.id':  ?0}")
    List<Transaction> findByClient_UserId(String clientUserId);
}