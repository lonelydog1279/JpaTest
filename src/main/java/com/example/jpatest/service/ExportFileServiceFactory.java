package com.example.jpatest.service;

public class ExportFileServiceFactory {

    public static <T> FileExportService<T> getFileExportService(String fileType) {
        switch (fileType.toLowerCase()) {
            case "csv":
                return new CsvExportService<>();
            case "excel":
                return new ExcelExportService<>();
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }
}

