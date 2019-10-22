package com.razakor.demo.bootstrap;

import com.razakor.demo.documents.User;
import com.razakor.demo.services.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements ApplicationRunner {

    private final UserService userService;

    public Bootstrap(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
/*
        User user1 = new User("user", "pass", true);
        user1.AddRole("ROLE_USER");
        userService.save(user1);

        User user2 = new User("admin", "password", true);
        user2.AddRole("ROLE_USER");
        user2.AddRole("ROLE_ADMIN");
        userService.save(user2);
*/
    }
}
