package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Reward;
import com.systemedebons.bonification.Service.RewardService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Api
@RestController
@RequestMapping("api/rewards")
public class RewardController {


    @Autowired
    RewardService rewardService;

    //obtenir la liste des  récompenses
    @GetMapping("reward-list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Reward> getRewards() {
        return rewardService.getAllRewards();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Reward> getRewardById(@PathVariable String id) {
        Optional<Reward> reward = rewardService.getRewardById(id);
        return reward.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }
    @PostMapping("create-reward")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Reward createReward(@RequestBody Reward reward) {
        return rewardService.saveReward(reward);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReward(@PathVariable String id) {
        rewardService.deleteReward(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/echanger/{UserId}/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> exchangesPoints(@PathVariable String UserId, @PathVariable  String id) {
        boolean success = rewardService.exchangePoints(UserId, id);

        if (success) {
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }




        }
