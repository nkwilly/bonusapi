package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.BaseRule;
import com.systemedebons.bonification.Repository.BaseRuleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BaseService {
    private final BaseRuleRepository baseRuleRepository;

    public List<BaseRule> getBaseRules() {
        return baseRuleRepository.findAll();
    }

    public BaseRule getBaseRule(String id) {
        return baseRuleRepository.findById(id).orElse(null);
    }

    public BaseRule saveBaseRule(BaseRule baseRule) {
        return baseRuleRepository.save(baseRule);
    }

    public BaseRule updateBaseRule(BaseRule baseRule) {
        return baseRuleRepository.save(baseRule);
    }

    public void deleteBaseRule(String id) {
        baseRuleRepository.deleteById(id);
    }
}
