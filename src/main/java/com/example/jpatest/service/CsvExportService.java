package com.example.jpatest.service;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.springframework.core.io.ByteArrayResource;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class CsvExportService<T> implements FileExportService<T> {

    @Override
    public ByteArrayResource export(List<T> dataList, Class<T> typeClass, List<String> headerList) throws IOException {
        String csvContent = createCSVContent(dataList, typeClass, headerList);
        return writeCSVToByteArrayResource(csvContent);
    }

    @Override
    public ByteArrayResource export(List<T> dataList, Class<T> typeClass, List<String> headerList, String password) throws IOException {
        String csvContent = createCSVContent(dataList, typeClass, headerList);
        return encryptCSVWithZip(csvContent, password);
    }

    private String createCSVContent(List<T> dataList, Class<T> typeClass, List<String> headerList) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(",", headerList)).append("\n");

        for (T data : dataList) {
            List<String> rowValues = headerList.stream().map(fieldName -> {
                try {
                    Field field = typeClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value = field.get(data);
                    return value != null ? value.toString() : "";
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    return "";
                }
            }).collect(Collectors.toList());
            sb.append(String.join(",", rowValues)).append("\n");
        }

        return sb.toString();
    }

    private ByteArrayResource writeCSVToByteArrayResource(String csvContent) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            bos.write(csvContent.getBytes(StandardCharsets.UTF_8));
            return new ByteArrayResource(bos.toByteArray());
        } catch (IOException e) {
            System.err.println("Error writing CSV to byte array: " + e.getMessage());
            return null;
        }
    }

    private ByteArrayResource encryptCSVWithZip(String csvContent, String password) throws IOException {
        File tempCsvFile = File.createTempFile("tempCsv", ".csv");
        try (FileOutputStream fos = new FileOutputStream(tempCsvFile)) {
            fos.write(csvContent.getBytes(StandardCharsets.UTF_8));
        }

        File tempZipFile = File.createTempFile("tempZip", ".zip");
        ZipFile zipFile = new ZipFile(tempZipFile, password.toCharArray());

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
        zipParameters.setCompressionLevel(CompressionLevel.NORMAL);
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);

        zipFile.addFile(tempCsvFile, zipParameters);

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             FileInputStream fis = new FileInputStream(tempZipFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return new ByteArrayResource(bos.toByteArray());
        } finally {
            tempCsvFile.delete();
            tempZipFile.delete();
        }
    }
}
