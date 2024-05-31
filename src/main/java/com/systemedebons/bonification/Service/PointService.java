package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Point;
import com.systemedebons.bonification.Repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointService {


    @Autowired
    private PointRepository pointRepository;

    public List<Point> getAllPoints() {
        return pointRepository.findAll();
    }

    public Optional<Point> getPointById(String id) {
        return pointRepository.findById(id);

    }

    public Point savePoint(Point point) {
        return pointRepository.save(point);
    }

    public void deletePoint(String id) {
        pointRepository.deleteById(id);
    }



}
