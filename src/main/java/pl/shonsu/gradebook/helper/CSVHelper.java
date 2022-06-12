package pl.shonsu.gradebook.helper;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import pl.shonsu.gradebook.model.Grade;
import pl.shonsu.gradebook.model.Rate;
import pl.shonsu.gradebook.model.Subject;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CSVHelper {
    private final SubjectsCsvRecordProvider subjectsCsvRecordProvider;

    private final CsvCellParser csvCellParser;

    public CSVHelper(SubjectsCsvRecordProvider subjectsCsvRecordProvider, CsvCellParser csvCellParser) {
        this.subjectsCsvRecordProvider = subjectsCsvRecordProvider;
        this.csvCellParser = csvCellParser;
    }

    public List<Subject> csvToSubject(InputStream inputStream) {
        return parse(inputStream)
                .stream()
                .collect(Collectors.groupingBy(GradeFromCsv::subjectName))
                .entrySet().stream()
                .map(entry -> new Subject(entry.getKey(), convertToGrades(entry.getValue())))
                .toList();
    }

    private static List<Grade> convertToGrades(List<GradeFromCsv> gradesFromCsv) {
        return gradesFromCsv.stream()
                .map(GradeFromCsv::grade)
                .toList();
    }

    private List<GradeFromCsv> parse(InputStream inputStream) {
        try (CSVParser csvParser = subjectsCsvRecordProvider.provide(inputStream)) {
            return extractSubjects(csvParser);
        } catch (IOException e) {
            throw new RuntimeException("Error while parsing file.");
        }
    }

    private List<GradeFromCsv> extractSubjects(CSVParser csvParser) {
        return csvParser.stream()
                .map(csvRecord -> {
                    Grade grade = createGrade(csvRecord);
                    String subject = csvRecord.get(SubjectCsvHeaders.SUBJECT);
                    return new GradeFromCsv(subject, grade);
                })
                .toList();
    }

    private Grade createGrade(CSVRecord csvRecord) {
        Double rate = csvCellParser.parseDouble(csvRecord.get(SubjectCsvHeaders.RATE));
        LocalDate rateDate = csvCellParser.parseDate(csvRecord.get(SubjectCsvHeaders.RATE_DATE));
        String description = csvRecord.get(SubjectCsvHeaders.DESCRIPTION);
        return new Grade(new Rate(rate), rateDate, description);
    }

    record GradeFromCsv(String subjectName, Grade grade) {
    }
}
