package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.BaseRule;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Service.BaseService;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BaseRuleRepository extends MongoRepository<BaseRule, String> {
    public BaseRule findByUserId(String userId);

    public BaseRule findByUser(User user);
}
