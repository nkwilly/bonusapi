package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Historique;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.HistoriqueRepository;
import com.systemedebons.bonification.Security.utils.SecurityUtils;
import com.systemedebons.bonification.Service.utils.Utils;
import com.systemedebons.bonification.payload.exception.AccessHistoriqueException;
import com.systemedebons.bonification.payload.exception.EntityNotFound;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HistoriqueService {

    private HistoriqueRepository historiqueRepository;

    private SecurityUtils securityUtils;

    private Utils utils;

    @PreAuthorize("hasRole('ADMIN')")
    public List<Historique> getAllHistoriques() {
        return historiqueRepository.findAll();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<Historique> getHistoriqueById(String historiqueId) {
        Historique historique = historiqueRepository.findById(historiqueId).orElseThrow(()-> new EntityNotFound(historiqueId));
        if (securityUtils.isClientOfCurrentUser(historique.getClient().getId()))
            return historiqueRepository.findById(historiqueId);
        throw new AccessHistoriqueException();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Historique> getHistoriquesByUserId(String userId) {
        Optional<User> user = utils.getUser(userId);
        if (user.isPresent())
            return historiqueRepository.findByUserId(userId);
        throw new EntityNotFound(userId);
    }

    @PreAuthorize("hasRole('USER')")
    public List<Historique> getHistoriquesByUserId() {
        Optional<User> user = securityUtils.getCurrentUser();
        if (user.isPresent())
            return historiqueRepository.findByUserId(user.get().getId());
        return List.of();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Historique saveHistorique(Historique historique) {
        if (securityUtils.isClientOfCurrentUser(historique.getClient().getId()) || securityUtils.isCurrentUserAdmin())
            return historiqueRepository.save(historique);
        throw new AccessHistoriqueException();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteHistorique(String historiqueId) {
        Historique historique = historiqueRepository.findById(historiqueId).orElse(new Historique());
        if (securityUtils.isClientOfCurrentUser(historique.getClient().getId()) || securityUtils.isCurrentUserAdmin())
            historiqueRepository.deleteById(historiqueId);
        throw new AccessHistoriqueException();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Historique> getHistoriqueByClientId(String clientId) {
        if (securityUtils.isClientOfCurrentUser(clientId) || securityUtils.isCurrentUserAdmin())
         return historiqueRepository.findByClientId(clientId);
        throw new AccessHistoriqueException();
    }
}
