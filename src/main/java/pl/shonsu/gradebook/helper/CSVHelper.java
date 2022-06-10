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
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.builder().setHeader(HEADERs).setSkipHeaderRecord(true).setDelimiter(';').build())) {
            List<Subject> subjects = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number rate;

            for (CSVRecord csvRecord : csvRecords) {
                try {
                    rate = format.parse(csvRecord.get("RATE"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                Grade grade = new Grade(
                        new Rate(rate.doubleValue()),
                        LocalDate.parse(csvRecord.get("RATE_DATE"), dtf),
                        csvRecord.get("DESCRIPTION"));
//                Subject subject = new Subject(csvRecord.get("SUBJECT"), List.of(grade));
//                subjects.add(subject);


                boolean finded = false;

                for (Subject s : subjects) {
                    if (Objects.equals(s.getName(), csvRecord.get("SUBJECT"))) {
                        System.out.println("add grade to existing subject");
                        System.out.println(s.getName());
                        s.grades().forEach(g -> System.out.println(g.rate()));
                        s.addGrade(grade);
                        finded = true;
                    }

                }

                if (!finded) {
                    System.out.println("its sucker");
                    subjects.add(new Subject(
                            csvRecord.get("SUBJECT"),
                            List.of(grade)));
                }

            }
            return subjects;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
