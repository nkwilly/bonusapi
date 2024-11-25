package com.systemedebons.bonification.Controller;


import com.systemedebons.bonification.Entity.Point;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Service.PointService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
@AllArgsConstructor
@RequestMapping("/api/points")
public class PointController {
    private UserRepository  userRepository;

    private PointService pointService;

    @GetMapping
    public List<Point> getAllPoints() {
        return pointService.getAllPoints();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Point> getPointByPointId(@PathVariable String id) {
        Optional<Point> point = pointService.getPointById(id);
        return point.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Point createPoint(@RequestBody Point point) {
        return pointService.savePoint(point);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoint(@PathVariable String id) {
        pointService.deletePoint(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/Solde/{UserId}")
    public ResponseEntity<Integer> getSoldePoint(@PathVariable String clientId) {
        int solde = pointService.getSoldePoints(clientId);
        return ResponseEntity.ok(solde);
    }

    @GetMapping("/user/{ClientId}")
    public List<Point> getPointsByClientId(@PathVariable String ClientId) {
        return pointService.getPointsByClientId(ClientId);
    }

    @GetMapping("/user/all/{userId}")
    public ResponseEntity<List<Point>> getAllPointsByUser(@PathVariable String userId) {
        List<Point> points = pointService.getAllPointsForUser(userId);
        return new ResponseEntity<>(points, HttpStatus.OK);
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<Point>> getAllPointsByUser() {
        List<Point> points = pointService.getAllPointsForUser();
        return new ResponseEntity<>(points, HttpStatus.OK);
    }
}
