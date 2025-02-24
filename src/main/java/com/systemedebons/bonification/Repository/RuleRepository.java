package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Rule;
import com.systemedebons.bonification.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RuleRepository extends MongoRepository<Rule, String> {
    List<Rule> findRuleByUser(User user);

    List<Rule> findByUserAndAmountMinLessThan(User user, double amount);

    Optional<Rule> findFirstByUserAndAmountMinLessThan(User user, double amount);
}
