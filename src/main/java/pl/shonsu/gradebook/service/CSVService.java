package pl.shonsu.gradebook.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.shonsu.gradebook.helper.CSVHelper;
import pl.shonsu.gradebook.model.Grade;
import pl.shonsu.gradebook.model.Subject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class CSVService {

    CSVHelper csvHelper;
    static List<Subject> subjectList;
    public CSVService(CSVHelper csvHelper) {
        this.csvHelper = csvHelper;
    }

    public void readCSVFile(MultipartFile file) {
        try {
            subjectList = csvHelper.csvToSubject(file.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Subject> getAllSubjects() {
        return subjectList.stream().toList();
    }

    public static List<Grade> getSingleSubjectGrades(String name) {
        return subjectList.stream()
                .filter(subject -> Objects.equals(subject.getName(), name))
                .findAny()
                .map(Subject::grades)
                .orElse(List.of());
    }

}
