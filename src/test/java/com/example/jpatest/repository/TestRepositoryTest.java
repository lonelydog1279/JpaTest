package com.example.jpatest.repository;

import com.example.jpatest.entity.Pass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class TestRepositoryTest {


    @Autowired
    private TestRepository testRepository;

    @Test
    public void findTestByTimeBefore() {
        List<Pass> testByTimeBefore = testRepository.findTestByTimeBefore(new Date());
        Assertions.assertFalse(testByTimeBefore.isEmpty());
    }
}