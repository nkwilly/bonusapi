package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Administrator;
import com.systemedebons.bonification.Repository.AdministratorRepository;
import com.systemedebons.bonification.Service.AdminService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
@RequestMapping("/api/administrator")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Administrator> createAdministrator( @Valid  @RequestBody Administrator administrator) {
        try{
            Administrator  savedAdministrator = adminService.saveAdministrator(administrator);

            return ResponseEntity.ok(savedAdministrator);
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
    }



    @GetMapping("users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Administrator> getAllAdministrators() {
        return adminService.getAdmins();
    }



    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Administrator> getAdministrateurById(@PathVariable String id) {
        Optional<Administrator> administrator = adminService.getAdministrator(id);
        return administrator.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAdministrateur(@PathVariable String id) {
        adminService.deleteAdministrator(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/protected-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getProtectedData() {
        return ResponseEntity.ok("This is protected data for admins only");
    }



}
