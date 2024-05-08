package com.example.jpatest.repository;

import com.example.jpatest.entity.Student;
import com.example.jpatest.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void test() {
        User user = new User();
        user.setSex("man");
        user.setName("name");
        Student student = new Student();
        student.setAge(12);
        student.setName("123");
        student.setExtension("ex");
        student.setUser(user);
        Student save = studentRepository.save(student);
        Assertions.assertNotNull(save.getId());


    }

    @Test
    public void testGet() {
        List<Student> all = studentRepository.findAll();
        for (Student stu : all) {
          log.info("stu : {}" , stu);
        }

    }

}