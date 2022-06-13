package pl.shonsu.gradebook.importer.csv.parser;

import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class SubjectsCsvRecordProvider {

    private static final Class<SubjectCsvHeaders> HEADERS = SubjectCsvHeaders.class;

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
