package com.systemedebons.bonification;

import com.systemedebons.bonification.Entity.*;
import com.systemedebons.bonification.Repository.ClientRepository;
import com.systemedebons.bonification.Repository.TransactionRepository;
import com.systemedebons.bonification.Service.TransactionService;
import com.systemedebons.bonification.Service.utils.Mapper;
import com.systemedebons.bonification.payload.dto.TransactionDTO;
import com.systemedebons.bonification.payload.response.SavedTransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {
    private static final Logger log = LoggerFactory.getLogger(TransactionServiceTest.class);
    @Autowired
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private Mapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransactionById() {
        Transaction transaction = new Transaction();
        transaction.setId("1");
        when(transactionRepository.findById("1")).thenReturn(Optional.of(transaction));
        Optional<Transaction> foundTransaction = transactionService.getTransactionById("1");
        assertTrue(foundTransaction.isPresent());
        assertEquals(transaction.getId(), foundTransaction.get().getId());
    }

    @Test
    void testSaveTransaction() {
        // User user = new User("testUser", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Role role = new Role();
        role.setId("1");
        role.setName(ERole.ROLE_USER);
        mongoTemplate.save(role);

        User user = new User();
        user.setId("1");
        user.setPassword("password");
        user.setLogin("willy");
        user.setEmail("berlus@gmail.com");
        user.setRoles(Set.of(role));
        mongoTemplate.save(user);

        clientRepository.deleteByLogin("berlus");
        Client client = new Client();
        client.setId("1");
        client.setLogin("berlus");
        client.setUser(user);
        mongoTemplate.save(client);

        Rule rule = new Rule();
        rule.setId("1");
        rule.setDescription("description");
        rule.setAmountMin(100);
        rule.setPoints(5);
        rule.setUser(user);
        mongoTemplate.save(rule);
        rule.setId("2");
        rule.setDescription("description2");
        rule.setAmountMin(200);
        rule.setPoints(10);
        rule.setUser(user);
        mongoTemplate.save(rule);
        rule.setId("3");
        rule.setDescription("description3");
        rule.setAmountMin(300);
        rule.setPoints(20);
        rule.setUser(user);
        mongoTemplate.save(rule);

        Reward reward = new Reward();
        reward.setId("1");
        reward.setValue(25.0);
        reward.setUser(user);
        mongoTemplate.save(reward);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("willy", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(auth);

        Transaction transaction = new Transaction();
        TransactionDTO dto = new TransactionDTO();
        dto.setId("1");
        dto.setAmount(1000);
        dto.setStatus(Statuts.COMPLETE);
        dto.setClientLogin("berlus");
        dto.setIsDebit(true);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        SavedTransactionResponse savedTransaction = transactionService.saveTransaction(dto);
        transaction = mapper.toTransactionFromRepository(savedTransaction);

        assertNotNull(savedTransaction);


        //verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testDeleteTransaction() {
        doNothing().when(transactionRepository).deleteById("1");

        transactionService.deleteTransaction("1");
        verify(transactionRepository, times(1)).deleteById("1");
    }

}
