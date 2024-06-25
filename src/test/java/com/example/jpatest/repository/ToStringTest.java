package com.example.jpatest.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class ToStringTest {

    @Test
    void test(){
        TestA testA = new TestA().setGender("1").setName("2");
        log.info(ToStringBuilder.reflectionToString(testA));
    }

}

@Data
@Accessors(chain = true)
class TestA{

    @JsonIgnore
    private String name;

    private String gender;
}

