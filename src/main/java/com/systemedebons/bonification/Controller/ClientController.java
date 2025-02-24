package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.payload.request.LoginRequest;
import com.systemedebons.bonification.Service.ClientService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(value = "Client Management System", description = "Operations pertaining to Client in Client Management System")
@RestController
@AllArgsConstructor
@RequestMapping("/api/client")
@Tag(name = "Client Controller", description = "Endpoints for managing clients")
public class ClientController {

    private ClientService clientService;

    @Operation(summary = "Retrieve all clients", description = "Returns a list of all clients.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of clients")
    @GetMapping("/list")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @Operation(summary = "Retrieve clients by user ID", description = "Fetches all clients associated with a given user ID.")
    @ApiResponse(responseCode = "200", description = "Clients retrieved")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Client>> getClientsByUser(@PathVariable String userId) {
        return new ResponseEntity<>(clientService.getAllClientsByUser(userId), HttpStatus.OK);
    }

    @Operation(summary = "Retrieve all clients for the current user", description = "Fetches all clients associated with the currently authenticated user.")
    @ApiResponse(responseCode = "200", description = "Clients retrieved")
    @GetMapping("/user")
    public ResponseEntity<List<Client>> getAllClientsByUser() {
        return new ResponseEntity<>(clientService.getAllClientsByUser(), HttpStatus.OK);
    }

    @Operation(summary = "Retrieve a client by ID", description = "Fetches a client based on its unique ID.")
    @ApiResponse(responseCode = "200", description = "Client found")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
        Optional<Client> client = clientService.getClientById(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new client", description = "Creates a new client for a given username.")
    @ApiResponse(responseCode = "200", description = "Client successfully created")
    @PostMapping("/{username}")
    public ResponseEntity<Client> createClient(@PathVariable String username) {
        Optional<Client> saveClient = clientService.createClient(username);
        return saveClient.map(ResponseEntity::ok).orElse(ResponseEntity.ok(new Client()));
    }

    @Operation(summary = "Delete a client", description = "Deletes a client using their login.")
    @ApiResponse(responseCode = "204", description = "Client successfully deleted")
    @DeleteMapping("/{clientLogin}")
    public ResponseEntity<Void> delete(@PathVariable String clientLogin) {
        clientService.delete(clientLogin);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a client", description = "Updates an existing client record.")
    @ApiResponse(responseCode = "200", description = "Client successfully updated")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @PutMapping
    public ResponseEntity<Client> update(@RequestBody Client client) {
        return clientService.update(client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

    


