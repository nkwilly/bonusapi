package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Reward;
import com.systemedebons.bonification.Service.RewardService;
import com.systemedebons.bonification.payload.dto.RewardDTO;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
@AllArgsConstructor
@RequestMapping("api/rewards")
public class RewardController {

    RewardService rewardService;

    //obtenir la liste des  récompenses
    @GetMapping("reward-list")
    public ResponseEntity<List<Reward>> getRewards() {
        return ResponseEntity.ok(rewardService.getAllRewards());
    }

    @GetMapping("reward-user")
    public ResponseEntity<List<Reward>> getRewardsByUserId(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(rewardService.getAllRewardsByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reward> getRewardById(@PathVariable String id) {
        Optional<Reward> reward = rewardService.getRewardById(id);
        return reward.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("create-reward")
    public Reward createReward(@RequestBody RewardDTO reward) {
        return rewardService.saveReward(reward);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable String id) {
        rewardService.deleteReward(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/echanger/{UserId}/{id}")
    public ResponseEntity<Void> exchangesPoints(@PathVariable String UserId, @PathVariable String id) {
        boolean success = rewardService.exchangePoints(UserId, id);
        if (success)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }
}