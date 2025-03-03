package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.*;
import com.systemedebons.bonification.Repository.*;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.payload.exception.AccessRulesException;
import com.systemedebons.bonification.payload.exception.EntityNotFound;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RuleService {

    private static final Logger log = LoggerFactory.getLogger(RuleService.class);

    private final RuleRepository ruleRepository;

    private final  UserRepository userRepository;


    private final SecurityUtils securityUtils;
    private final BaseRuleRepository baseRuleRepository;
    private final ClientRepository clientRepository;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<Rule> getAllRules() {
        if (securityUtils.isCurrentUserAdmin())
            return ruleRepository.findAll();
        User currentUser = securityUtils.getCurrentUser().orElseThrow(() -> new EntityNotFound("User Not Found"));
        return ruleRepository.findRuleByUserId(currentUser.getId());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Optional<Rule> getRuleById(String  ruleId) {
        Rule rule = ruleRepository.findById(ruleId).orElseThrow(() -> new EntityNotFound(ruleId));
        User user = userRepository.findById(rule.getUserId()).orElseThrow(() -> new EntityNotFound("User Not Found"));
        if (securityUtils.isUserOfCurrentUser(user.getId()) || securityUtils.isCurrentUserAdmin())
            return ruleRepository.findById(ruleId);
        throw new AccessRulesException();
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Rule> getRulesByUserId(String userId) {
        return ruleRepository
                .findRuleByUserId(
                        userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFound("User Not Found")).getId());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Rule saveRule(Rule rule) {
        if (rule.getUserId() == null)
            rule.setUserId(securityUtils.getCurrentUser().orElseThrow().getId());
        if (rule.getAmountMax() <= rule.getAmountMin())
            throw new RuntimeException("Amount max should be greater than amount min");
        User user = userRepository.findById(rule.getUserId()).orElseThrow(() -> new EntityNotFound("User Not Found"));
        if (securityUtils.isUserOfCurrentUser(user.getId()) || securityUtils.isCurrentUserAdmin()) {
            List<Rule> rules = ruleRepository.findRuleByUserId(rule.getUserId());
            log.info("existing rules = {}", rules);
            rules.forEach(saveRule -> {
                if (rule.getAmountMin() <= saveRule.getAmountMax() && rule.getAmountMax() >=  saveRule.getAmountMin())
                    throw new RuntimeException("Save this rule is not possible");
            });
            rule.setId(UUID.randomUUID().toString());
            return ruleRepository.save(rule);
        }
        throw new AccessRulesException();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteRule(String ruleId) {
        Rule rule = ruleRepository.findById(ruleId).orElseThrow(() -> new EntityNotFound(ruleId));
        User user = userRepository.findById(rule.getUserId()).orElseThrow(() -> new EntityNotFound("User Not Found"));
        if (securityUtils.isUserOfCurrentUser(user.getId()) || securityUtils.isCurrentUserAdmin())
            ruleRepository.deleteById(ruleId);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public boolean estUneTransactionEligible(Transaction transaction) {
        Client client = clientRepository.findById(transaction.getClientLogin()).orElseThrow();
        if (!securityUtils.isClientOfCurrentUser(client.getId()) && !securityUtils.isCurrentUserAdmin())
            throw new AccessRulesException();
        User currentUser = securityUtils.getCurrentUser().orElseThrow(() -> new EntityNotFound("current user"));
        List<Rule> rules = ruleRepository.findRuleByUserId(currentUser.getId());
         return !rules.stream().map(Rule::getAmountMin)
                .filter(montantMin -> transaction.getAmount() >= montantMin)
                .toList()
                .isEmpty();
    }


    /**
     * Compute the number of points generate by certain amount.
     * @param amount amount.
     * @return number of points.
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public int computePoints(double amount) {
        List<Rule> rules = ruleRepository.findRuleByUserId(securityUtils.getCurrentUser().orElseThrow(() -> new EntityNotFound("current user")).getId());
        return rules.stream()
                .map(rule -> amount >= rule.getAmountMin() && amount<= rule.getAmountMax() ? rule.getPoints() : 0)
                .max(Integer::compareTo)
                .orElseThrow(() -> new RuntimeException("Computation errors"));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Double getAmountOfPointsForClient(Point point) {
        Client client = clientRepository.findById(point.getClientId()).orElseThrow();
        User user = userRepository.findById(client.getUserId()).orElseThrow();
        BaseRule optionalBaseRule = baseRuleRepository.findByUserId(user.getId());
        return point.getNumber() * optionalBaseRule.getAmount();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Double getAmountOfPoints(Integer points) {
        User currentUser = securityUtils.getCurrentUser().orElseThrow(RuntimeException::new);
        BaseRule base = baseRuleRepository.findByUserId(currentUser.getId());
        return points * base.getAmount();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public BaseRule createBaseRule(BaseRule baseRule) {
        User currentUser = securityUtils.getCurrentUser().orElseThrow(RuntimeException::new);
        baseRule.setUserId(currentUser.getId());
        return baseRuleRepository.save(baseRule);
    }
}