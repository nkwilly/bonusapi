package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Rule;
import com.systemedebons.bonification.Repository.RuleRepository;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RuleService {


    private final RuleRepository ruleRepository;

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public void addBonificationRule(Rule bonificationRule) {
        // Vérifier que la règle est valide (par exemple, que le montant minimal est inférieur ou égal au montant maximal)
        // Enregistrer la règle dans la base de données
        // Notifier les utilisateurs concernés par la nouvelle règle (par exemple, les administrateurs ou les clients)
    }

    public void updateBonificationRule(String Id, Rule updatedRule) {
        // Vérifier que la règle mise à jour est valide et que l'utilisateur a les droits nécessaires pour la modifier
        // Mettre à jour la règle dans la base de données
        // Notifier les utilisateurs concernés par la modification de la règle
    }

    public void deleteBonificationRule(String Id) {
        // Vérifier que l'utilisateur a les droits nécessaires pour supprimer la règle
        // Supprimer la règle de la base de données
        // Notifier les utilisateurs concernés par la suppression de la règle
    }

    public List<Rule> getAllBonificationRules() {
        // Récupérer toutes les règles de la base de données
        // Retourner la liste des règles
        return List.of();
    }

    public Rule getBonificationRuleById(String Id) {
        // Récupérer la règle correspondant à l'identifiant dans la base de données
        // Retourner la règle ou une erreur si elle n'existe pas
        return null;
    }

    public int calculatePointsToAttribute(BigDecimal amount) {
        // Récupérer toutes les règles de la base de données
        // Parcourir les règles et calculer le nombre de points à attribuer en fonction du montant d'achat
        // Retourner le nombre de points à attribuer
        return 0;
    }
 public List<Rule> getBonificationRules() {
        return ruleRepository.findAll();
 }


 public Rule geBonificationRuleById(String Id) {
        return ruleRepository.findById(Id)
                .orElseThrow(()->new RuntimeException("Bonification rule not found with id:" + Id));

 }

 public Rule createBonificationRule(Rule bonificationRule) {
        return ruleRepository.save(bonificationRule);
 }

 /**
 public Rule updateBonificationRule(String Id, Rule bonificationRule) {

        Rule existingRule = getBonificationRuleById(Id);

        existingRule.setMaxAmount(bonificationRule.getMaxAmount());
        existingRule.setMinAmount(bonificationRule.getMinAmount());
     return ruleRepository.save(existingRule);
 }
 public void deleteBonificationRule(String Id) {
     ruleRepository.deleteById(Id);
 }  
**/

}
