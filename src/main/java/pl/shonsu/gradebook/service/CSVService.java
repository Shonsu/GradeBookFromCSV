package pl.shonsu.gradebook.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    static List<Subject> subjectList;


    public void readCSVFile(MultipartFile file) {
        try {
            subjectList = CSVHelper.csvToSubject(file.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Subject> getAllSubjects() {
        return subjectList.stream().toList();
    }

//    public static List<Grade> getSingleSubjectGrades(String name) {
//        List<Subject> s = subjectList.stream().filter(grades-> Objects.equals(grades.getName(), name)).toList();
//        s.map(s->new Grade(s.grades()));
//    }

}
