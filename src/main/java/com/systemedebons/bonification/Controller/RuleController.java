package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Rule;
import com.systemedebons.bonification.Service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
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



}
