package pl.shonsu.gradebook.importer.csv.controller;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;

public class SubjectImportValidator {

    public static final String CSV_MIME_TYPE = "text/csv";

    public static MultipartFile validate(MultipartFile multipartFile) {
        Objects.requireNonNull(multipartFile, "File can not be null.");
        File file = multipartfileToFile(multipartFile);
        if(file.length()==0){
            throw new RuntimeException("File is empty");
        }
        String contentType = multipartFile.getContentType();
        if (!Objects.nonNull(contentType)) {
            throw new IllegalArgumentException("Content type is null.");
        }
        if (!isSupportedContentType(contentType)) {
            throw new IllegalArgumentException("File is not a valid csv file.");
        }
        return multipartFile;
    }

    private static boolean isSupportedContentType(String contentType) {
        return contentType.equals(CSV_MIME_TYPE);
    }

    private static File multipartfileToFile(MultipartFile multipartFile) {
        File file = new File("src/main/resources/targetFile.tmp");
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
            return file;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
