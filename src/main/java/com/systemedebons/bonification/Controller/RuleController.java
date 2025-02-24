package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.BaseRule;
import com.systemedebons.bonification.Entity.Rule;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.BaseRuleRepository;
import com.systemedebons.bonification.Repository.RuleRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.RuleService;
import com.systemedebons.bonification.Service.utils.Mapper;
import com.systemedebons.bonification.payload.dto.BaseRuleDTO;
import com.systemedebons.bonification.payload.dto.RuleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Api
@RestController
@AllArgsConstructor
@RequestMapping("/api/rules")
@Tag(name = "Rule Controller", description = "Endpoints for managing rules")
public class RuleController {

    private final RuleRepository ruleRepository;
    private final RuleService ruleService;
    private final SecurityUtils securityUtils;
    private final Mapper mapper;
    private final BaseRuleRepository baseRuleRepository;

    @Operation(summary = "Retrieve all rules", description = "Returns a list of all rules.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of rules")
    @GetMapping
    public List<Rule> getAllRules() {
        return ruleService.getAllRules();
    }

    @Operation(summary = "Retrieve a rule by ID", description = "Fetches a rule based on its unique ID.")
    @ApiResponse(responseCode = "200", description = "Rule found")
    @ApiResponse(responseCode = "404", description = "Rule not found")
    @GetMapping("/{id}")
    public ResponseEntity<Rule> getRuleById(@PathVariable String id) {
        Optional<Rule> rule = ruleService.getRuleById(id);
        return rule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Retrieve rules by user ID", description = "Fetches all rules associated with a given user ID.")
    @ApiResponse(responseCode = "200", description = "Rules found")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rule>> getRulesByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(ruleService.getRulesByUserId(userId));
    }

    @Operation(summary = "Create a new rule", description = "Saves a new rule to the database.")
    @ApiResponse(responseCode = "200", description = "Rule successfully created")
    @PostMapping
    public Rule createRule(@RequestBody @Valid RuleDTO dto) {
        Rule rule = mapper.toRule(dto);
        return ruleService.saveRule(rule);
    }

    @Operation(summary = "Retrieve rules by amount", description = "Fetches the highest points associated with rules where the amount is less than the specified value.")
    @ApiResponse(responseCode = "200", description = "Points retrieved")
    @GetMapping("/rules-amount/{amount}")
    public ResponseEntity<Integer> getRulesByAmount(@PathVariable Double amount) {
        User currentUser = securityUtils.getCurrentUser().orElseThrow(() -> new RuntimeException("User not authenticated"));
        List<Rule> rules = ruleRepository.findByUserAndAmountMinLessThan(currentUser, amount);
        Optional<Integer> points = rules.stream().map(Rule::getPoints).max(Integer::compareTo);
        return points.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(0));
    }

    @Operation(summary = "Retrieve amount by points", description = "Fetches the amount associated with a given number of points.")
    @ApiResponse(responseCode = "200", description = "Amount retrieved")
    @GetMapping("/rules-points/{points}")
    public ResponseEntity<Double> getRulesByPoints(@PathVariable Integer points) {
        return ResponseEntity.ok(ruleService.getAmountOfPoints(points));
    }

    @Operation(summary = "Set base rule for point conversion", description = "Set the baseRule")
    @ApiResponse(responseCode = "200", description = "BaseRule create")
    @PostMapping("/rules-points/baseRule")
    public ResponseEntity<BaseRule> createBaseRule(@RequestBody BaseRuleDTO dto) {
        BaseRule baseRule = new BaseRule();
        baseRule.setId(dto.getId());
        baseRule.setAmount(dto.getAmount());
        baseRule.setUser(securityUtils.getCurrentUser().orElseThrow(() -> new RuntimeException("User not authenticated")));
        return ResponseEntity.ok(ruleService.createBaseRule(baseRule));
    }

    @Operation(summary = "Get base ruel", description = "Get the baseRule")
    @ApiResponse(responseCode = "200", description = "BaseRule get")
    @GetMapping("/rules-points/baseRule")
    public ResponseEntity<BaseRule> getBaseRule() {
        return ResponseEntity.ok(baseRuleRepository.findByUser(securityUtils.getCurrentUser().orElseThrow()));
    }

    @Operation(summary = "Delete a rule", description = "Deletes a rule using its ID.")
    @ApiResponse(responseCode = "204", description = "Rule successfully deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Rule> deleteRule(@PathVariable String id) {
        ruleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
}

