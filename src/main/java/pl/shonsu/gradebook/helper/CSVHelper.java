package pl.shonsu.gradebook.helper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import pl.shonsu.gradebook.model.Grade;
import pl.shonsu.gradebook.model.Rate;
import pl.shonsu.gradebook.model.Subject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = {"SUBJECT", "RATE", "RATE_DATE", "DESCRIPTION"};

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<Subject> csvToSubject(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT)) {
            List<Subject> subjects = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Grade grade = new Grade(
                        new Rate(Double.parseDouble(csvRecord.get("RATE"))),
                        LocalDate.parse(csvRecord.get("RATE_DATE")),
                        csvRecord.get("DESCRIPTION"));
                Subject subject = new Subject(
                        csvRecord.get("SUBJECT"),
                        List.of(grade)
                );
                subjects.add(subject);
            }
            return subjects;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
