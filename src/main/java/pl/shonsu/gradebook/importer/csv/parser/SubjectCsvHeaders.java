package pl.shonsu.gradebook.importer.csv.parser;

import java.util.Arrays;
import java.util.List;

public enum SubjectCsvHeaders {
    SUBJECT, RATE, RATE_DATE, DESCRIPTION;

    public static List<String> names(){
        return Arrays.stream(values()).map(Enum::name).toList();
    }
}
