package com.example.jpatest.repository;

import com.example.jpatest.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TestRepository extends JpaRepository<Pass,Integer> {

    List<Pass> findTestByTimeBefore(Date date);
}
