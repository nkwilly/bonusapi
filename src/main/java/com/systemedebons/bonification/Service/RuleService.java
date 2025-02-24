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

@Service
@AllArgsConstructor
public class RuleService {

    private static final Logger log = LoggerFactory.getLogger(RuleService.class);

    private final RuleRepository ruleRepository;

    private final  UserRepository userRepository;

    private final RewardRepository rewardRepository;

    private final SecurityUtils securityUtils;
    private final BaseRuleRepository baseRuleRepository;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<Rule> getAllRules() {
        if (securityUtils.isCurrentUserAdmin())
            return ruleRepository.findAll();
        User currentUser = securityUtils.getCurrentUser().orElseThrow(() -> new EntityNotFound("User Not Found"));
        return ruleRepository.findRuleByUser(currentUser);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Optional<Rule> getRuleById(String  ruleId) {
        Rule rule = ruleRepository.findById(ruleId).orElseThrow(() -> new EntityNotFound(ruleId));
        if (securityUtils.isUserOfCurrentUser(rule.getUser().getId()) || securityUtils.isCurrentUserAdmin())
            return ruleRepository.findById(ruleId);
        throw new AccessRulesException();
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Rule> getRulesByUserId(String userId) {
        return ruleRepository
                .findRuleByUser(
                        userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFound("User Not Found")));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Rule saveRule(Rule rule) {
        if (rule.getUser() == null)
            rule.setUser(securityUtils.getCurrentUser().orElseThrow());
        if (rule.getAmountMax() <= rule.getAmountMin())
            throw new RuntimeException("Amount max should be greater than amount min");
        log.info("user = {}", rule.getUser());
        if (securityUtils.isUserOfCurrentUser(rule.getUser().getId()) || securityUtils.isCurrentUserAdmin()) {
            List<Rule> rules = ruleRepository.findRuleByUser(rule.getUser());
            log.info("existing rules = {}", rules);
            rules.forEach(saveRule -> {
                if (rule.getAmountMin() <= saveRule.getAmountMax() && rule.getAmountMax() >=  saveRule.getAmountMin())
                    throw new RuntimeException("Save this rule is not possible");
            });
            return ruleRepository.save(rule);
        }
        throw new AccessRulesException();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteRule(String ruleId) {
        Rule rule = ruleRepository.findById(ruleId).orElseThrow(() -> new EntityNotFound(ruleId));
        if (securityUtils.isUserOfCurrentUser(rule.getUser().getId()) || securityUtils.isCurrentUserAdmin())
            ruleRepository.deleteById(ruleId);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public boolean estUneTransactionEligible(Transaction transaction) {
        if (!securityUtils.isClientOfCurrentUser(transaction.getClient().getLogin()) && !securityUtils.isCurrentUserAdmin())
            throw new AccessRulesException();
        User currentUser = securityUtils.getCurrentUser().orElseThrow(() -> new EntityNotFound("current user"));
        List<Rule> rules = ruleRepository.findRuleByUser(currentUser);
         return !rules.stream().map(Rule::getAmountMin)
                .filter(montantMin -> transaction.getAmount() >= montantMin)
                .toList()
                .isEmpty();
    }

    /**
     * Compute the number of points in a transaction.
     * Use amount field of the {@code Transaction} entity.
     * @param transaction transaction entity
     * @return number of points generate by the transaction.
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public int computePoints(Transaction transaction) {
        if (!securityUtils.isClientOfCurrentUser(transaction.getClient().getLogin()) && !securityUtils.isCurrentUserAdmin())
            throw new AccessRulesException();
        List<Rule> rules = ruleRepository.findRuleByUser(securityUtils.getCurrentUser().orElseThrow(() -> new EntityNotFound("current user")));
        return rules.stream()
                .map(rule -> transaction.getAmount() >= rule.getAmountMin() ? rule.getPoints() : 0)
                .toList()
                .stream().filter(elt -> elt != 0)
                .max(Integer::compareTo)
                .orElseThrow(() -> new RuntimeException("Computation errors"));
    }

    /**
     * Compute the number of points generate by certain amount.
     * @param amount amount.
     * @return number of points.
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public int computePoints(double amount) {
        List<Rule> rules = ruleRepository.findRuleByUser(securityUtils.getCurrentUser().orElseThrow(() -> new EntityNotFound("current user")));
        return rules.stream()
                .map(rule -> amount >= rule.getAmountMin() && amount<= rule.getAmountMax() ? rule.getPoints() : 0)
                .max(Integer::compareTo)
                .orElseThrow(() -> new RuntimeException("Computation errors"));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Double getAmountOfPointsForClient(Point point) {
        Optional<Reward> optionalReward = rewardRepository.findByUserId(point.getClient().getUser().getId());
        if (optionalReward.isEmpty())
            throw new RuntimeException("Not Reward defined");
        Reward reward = optionalReward.get();
        return point.getNumber() * reward.getValue();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Double getAmountOfPoints(Integer points) {
        User currentUser = securityUtils.getCurrentUser().orElseThrow(RuntimeException::new);
        BaseRule base = baseRuleRepository.findByUser(currentUser);
        return points * base.getAmount();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public BaseRule createBaseRule(BaseRule baseRule) {
        User currentUser = securityUtils.getCurrentUser().orElseThrow(RuntimeException::new);
        baseRule.setUser(currentUser);
        return baseRuleRepository.save(baseRule);
    }
}