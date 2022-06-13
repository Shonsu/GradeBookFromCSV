package pl.shonsu.gradebook.importer.csv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.shonsu.gradebook.message.ResponseMessage;
import pl.shonsu.gradebook.model.Grade;
import pl.shonsu.gradebook.model.Subject;
import pl.shonsu.gradebook.importer.csv.service.CSVService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/csv")
public class SubjectImportController {
    @Autowired
    CSVService fileService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> uploadCsv(@RequestParam("file") MultipartFile file) {

        String message;
        SubjectImportValidator.validate(file);
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Can't get inputstream for uploaded csv file.");
        }
        fileService.readCSVFile(inputStream);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

    }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        try {
            List<Subject> subjects = fileService.getAllSubjects();
            if (subjects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(subjects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/subjects/{name}/grades")
    public List<Grade> getSingleSubjectGrades(@PathVariable String name) {
        return CSVService.getSingleSubjectGrades(name);
    }
}

