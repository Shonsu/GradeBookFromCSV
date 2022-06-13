package pl.shonsu.gradebook.helper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

@Component
public class CsvParserBuilder {
    public CSVParser build(BufferedReader bufferedReader, Class<SubjectCsvHeaders> headers, char delimiter) {
        try {
            return new CSVParser(bufferedReader, createCsvFormat(headers, delimiter));
        } catch (IOException e) {
            throw new RuntimeException("Error while creating csv parser", e);
        }
    }

    private CSVFormat createCsvFormat(Class<SubjectCsvHeaders> headers, char delimiter) {
        return CSVFormat.DEFAULT.builder()
                .setHeader()
                .setDelimiter(delimiter)
                .build();
    }
}
