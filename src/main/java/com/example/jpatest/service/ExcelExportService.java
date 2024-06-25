package com.example.jpatest.service;

import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;

import java.io.*;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.util.List;

public class ExcelExportService<T> implements FileExportService<T> {

    @Override
    public ByteArrayResource export(List<T> dataList, Class<T> typeClass, List<String> headerList) throws IOException {
        Workbook workbook = createWorkbook(dataList, typeClass, headerList);
        return writeWorkbookToByteArrayResource(workbook);
    }

    @Override
    public ByteArrayResource export(List<T> dataList, Class<T> typeClass, List<String> headerList, String password) throws IOException {
        Workbook workbook = createWorkbook(dataList, typeClass, headerList);
        return encryptWorkbook(workbook, password);
    }

    private Workbook createWorkbook(List<T> dataList, Class<T> typeClass, List<String> headerList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headerList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerList.get(i));
        }

        for (int i = 0; i < dataList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            T data = dataList.get(i);
            for (int j = 0; j < headerList.size(); j++) {
                Cell cell = row.createCell(j);
                String fieldName = headerList.get(j);
                try {
                    Field field = typeClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value = field.get(data);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return workbook;
    }

    private ByteArrayResource writeWorkbookToByteArrayResource(Workbook workbook) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            workbook.write(bos);
            workbook.close();
            return new ByteArrayResource(bos.toByteArray());
        } catch (IOException e) {
            System.err.println("Error writing to byte array: " + e.getMessage());
            return null;
        }
    }

    private ByteArrayResource encryptWorkbook(Workbook workbook, String password) {
        try (POIFSFileSystem fs = new POIFSFileSystem();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
            Encryptor encryptor = info.getEncryptor();
            encryptor.confirmPassword(password);

            try (OutputStream os = encryptor.getDataStream(fs)) {
                workbook.write(os);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }

            workbook.close();
            fs.writeFilesystem(bos);
            return new ByteArrayResource(bos.toByteArray());
        } catch (IOException e) {
            System.err.println("Error encrypting workbook: " + e.getMessage());
            return null;
        }
    }
}
