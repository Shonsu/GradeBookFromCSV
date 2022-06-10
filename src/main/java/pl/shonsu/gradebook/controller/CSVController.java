package pl.shonsu.gradebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.shonsu.gradebook.helper.CSVHelper;
import pl.shonsu.gradebook.message.ResponseMessage;
import pl.shonsu.gradebook.model.Grade;
import pl.shonsu.gradebook.model.Subject;
import pl.shonsu.gradebook.service.CSVService;

import java.util.List;

@RestController
@RequestMapping("/api/csv")
public class CSVController {
    @Autowired
    CSVService fileService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message;
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                fileService.readCSVFile(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }
    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAllTutorials() {
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
//    @GetMapping("/subjects/{name}/grades")
//    public List<Grade> getSingleSubjectGrades(@PathVariable String name) {
//        return CSVService.getSingleSubjectGrades(name);
//    }
}

