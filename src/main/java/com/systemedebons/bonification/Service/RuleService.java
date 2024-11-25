package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Rule;
import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.RuleRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.payload.exception.AccessRulesException;
import com.systemedebons.bonification.payload.exception.EntityNotFound;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RuleService {

    private static final Logger log = LoggerFactory.getLogger(RuleService.class);

    private RuleRepository ruleRepository;

    private UserRepository userRepository;

    private SecurityUtils securityUtils;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public List<Rule> getAllRules() {
        if (securityUtils.isCurrentUserAdmin())
            return ruleRepository.findAll();
        User currentUser = securityUtils.getCurrentUser().orElseThrow(() -> new EntityNotFound("User Not Found"));
        return ruleRepository.findRuleByUser(currentUser);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Optional<Rule> getRuleById(String  ruleId) {
        Rule rule = ruleRepository.findById(ruleId).orElseThrow(() -> new EntityNotFound(ruleId));
        if (securityUtils.isUserOfCurrentUser(rule.getUser().getId()) || securityUtils.isCurrentUserAdmin())
            return ruleRepository.findById(ruleId);
        throw new AccessRulesException();
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Rule> getRulesByUserId(String userId) {
        log.debug("Bon je suis là!!!");
        return ruleRepository.findRuleByUser(userRepository.findById(userId).orElseThrow(() -> new EntityNotFound("User Not Found")));
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Rule saveRule(Rule rule) {
        if (securityUtils.isUserOfCurrentUser(rule.getUser().getId()) || securityUtils.isCurrentUserAdmin())
            return ruleRepository.save(rule);
        throw new AccessRulesException();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public void deleteRule(String ruleId) {
        Rule rule = ruleRepository.findById(ruleId).orElseThrow(() -> new EntityNotFound(ruleId));
        if (securityUtils.isUserOfCurrentUser(rule.getUser().getId()) || securityUtils.isCurrentUserAdmin())
            ruleRepository.deleteById(ruleId);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public boolean estUneTransactionEligible(Transaction transaction) {
        if (!securityUtils.isClientOfCurrentUser(transaction.getClient().getId()) && !securityUtils.isCurrentUserAdmin())
            throw new AccessRulesException();
        User currentUser = securityUtils.getCurrentUser().orElseThrow(() -> new EntityNotFound("current user"));
        List<Rule> rules = ruleRepository.findRuleByUser(currentUser);
         return !rules.stream().map(Rule::getMontantMin)
                .filter(montantMin -> transaction.getMontant() >= montantMin)
                .toList()
                .isEmpty();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public int calculerPoints(Transaction transaction) {
        if (!securityUtils.isClientOfCurrentUser(transaction.getClient().getId()) && !securityUtils.isCurrentUserAdmin())
            throw new AccessRulesException();
        List<Rule> rules = ruleRepository.findRuleByUser(securityUtils.getCurrentUser().orElseThrow(() -> new EntityNotFound("current user")));
        int points = rules.stream()
                .map(rule -> transaction.getMontant() >= rule.getMontantMin() ? rule.getPoints() : 0)
                .toList()
                .stream().filter(elt -> elt != 0)
                .max(Integer::compareTo)
                .orElseThrow(() -> new RuntimeException("Computation errors"));
        log.debug("points: {}", points);
        return points;
    }
}