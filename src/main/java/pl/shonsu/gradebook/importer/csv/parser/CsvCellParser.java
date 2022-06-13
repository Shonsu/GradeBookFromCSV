package pl.shonsu.gradebook.importer.csv.parser;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Component
public class CsvCellParser {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());

    public LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException dateTimeParseException) {
            throw new RuntimeException("Error while parsing date: " + date);
        }
    }

    public Double parseDouble(String number) {
        try {
            return numberFormat.parse(number).doubleValue();
        } catch (ParseException e) {
            throw new RuntimeException("Error while parsing number: " + number);
        }
    }
}
