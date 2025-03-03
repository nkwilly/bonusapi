package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Rule;
import com.systemedebons.bonification.Entity.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RuleRepository extends CassandraRepository<Rule, String> {
    List<Rule> findRuleByUserId(String userId);

    List<Rule> findByUserIdAndAmountMinLessThan(String userId, double amount);

    @Query("SELECT * FROM rules WHERE user_id=?0 AND amount_max<?1 LIMIT ?2 ALLOW FILTERING")
    Optional<Rule> findFirstByUserIdAndAmountMinLessThan(String userId, double amount, int limit);
}
