package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Historique;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.HistoriqueRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Service.HistoriqueService;
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
@RequestMapping("/api/historiques")
public class HistoriqueController {


    @Autowired
    private HistoriqueService historiqueService;
    @Autowired
    private HistoriqueRepository historiqueRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Historique> getAllHistoriques() {
        return historiqueService.getAllHistoriques();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Historique> getHistoriqueById(@PathVariable String id) {
        Optional<Historique> historique = historiqueService.getHistoriqueById(id);
        return historique.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Historique createHistorique(@RequestBody Historique historique) {
        return historiqueService.saveHistorique(historique);
    }





    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHistorique(@PathVariable String id) {
        historiqueService.deleteHistorique(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/users/{UserId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Historique>> getHistoriqueByUserId(@PathVariable String UserId) {
        List<Historique> historiqueList = historiqueService.getHistoriqueByUserId(UserId);
        return ResponseEntity.ok(historiqueList);

    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Historique>> getHistoriqueByCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Historique> historiques = historiqueRepository.findByUserId(user.getId());
        return ResponseEntity.ok(historiques);
    }


}
