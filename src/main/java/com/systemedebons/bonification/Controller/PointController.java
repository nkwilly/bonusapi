package com.systemedebons.bonification.Controller;


import com.systemedebons.bonification.Entity.Point;
import com.systemedebons.bonification.Service.PointService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
@RequestMapping("/api/points")
public class PointController {


    @Autowired
    private PointService pointService;

    @GetMapping
    public List<Point> getAllPoints() {
        return pointService.getAllPoints();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Point> getPointById(@PathVariable String id) {
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







}
