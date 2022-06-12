package pl.shonsu.gradebook.controller;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class SubjectImportValidator{

    public static final String CSV_MIME_TYPE="text/csv";

    public static MultipartFile validate(MultipartFile multipartFile){
        Objects.requireNonNull(multipartFile,"File can not be null.");
        String contentType = multipartFile.getContentType();
        if(!Objects.nonNull(contentType)){
            throw new IllegalArgumentException("Content type is null.");
        }
        if(!isSupportedContentType(contentType)){
            throw new IllegalArgumentException("File is not a valid csv file.");
        }
        return multipartFile;
    }
    private static boolean isSupportedContentType(String contentType) {
        return contentType.equals(CSV_MIME_TYPE);
    }
}
