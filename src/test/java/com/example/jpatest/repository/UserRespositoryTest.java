package com.example.jpatest.repository;

import com.example.jpatest.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class UserRespositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testInsert() {
        User user = new User();
        user.setName("1");
        user.setSex("2");

        User save = userRepository.save(user);

        Assertions.assertNotNull(save.getId());
    }

    @Test
    public void testGet() {
        List<User> all = userRepository.findAll();
        for (User user : all) {
            log.info("user: {}", user);
        }
    }


}