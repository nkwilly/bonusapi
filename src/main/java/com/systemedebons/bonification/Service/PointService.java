package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Point;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.PointRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.utils.Utils;
import com.systemedebons.bonification.payload.exception.AccessPointsException;
import com.systemedebons.bonification.payload.exception.UsernameNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class PointService {

    private static final Logger log = LoggerFactory.getLogger(PointService.class);

    private PointRepository pointRepository;

    private UserRepository userRepository;

    private Utils utils;
    
    private SecurityUtils securityUtils;

    @PreAuthorize("hasRole('ADMIN')")
    public List<Point> getAllPoints() {
        return pointRepository.findAll();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<Point> getPointById(String clientId) {
        if (securityUtils.isClientOfCurrentUser(clientId) || securityUtils.isCurrentUserAdmin())
            return pointRepository.findById(clientId);
        return Optional.empty();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<Point> createPoint(Point point) {
        if (securityUtils.isClientOfCurrentUser(point.getClient().getId()) || securityUtils.isCurrentUserAdmin())
            return Optional.of(pointRepository.save(point));
        throw new AccessPointsException();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Point> getPointsByClientId(String clientId) {
        if (securityUtils.isClientOfCurrentUser(clientId) || securityUtils.isCurrentUserAdmin())
            return pointRepository.findByClientId(clientId);
        throw new AccessPointsException();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Point savePoint(Point point) {
        if (securityUtils.isClientOfCurrentUser(point.getClient().getId()) || securityUtils.isCurrentUserAdmin())
            return pointRepository.save(point);
        throw new AccessPointsException();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void deletePoint(String clientId) {
        if (securityUtils.isClientOfCurrentUser(clientId) || securityUtils.isCurrentUserAdmin())
            pointRepository.deleteById(clientId);
        throw new AccessPointsException();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public int getSoldePoints(String clientId) {
        if (securityUtils.isClientOfCurrentUser(clientId) || securityUtils.isCurrentUserAdmin()) {
            List<Point> points = pointRepository.findByClientId(clientId);
            return points.stream().mapToInt(Point::getNombre).sum();
        }
        throw new AccessPointsException();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Point> getAllPointsForUser(String userId) {
        return pointRepository.getAllClientsForUser(userId);
    }
    
    @PreAuthorize("hasRole('USER')")
    public List<Point> getAllPointsForUser() {
        User currentUser = securityUtils.getCurrentUser().orElseThrow(UsernameNotFoundException::new);
        return pointRepository.getAllClientsForUser(currentUser.getId());
    }
}