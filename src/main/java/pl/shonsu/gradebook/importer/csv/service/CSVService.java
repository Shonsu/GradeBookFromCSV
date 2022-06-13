package pl.shonsu.gradebook.importer.csv.service;

import org.springframework.stereotype.Service;
import pl.shonsu.gradebook.importer.csv.helper.CSVHelper;
import pl.shonsu.gradebook.model.Grade;
import pl.shonsu.gradebook.model.Subject;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Service
public class CSVService {

    CSVHelper csvHelper;
    static List<Subject> subjectList;
    public CSVService(CSVHelper csvHelper) {
        this.csvHelper = csvHelper;
    }

    public void readCSVFile(InputStream inputStream) {
        subjectList = csvHelper.csvToSubject(inputStream);
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
