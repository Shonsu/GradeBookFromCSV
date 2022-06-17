package pl.shonsu.gradebook.importer.csv.helper;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import pl.shonsu.gradebook.importer.csv.parser.CsvCellParser;
import pl.shonsu.gradebook.importer.csv.parser.SubjectCsvHeaders;
import pl.shonsu.gradebook.importer.csv.parser.SubjectsCsvRecordProvider;
import pl.shonsu.gradebook.model.Grade;
import pl.shonsu.gradebook.model.Rate;
import pl.shonsu.gradebook.model.Subject;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CSVHelper {
    private final SubjectsCsvRecordProvider subjectsCsvRecordProvider;

    private static final String VALID_HEADERS = SubjectCsvHeaders.names().stream().map(String::valueOf).collect(Collectors.joining(";"));

    private final CsvCellParser csvCellParser;

    public CSVHelper(SubjectsCsvRecordProvider subjectsCsvRecordProvider, CsvCellParser csvCellParser) {
        this.subjectsCsvRecordProvider = subjectsCsvRecordProvider;
        this.csvCellParser = csvCellParser;
    }

    public void checkValidHeaders(CSVParser csvParser) {

        List<String> headersNamesFromCsv = csvParser.getHeaderNames();
        List<String> expectedHeaderNames = SubjectCsvHeaders.names();
        if (headersNamesFromCsv.size() != expectedHeaderNames.size()) {
            throw new RuntimeException("Wrong number of columns! Expected " + expectedHeaderNames.size());
        }
        IntStream
                .range(0, expectedHeaderNames.size())
                .filter(headerNumber -> !Objects.equals(headersNamesFromCsv.get(headerNumber), expectedHeaderNames.get(headerNumber)))
                .findFirst()
                .ifPresent(headerNumber -> {
                    throw new RuntimeException("Wrong header [" +
                            headersNamesFromCsv.get(headerNumber) +
                            "] at column [" +
                            headerNumber +
                            "]" +
                            System.lineSeparator() +
                            "Headers should looks like: [" +
                            VALID_HEADERS +
                            "]"
                    );
                });

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
        checkValidHeaders(csvParser);
        List<GradeFromCsv> grades = csvParser.stream()
                .map(csvRecord -> {
                    Grade grade = createGrade(csvRecord);
                    String subject = csvRecord.get(SubjectCsvHeaders.SUBJECT);
                    return new GradeFromCsv(subject, grade);
                })
                .toList();
        if (grades.isEmpty()) {
            throw new RuntimeException("Csv file has no data.");
        }
        return grades;
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
