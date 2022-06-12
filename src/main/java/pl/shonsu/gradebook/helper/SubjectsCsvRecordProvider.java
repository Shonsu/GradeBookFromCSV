package pl.shonsu.gradebook.helper;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
public class SubjectsCsvRecordProvider {

    private static final String[] HEADERS = {"SUBJECT","RATE","RATE_DATE","DESCRIPTION"};
    //private static final String[] HEADERS = Arrays.stream(SubjectCsvHeaders.values()).toArray(String[]::new);

    private static final char DELIMITER = ';';

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private final CsvParserBuilder csvParserBuilder;

    public SubjectsCsvRecordProvider(CsvParserBuilder csvParserBuilder) {
        this.csvParserBuilder = csvParserBuilder;
    }

    public Iterable<CSVRecord> provide(InputStream inputStream) {
        System.out.println("inside SubjectsCsvRecordProvider.provide");
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));
        System.out.println("after new buffer reader");
        CSVParser csvParser = csvParserBuilder.build(fileReader, HEADERS, DELIMITER);
        System.out.println("after csvparserbuild");
        try {
            return csvParser.getRecords();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading csv records.", e);
        }
    }
}
