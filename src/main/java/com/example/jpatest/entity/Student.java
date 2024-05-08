package com.example.jpatest.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "student")
public class Student {

    @Id
    private String id;

    private String name;

    private Integer age;

    private String extension;

    private User user;

}
