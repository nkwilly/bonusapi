package com.systemedebons.bonification.Controller;

import com.systemedebons.bonification.Entity.History;
import com.systemedebons.bonification.Service.HistoryService;
import com.systemedebons.bonification.payload.dto.HistoryDTO;
import com.systemedebons.bonification.payload.request.HistoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
@AllArgsConstructor
@RequestMapping("/api/history")
@Tag(name = "History Controller", description = "Endpoints for managing transaction history")
public class HistoryController {

    private HistoryService historyService;

    @Operation(summary = "Retrieve all histories", description = "Returns a list of all transaction histories.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of histories")
    @GetMapping
    public List<History> getAllHistory() {
        return historyService.getAllHistory();
    }

    @Operation(summary = "Retrieve history by ID", description = "Fetches a history based on its unique ID.")
    @ApiResponse(responseCode = "200", description = "History found")
    @ApiResponse(responseCode = "404", description = "History not found")
    @GetMapping("/{id}")
    public ResponseEntity<History> getHistoryById(@PathVariable String id) {
        Optional<History> history = historyService.getHistoryById(id);
        return history.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Retrieve histories by user ID", description = "Fetches all histories associated with a given user ID.")
    @ApiResponse(responseCode = "200", description = "Histories retrieved")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HistoryDTO>> getHistoryByClients(@PathVariable String userId) {
        return new ResponseEntity<>(historyService.getHistoryByUserId(userId), HttpStatus.OK);
    }

    @Operation(summary = "Retrieve histories for the current user", description = "Fetches all histories associated with the currently authenticated user.")
    @ApiResponse(responseCode = "200", description = "Histories retrieved")
    @GetMapping("/user")
    public ResponseEntity<List<HistoryDTO>> getHistoryByUser() {
        return new ResponseEntity<>(historyService.getHistoryByUserId(), HttpStatus.OK);
    }

    @Operation(summary = "Create a new history entry", description = "Saves a new transaction history.")
    @ApiResponse(responseCode = "200", description = "History successfully created")
    @PostMapping
    public HistoryDTO createHistory(@RequestBody HistoryDTO historyDTO) {
        return historyService.saveHistory(historyDTO);
    }

    @Operation(summary = "Delete a history entry", description = "Deletes a history using its ID.")
    @ApiResponse(responseCode = "204", description = "History successfully deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable String id) {
        historyService.deleteHistory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieve history by client id", description = "Fetches all transaction histories associated with a given client id.")
    @ApiResponse(responseCode = "200", description = "Histories retrieved")
    @GetMapping("/clients/{clientId}")
    public ResponseEntity<List<HistoryDTO>> getHistoryByclientLogin(@PathVariable String clientId) {
        List<HistoryDTO> historyList = historyService.getHistoryByClientId(clientId);
        return ResponseEntity.ok(historyList);
    }

    @Operation(summary = "Retrieve history by user ID", description = "Fetches all histories for a given user ID with access control.")
    @ApiResponse(responseCode = "200", description = "Histories retrieved")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/users/{UserId}")
    public ResponseEntity<List<HistoryDTO>> getHistoryByUserId(@PathVariable String UserId) {
        List<HistoryDTO> historyList = historyService.getHistoryByUserId(UserId);
        return ResponseEntity.ok(historyList);
    }

    @Operation(summary = "Retrieve history by transaction ID", description = "Fetches a history entry using a transaction ID.")
    @ApiResponse(responseCode = "200", description = "History retrieved")
    @ApiResponse(responseCode = "404", description = "History not found")
    @GetMapping("/transaction/{id}")
    public ResponseEntity<HistoryRequest> getHistoryByTransactionId(@PathVariable String id) {
        Optional<HistoryDTO> history = historyService.getHistoryByTransactionId(id);
        if (history.isPresent()) {
            HistoryRequest historyRequest = new HistoryRequest();
            historyRequest.setId(history.get().getId());
            historyRequest.setPoints(history.get().getPoints());
            historyRequest.setTransactionId(history.get().getTransactionId());
            return ResponseEntity.ok(historyRequest);
        }
        return ResponseEntity.notFound().build();
    }
}