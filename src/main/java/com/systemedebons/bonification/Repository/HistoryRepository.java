package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.History;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends CassandraRepository<History, String> {
    List<History> findByClientId(String clientId);

    List<History> findByUserId(String userId);

    Optional<History> findByTransactionId(String transactionId);

    default Optional<History> findTopByClientIdOrderByDateDesc(String clientId) {
        List<History> findByClientId = findByClientId(clientId);
        if (findByClientId.isEmpty())
            return Optional.empty();
        findByClientId.sort(Comparator.comparing(History::getDate).reversed());;
        return Optional.of(findByClientId.get(0));
    }
}