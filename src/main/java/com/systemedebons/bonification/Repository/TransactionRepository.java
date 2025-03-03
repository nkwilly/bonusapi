package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Entity.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CassandraRepository<Transaction, String> {
    List<Transaction> findByUserId(String userId);
}