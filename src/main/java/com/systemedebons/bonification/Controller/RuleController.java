package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Rule;
import com.systemedebons.bonification.Service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;


    @GetMapping
    public List<Rule> getAllRules() {
        return ruleService.getAllRules();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rule> getRuleById(@PathVariable String id) {
        Optional<Rule> Rule = ruleService.getRuleById(id);
        return Rule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Rule createRule(@RequestBody Rule rule) {
        return ruleService.saveRule(rule);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Rule> deleteRule(@PathVariable String id) {
        ruleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }

}
