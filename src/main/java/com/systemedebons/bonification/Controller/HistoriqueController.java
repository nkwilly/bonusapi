package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Historique;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.HistoriqueRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Service.HistoriqueService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/historiques")
public class HistoriqueController {

    private HistoriqueService historiqueService;

    private HistoriqueRepository historiqueRepository;

    private UserRepository userRepository;

    @GetMapping
    public List<Historique> getAllHistoriques() {
        return historiqueService.getAllHistoriques();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Historique> getHistoriqueById(@PathVariable String id) {
        Optional<Historique> historique = historiqueService.getHistoriqueById(id);
        return historique.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Historique>> getHistoriquesByClients(@PathVariable String userId) {
        return new ResponseEntity<>(historiqueService.getHistoriquesByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Historique>> getHistoriquesByUser() {
        return new ResponseEntity<>(historiqueService.getHistoriquesByUserId(), HttpStatus.OK);
    }

    @PostMapping
    public Historique createHistorique(@RequestBody Historique historique) {
        return historiqueService.saveHistorique(historique);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistorique(@PathVariable String id) {
        historiqueService.deleteHistorique(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clients/{clientId}")
    public ResponseEntity<List<Historique>> getHistoriqueByClientId(@PathVariable String clientId) {
        List<Historique> historiqueList = historiqueService.getHistoriqueByClientId(clientId);
        return ResponseEntity.ok(historiqueList);
    }


    @GetMapping("/users/{UserId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Historique>> getHistoriqueByUserId(@PathVariable String UserId) {
        List<Historique> historiqueList = historiqueService.getHistoriquesByUserId(UserId);
        return ResponseEntity.ok(historiqueList);

    }
}
