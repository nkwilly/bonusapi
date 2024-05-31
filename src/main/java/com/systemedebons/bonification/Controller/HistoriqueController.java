package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Historique;
import com.systemedebons.bonification.Service.HistoriqueService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
@RequestMapping("/api/historiques")
public class HistoriqueController {


    @Autowired
    private HistoriqueService historiqueService;



    @GetMapping
    public List<Historique> getAllHistoriques() {
        return historiqueService.getAllHistoriques();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Historique> getHistoriqueById(@PathVariable String id) {
        Optional<Historique> historique = historiqueService.getHistoriqueById(id);
        return historique.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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


    @GetMapping("/users/{UserId}")
    public ResponseEntity<List<Historique>> getHistoriqueByUserId(@PathVariable String UserId) {
        List<Historique> historiqueList = historiqueService.getHistoriqueByUserId(UserId);
        return ResponseEntity.ok(historiqueList);

    }




}
