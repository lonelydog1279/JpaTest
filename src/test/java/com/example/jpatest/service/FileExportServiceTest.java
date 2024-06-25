package com.example.jpatest.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class FileExportServiceTest {

    @Test
    void test() throws IOException {
        List<String> headerList = List.of("name", "age", "country");
        List<Person> dataList = List.of(
                new Person("John Doe", 30, "USA"),
                new Person("Jane Smith", 25, "UK"),
                new Person("Jack Johnson", 45, "Canada")
        );

        FileExportService<Person> csvExportService = ExportFileServiceFactory.getFileExportService("csv");
        ByteArrayResource csvResource = csvExportService.export(dataList, Person.class, headerList);
        saveToFile(csvResource, "output.csv");

        ByteArrayResource encryptedCsvResource = csvExportService.export(dataList, Person.class, headerList, "password123");
        saveToFile(encryptedCsvResource, "encrypted_output.zip");

        FileExportService<Person> excelExportService = ExportFileServiceFactory.getFileExportService("excel");
        ByteArrayResource excelResource = excelExportService.export(dataList, Person.class, headerList);
        saveToFile(excelResource, "output.xlsx");

        ByteArrayResource encryptedExcelResource = excelExportService.export(dataList, Person.class, headerList, "password123");
        saveToFile(encryptedExcelResource, "encrypted_output.xlsx");
    }

    private static void saveToFile(ByteArrayResource resource, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(resource.getByteArray());
            System.out.println("File saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }
}

class Person {
    private String name;
    private int age;
    private String country;

    public Person(String name, int age, String country) {
        this.name = name;
        this.age = age;
        this.country = country;
    }

    // Getters and setters (omitted for brevity)
}