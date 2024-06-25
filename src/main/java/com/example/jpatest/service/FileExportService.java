package com.example.jpatest.service;

import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.List;

public interface FileExportService<T> {
    ByteArrayResource export(List<T> dataList, Class<T> typeClass, List<String> headerList) throws IOException;
    ByteArrayResource export(List<T> dataList, Class<T> typeClass, List<String> headerList, String password) throws IOException;
}

