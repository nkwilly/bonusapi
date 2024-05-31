package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Historique;
import com.systemedebons.bonification.Repository.HistoriqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoriqueService {

    @Autowired
    private HistoriqueRepository historiqueRepository;

    public List<Historique> getAllHistoriques() {
        return historiqueRepository.findAll();
    }

    public Optional<Historique> getHistoriqueById(String id) {
        return historiqueRepository.findById(id);

    }

    public Historique saveHistorique(Historique historique) {
        return historiqueRepository.save(historique);
    }

    public void deleteHistorique(String id) {
        historiqueRepository.deleteById(id);
    }




}
