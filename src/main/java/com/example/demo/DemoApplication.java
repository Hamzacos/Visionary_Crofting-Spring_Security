package com.example.demo;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {
            accountService.addNewRole(new Role(null,"User"));
            accountService.addNewRole(new Role(null,"Admin"));
            accountService.addNewRole(new Role(null,"Fournisseur"));

            accountService.addNewUser(new User(null,"user1","1234",new ArrayList<>()));
            accountService.addNewUser(new User(null,"user2","1234",new ArrayList<>()));
            accountService.addNewUser(new User(null,"admin","1234",new ArrayList<>()));
            accountService.addNewUser(new User(null,"hamza","1234",new ArrayList<>()));
            accountService.addNewUser(new User(null,"salim","1234",new ArrayList<>()));

            accountService.addRoleToUser("user1","User");
            accountService.addRoleToUser("user2","User");
            accountService.addRoleToUser("admin","Admin");
            accountService.addRoleToUser("hamza","Fournisseur");
            accountService.addRoleToUser("salim","Fournisseur");
        };
    }*/

}
