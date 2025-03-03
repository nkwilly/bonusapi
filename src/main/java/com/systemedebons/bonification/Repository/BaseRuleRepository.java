package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.BaseRule;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface BaseRuleRepository extends CassandraRepository<BaseRule, String> {
    BaseRule findByUserId(String userId);
}

