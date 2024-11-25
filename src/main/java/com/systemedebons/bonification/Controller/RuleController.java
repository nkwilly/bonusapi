package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Rule;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.RuleService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Api
@RestController
@AllArgsConstructor
@RequestMapping("/api/rules")
public class RuleController {

    private RuleService ruleService;

    private SecurityUtils securityUtils;

    @GetMapping
    public List<Rule> getAllRules() {
        return ruleService.getAllRules();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rule> getRuleById(@PathVariable String id) {
        Optional<Rule> Rule = ruleService.getRuleById(id);
        return Rule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rule>> getRulesByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(ruleService.getRulesByUserId(userId));
    }

    @PostMapping
    public Rule createRule(@RequestBody Rule rule) {
        User currentUser = securityUtils.getCurrentUser().orElseThrow(() -> new RuntimeException("User not authenticate"));
        rule.setUser(currentUser);
        return ruleService.saveRule(rule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Rule> deleteRule(@PathVariable String id) {
        ruleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
}
