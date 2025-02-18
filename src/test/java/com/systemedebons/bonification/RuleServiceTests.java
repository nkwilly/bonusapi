package com.systemedebons.bonification;

import com.systemedebons.bonification.Entity.Rule;
import com.systemedebons.bonification.Entity.Transaction;
import com.systemedebons.bonification.Repository.RuleRepository;
import com.systemedebons.bonification.Service.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RuleServiceTests {

    @InjectMocks
    private RuleService ruleService;

    @Mock
    private RuleRepository ruleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }



    @Test
    void testEstTransactionEligible(){
        Rule rule = new Rule();
        rule.setAmountMin(50.0f);
        when(ruleRepository.findAll()).thenReturn(Collections.singletonList(rule));

        Transaction transaction = new Transaction();
        transaction.setAmount(100.0f);
        assertTrue(ruleService.estUneTransactionEligible(transaction));

        transaction.setAmount(30.0f);
        assertFalse(ruleService.estUneTransactionEligible(transaction));
    }



    @Test
    void testSaveRule(){
        Rule rule = new Rule();
        when(ruleRepository.save(any())).thenReturn(rule);


        Rule savedRule = ruleService.saveRule(rule);
        assertNotNull(savedRule);
        verify(ruleRepository, times(1)).save(rule);


    }


    @Test
    void testDeleteRule(){
        doNothing().when(ruleRepository).deleteById("1");

        ruleService.deleteRule("1");
        verify(ruleRepository, times(1)).deleteById("1");
    }













}
