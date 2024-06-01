package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Rule;
import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }
    public Optional<Rule> getRuleById(String  id) {
        return ruleRepository.findById(id);
    }

    public Rule saveRule(Rule rule) {
        return ruleRepository.save(rule);
    }

    public void deleteRule(String id) {
        ruleRepository.deleteById(id);
    }

    public boolean estUneTransactionEligible(Transaction transaction) {

        List<Rule> rules = ruleRepository.findAll();
        for (Rule rule : rules) {
            if(transaction.getMontant() >= rule.getMontantMin()){
                return true;
            }
        }
        return false;
    }

    public int calculerPoints(Transaction transaction) {

        List<Rule> rules = ruleRepository.findAll();
        int points = 0;
        for (Rule rule : rules) {
            if(transaction.getMontant() >= rule.getMontantMin()){
                points += rule.getPoints();
            }
        }
        return points;
    }



}
