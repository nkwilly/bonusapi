package com.systemedebons.bonification.Controller;


import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.payload.request.LoginRequest;
import com.systemedebons.bonification.Service.ClientService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(value = "Client Management System", description = "Operations pertaining to Client in Client Management System")
@RestController
@AllArgsConstructor
@RequestMapping("/api/Client")
public class ClientController {

    private ClientService clientService;
    
    @GetMapping("list")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Client>> getClientsByUser(@PathVariable String userId) {
        return new ResponseEntity<>(clientService.getAllClientsByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Client>> getAllClientsByUser() {
        return new ResponseEntity<>(clientService.getAllClientsByUser(), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
        Optional<Client> Client = clientService.getClientById(id);
        return Client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestParam String clientUsername) {
        Optional<Client>  saveClient = clientService.createClient(clientUsername);
        return saveClient.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> delete(@PathVariable String clientId) {
        clientService.delete(clientId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Client> update(@RequestBody Client Client) {
        return clientService.update(Client)
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
    


