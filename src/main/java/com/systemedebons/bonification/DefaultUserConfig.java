package com.systemedebons.bonification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class DefaultUserConfig extends SecurityConfig{

    @Bean
    public UserDetailsService userDetailsService(AuthenticationManagerBuilder auth) throws  Exception {

        auth.inMemoryAuthentication()
                .withUser(User.withUsername("user")
                        .password(passwordEncoder().encode("password"))
                        .roles("USER"));

        return  new InMemoryUserDetailsManager();

    }


}
