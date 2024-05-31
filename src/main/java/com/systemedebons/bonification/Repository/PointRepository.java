package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends MongoRepository<Point, String> {
    List<Point> findByUserId(String userId);
}
