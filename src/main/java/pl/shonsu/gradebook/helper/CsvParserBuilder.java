package pl.shonsu.gradebook.helper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

@Component
public class CsvParserBuilder {
    public CSVParser build(BufferedReader bufferedReader, String[] headers, char delimiter) {
        try {
            return new CSVParser(bufferedReader, createCsvFormat(headers, delimiter));
        } catch (IOException e) {
            throw new RuntimeException("Error while creating csv parser", e);
        }
    }

    private CSVFormat createCsvFormat(String[] headers, char delimiter) {
        return CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .setSkipHeaderRecord(true)
                .setDelimiter(delimiter)
                .build();
    }
}
