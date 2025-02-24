package com.systemedebons.bonification.Controller;


import com.systemedebons.bonification.Entity.Point;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Service.PointService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
@AllArgsConstructor
@RequestMapping("/api/points")
@Tag(name = "Point Controller", description = "Endpoints for managing points")
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);
    private UserRepository userRepository;
    private PointService pointService;

    @Operation(summary = "Retrieve all points", description = "Returns a list of all points.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of points")
    @GetMapping
    public List<Point> getAllPoints() {
        return pointService.getAllPoints();
    }

    @Operation(summary = "Retrieve a point by ID", description = "Fetches a point based on its unique ID.")
    @ApiResponse(responseCode = "200", description = "Point found")
    @ApiResponse(responseCode = "404", description = "Point not found")
    @GetMapping("/{id}")
    public ResponseEntity<Point> getPointByPointId(@PathVariable String id) {
        Optional<Point> point = pointService.getPointById(id);
        return point.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new point", description = "Saves a new point to the database.")
    @ApiResponse(responseCode = "200", description = "Point successfully created")
    @PostMapping
    public Point createPoint(@RequestBody Point point) {
        return pointService.savePoint(point);
    }

    @Operation(summary = "Delete a point", description = "Deletes a point using its ID.")
    @ApiResponse(responseCode = "204", description = "Point successfully deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoint(@PathVariable String id) {
        pointService.deletePoint(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieve point balance", description = "Fetches the point balance for a given client login.")
    @ApiResponse(responseCode = "200", description = "Point balance retrieved")
    @GetMapping("/solde/{clientLogin}")
    public ResponseEntity<Integer> getSoldePoint(@PathVariable String clientLogin) {
        int solde = pointService.getSoldePoints(clientLogin);
        return ResponseEntity.ok(solde);
    }

    @Operation(summary = "Retrieve points by client login", description = "Fetches all points associated with a given client login.")
    @ApiResponse(responseCode = "200", description = "Points retrieved")
    @ApiResponse(responseCode = "404", description = "Points not found")
    @GetMapping("/user/{clientLogin}")
    public ResponseEntity<Point> getPointsByClientLogin(@PathVariable String clientLogin) {
        return pointService.getPointsByClientLogin(clientLogin).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Retrieve all points by user ID", description = "Fetches all points for a given user ID.")
    @ApiResponse(responseCode = "200", description = "Points retrieved")
    @GetMapping("/user/all/{userId}")
    public ResponseEntity<List<Point>> getAllPointsByUser(@PathVariable String userId) {
        List<Point> points = pointService.getAllPointsForUser(userId);
        return new ResponseEntity<>(points, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve all points for the current user", description = "Fetches all points associated with the currently authenticated user.")
    @ApiResponse(responseCode = "200", description = "Points retrieved")
    @GetMapping("/user/all")
    public ResponseEntity<List<Point>> getAllPointsByUser() {
        List<Point> points = pointService.getAllPointsForUser();
        return new ResponseEntity<>(points, HttpStatus.OK);
    }
}
