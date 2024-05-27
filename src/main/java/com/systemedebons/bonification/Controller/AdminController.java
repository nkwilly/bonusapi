package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Administrator;
import com.systemedebons.bonification.Repository.AdministratorRepository;
import com.systemedebons.bonification.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/administrator")
public class AdminController {


    @Autowired
    private AdminService adminService;
    private AdministratorRepository administratorRepository;


    @PostMapping
    public Administrator createAdministrator(@RequestBody Administrator administrator) {
        return adminService.createAdministrator(administrator);
    }

    @PostMapping("/login")
    public Administrator login(@RequestParam String username, @RequestParam String password) {

        return adminService.login(username, password);
    }


    @GetMapping
    public List<Administrator> getAllAdministrators() {
        return adminService.getAdmins();
    }




    @PutMapping("/{id}")
    public ResponseEntity<Administrator> updateAdministrator(@PathVariable String id, @RequestBody Administrator updateAdministrator) {

        Optional<Administrator> administratorOptional = administratorRepository.findById(id);

        if (!administratorOptional.isPresent()) {

            return  ResponseEntity.notFound().build();
        }

        Administrator  administrator = administratorOptional.get();


        administrator.setUsername(updateAdministrator.getUsername());
        administrator.setEmail(updateAdministrator.getEmail());


        // Enregistrer les modifications apportées à l'objet administrateur dans la base de données
        administratorRepository.save(administrator);

        // Renvoie l'objet administrateur mis à jour avec un code de réussite 200

        return ResponseEntity.ok(administrator);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Administrator> deleteAdministrator(@PathVariable String id) {
        if(administratorRepository.existsById(id)) {
            administratorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }










}
