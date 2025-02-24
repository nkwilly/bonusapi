package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.History;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends MongoRepository<History, String> {

    List<History> findByTransaction_ClientId(String clientId);

    @Query("{'client.userId': ?0}")
    List<History> findByUserId(String userId);

    Optional<History> findByTransactionId(String transactionId);

    Optional<History> findTopByOrderByDateDesc();

    Optional<History> findTopByTransaction_Client_LoginOrderByDateDesc(String login);

    Optional<History> findTopByTransaction_ClientOrderByDateDesc(Client client);
}
