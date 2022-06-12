package pl.shonsu.gradebook.helper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import pl.shonsu.gradebook.model.Grade;
import pl.shonsu.gradebook.model.Rate;
import pl.shonsu.gradebook.model.Subject;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CSVHelper {
    private static SubjectsCsvRecordProvider subjectsCsvRecordProvider;

    private static CsvCellParser csvCellParser;

    public CSVHelper(SubjectsCsvRecordProvider subjectsCsvRecordProvider, CsvCellParser csvCellParser) {
        this.subjectsCsvRecordProvider = subjectsCsvRecordProvider;
        this.csvCellParser = csvCellParser;
    }

    public static boolean hasCSVFormat(MultipartFile file) {
        if(Objects.equals(file.getContentType(), "text/csv")){
            return true;
        }
        return false;
    }

    public static List<Subject> csvToSubject(InputStream inputStream) {
        System.out.println(SubjectCsvHeaders.SUBJECT);

        subjectsCsvRecordProvider.provide(inputStream);
        System.out.println("after subjectsCsvRecordProvider.provide(inputStream)");
        return extractSubjects(subjectsCsvRecordProvider.provide(inputStream)).stream()
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

    private static List<GradeFromCsv> extractSubjects(Iterable<CSVRecord> csvRecords) {
        List<GradeFromCsv> subjectsFromCsv =  new ArrayList<>();
        for (CSVRecord csvRecord : csvRecords) {
            Grade grade = createGrade(csvRecord);
            String subject = csvRecord.get(SubjectCsvHeaders.SUBJECT);
            System.out.println(grade + " " + subject);
            subjectsFromCsv.add(new GradeFromCsv(subject, grade));
        }
        return subjectsFromCsv;
    }

    private static Grade createGrade(CSVRecord csvRecord) {
        Double rate = csvCellParser.parseDouble(csvRecord.get(SubjectCsvHeaders.RATE));
        LocalDate rateDate = csvCellParser.parseDate(csvRecord.get(SubjectCsvHeaders.RATE_DATE));
        String description = csvRecord.get(SubjectCsvHeaders.DESCRIPTION);
        return new Grade(new Rate(rate), rateDate, description);
    }

    record GradeFromCsv(String subjectName, Grade grade) {
    }
}
