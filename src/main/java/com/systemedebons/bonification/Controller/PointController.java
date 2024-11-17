package com.systemedebons.bonification.Controller;


import com.systemedebons.bonification.Entity.Point;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Service.PointService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
@RequestMapping("/api/points")
public class PointController {

    @Autowired
    private UserRepository  userRepository;
    @Autowired
    private PointService pointService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Point> getAllPoints() {
        return pointService.getAllPoints();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Point> getPointById(@PathVariable String id) {
        Optional<Point> point = pointService.getPointById(id);
        return point.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Point createPoint(@RequestBody Point point) {
        return pointService.savePoint(point);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePoint(@PathVariable String id) {
        pointService.deletePoint(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/Solde/{UserId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Integer> getSoldePoint(@PathVariable String UserId) {

        int solde = pointService.getSoldePoints(UserId);

        return ResponseEntity.ok(solde);

    }
    @GetMapping("/user/{ClientId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Point> getPointsByUtilisateurId(@PathVariable String ClientId) {
        return pointService.getPointsByClientId(ClientId);
    }


    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> getPointsOfCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        int solde = pointService.getSoldePoints(user.getId());
        return ResponseEntity.ok(solde);
    }




}
