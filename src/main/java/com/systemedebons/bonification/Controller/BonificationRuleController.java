package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Rule;
import com.systemedebons.bonification.Service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bonification-rules")
public class BonificationRuleController {


  /**  @Autowired
    private RuleService ruleService;


    @GetMapping
    public List<Rule> getAllBonificationRules() {
        return RuleService.getAllBonificationRules();
    }

    @GetMapping("/{id}")
    public Rule getBonificationRuleById(@PathVariable Long id) {
        return RuleService.getBonificationRuleById(id);
    }

    @PostMapping
    public Rule createBonificationRule(@RequestBody Rule rule) {
        return RuleService.createBonificationRule(rule);
    }

   @PutMapping("/{id}")
    public Rule updateBonificationRule(@PathVariable Long id, @RequestBody Rule bonificationRule) {
        return RuleService.updateBonificationRule(id, bonificationRule);
    }

    @DeleteMapping("/{id}")
    public void deleteBonificationRule(@PathVariable Long id) { RuleService.deleteBonificationRule(id);
    }**/


}
