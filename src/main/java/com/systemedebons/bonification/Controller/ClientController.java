package com.systemedebons.bonification.Controller;


import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.payload.request.LoginRequest;
import com.systemedebons.bonification.Service.ClientService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(value = "Client Management System", description = "Operations pertaining to Client in Client Management System")
@RestController
@RequestMapping("/api/Client")
public class ClientController {
    
    @Autowired
    private ClientService clientService;
    
    @GetMapping("list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
    
        Optional<Client> Client = clientService.getClientById(id);
        return Client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    
    }
    
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Client> createClient(@Valid  @RequestBody Client client) {
        try {
            Client  saveClient = clientService.saveClient(client);
            return ResponseEntity.ok(saveClient);
        }catch (IllegalArgumentException e){
    
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
    
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
    

    
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Client> updateClient(@PathVariable String id, @RequestBody Client Client) {
            return clientService.updateClient(id, Client)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
    
    
    
    
       /** @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
            return ClientService.login(loginRequest)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(401).build());
        }*/
}
    


