package pl.shonsu.gradebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.shonsu.gradebook.helper.CSVHelper;
import pl.shonsu.gradebook.model.Subject;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService {

    List<Subject> subjectList;

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

}
