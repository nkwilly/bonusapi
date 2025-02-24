package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.Point;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.PointRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.utils.Utils;
import com.systemedebons.bonification.payload.exception.AccessPointsException;
import com.systemedebons.bonification.payload.exception.UsernameNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PointService {

    private static final Logger log = LoggerFactory.getLogger(PointService.class);
    private final ClientRepository clientRepository;

    private PointRepository pointRepository;

    private UserRepository userRepository;

    private Utils utils;
    
    private SecurityUtils securityUtils;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Point> getAllPoints() {
        return pointRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Optional<Point> getPointById(String clientLogin) {
        if (securityUtils.isClientOfCurrentUser(clientLogin) || securityUtils.isCurrentUserAdmin())
            return pointRepository.findById(clientLogin);
        return Optional.empty();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Optional<Point> createPoint(Point point) {
        if (securityUtils.isClientOfCurrentUser(point.getClient().getLogin()) || securityUtils.isCurrentUserAdmin())
            return Optional.of(pointRepository.save(point));
        throw new AccessPointsException();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Optional<Point> getPointsByClientLogin(String clientLogin) {
        if (securityUtils.isClientOfCurrentUser(clientLogin) || securityUtils.isCurrentUserAdmin()) {
            Client client = clientRepository.findByLogin(clientLogin).orElse(null);
            assert client != null;
            return pointRepository.findByClientId(client.getId());
        }

        throw new AccessPointsException();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Point savePoint(Point point) {
        if (securityUtils.isClientOfCurrentUser(point.getClient().getLogin()) || securityUtils.isCurrentUserAdmin())
            return pointRepository.save(point);
        throw new AccessPointsException();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public void deletePoint(String clientLogin) {
        if (securityUtils.isClientOfCurrentUser(clientLogin) || securityUtils.isCurrentUserAdmin())
            pointRepository.deleteById(clientLogin);
        throw new AccessPointsException();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public int getSoldePoints(@PathVariable  String clientLogin) {
        if (securityUtils.isClientOfCurrentUser(clientLogin) || securityUtils.isCurrentUserAdmin()) {
            Client client = clientRepository.findByLogin(clientLogin).orElseThrow();
            Optional<Point> point = pointRepository.findByClient_Id(client.getId());
            if (point.isPresent())
                return point.get().getNumber();

        }
        return 0;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Point> getAllPointsForUser(String userId) {
        return pointRepository.getAllClientsForUser(userId);
    }
    
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Point> getAllPointsForUser() {
        User currentUser = securityUtils.getCurrentUser().orElseThrow(UsernameNotFoundException::new);
        return pointRepository.getAllClientsForUser(currentUser.getId());
    }
}