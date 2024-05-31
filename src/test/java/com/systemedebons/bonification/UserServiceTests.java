package com.systemedebons.bonification;


import com.systemedebons.bonification.Entity.User;
import com.systemedebons.bonification.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Test
    void testCreateUser() {
        User user = new User();
        user.setNom("Momo");
        user.setPrenom("Jack");
        user.setEmail("jack@gmail.com");
        user.setMotDePasse("123456");

        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser.getId());
    }

}
