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

    private static final Class HEADERS = SubjectCsvHeaders.class;

    private static final char DELIMITER = ';';

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private final CsvParserBuilder csvParserBuilder;

    public SubjectsCsvRecordProvider(CsvParserBuilder csvParserBuilder) {
        this.csvParserBuilder = csvParserBuilder;
    }

    public CSVParser provide(InputStream inputStream) {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));
        return csvParserBuilder.build(fileReader, HEADERS, DELIMITER);
    }
}
