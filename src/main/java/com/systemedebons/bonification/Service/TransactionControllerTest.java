package com.systemedebons.bonification.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.systemedebons.bonification.Controller.TransactionController;
import com.systemedebons.bonification.Entity.Client;
import com.systemedebons.bonification.Entity.Statuts;
import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.UserRepository;
import com.systemedebons.bonification.Service.utils.Mapper;
import com.systemedebons.bonification.payload.dto.TransactionDTO;
import com.systemedebons.bonification.payload.response.SavedTransactionResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Mapper mapper;

    @MockBean
    private ClientRepository clientRepository;

    @Test
    public void saveTransaction() throws Exception {
        User user = new User();
        user.setId("67433ad7c9212b66e2d203c9");
        Client client = new Client();
        client.setId("674434d43f1e7f13b9d96c0e");

        when(userRepository.findById("67433ad7c9212b66e2d203c9")).thenReturn(Optional.of(user));
        when(clientRepository.findById("674434d43f1e7f13b9d96c0e")).thenReturn(Optional.of(client));

        System.out.println("user = " + user);
        System.out.println("client = " + client);

        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(10);
        dto.setId("2");
        dto.setStatus(Statuts.COMPLETE);
        dto.setIsDebit(Boolean.TRUE);
        dto.setClientLogin("bernard");
        SavedTransactionResponse transaction = new SavedTransactionResponse();
        transaction.setAmount(5000);
        transaction.setStatuts(Statuts.COMPLETE);
        transaction.setClientLogin(client.getId());
        transaction.setMessage("Test message");
        transaction.setIsDebit(Boolean.TRUE);
        transaction.setId("1");


        when(transactionService.saveTransaction(dto
        )).thenReturn(transaction);
        this.mockMvc.perform(post("/api/transactions/create-transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(transaction)))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
