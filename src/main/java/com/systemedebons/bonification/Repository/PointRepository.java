package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Point;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointRepository extends CassandraRepository<Point, String> {

    List<Point> getPointByUserId(String userId);

   Optional<Point> findByClientId(String transactionClientId);
}
