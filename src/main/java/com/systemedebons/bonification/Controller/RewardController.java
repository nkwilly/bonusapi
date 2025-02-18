package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Reward;
import com.systemedebons.bonification.Entity.Rewards;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.RewardRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.RewardService;
import com.systemedebons.bonification.payload.dto.RewardDTO;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
@AllArgsConstructor
@RequestMapping("api/rewards")
public class RewardController {

    private final RewardRepository rewardRepository;
    private RewardService rewardService;

    private SecurityUtils securityUtils;



    @GetMapping("reward-list")
    public ResponseEntity<List<Reward>> getRewards() {
        return ResponseEntity.ok(rewardService.getAllReward());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reward> getRewardById(@PathVariable String id) {
        Optional<Reward> reward = rewardService.getRewardById(id);
        return reward.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Reward createReward(@RequestBody RewardDTO reward) {
        return rewardService.saveReward(reward);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable String id) {
        rewardService.deleteReward(id);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/exchange-rewards/{id}")
    public ResponseEntity<Reward> exchangesPoints(@PathVariable String id) {
        String userId = securityUtils.getCurrentUser().orElseThrow().getId();
        Optional<Reward> reward = rewardService.exchangePoints(userId, id);
        return reward.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}