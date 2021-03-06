package pl.shonsu.gradebook.helper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.shonsu.gradebook.importer.csv.helper.CSVHelper;
import pl.shonsu.gradebook.model.Grade;
import pl.shonsu.gradebook.model.Rate;
import pl.shonsu.gradebook.model.Subject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CSVHelperTest {

    @Autowired
    private CSVHelper csvHelper;

    @Test
    public void emptyFile() {
        List<Subject> providedSubjects = assertDoesNotThrow(() -> getSubjectsFromFile("emptyFile.csv"));
        assertEquals(List.of(), providedSubjects);
    }

    private List<Subject> getSubjectsFromFile(String fileName) throws IOException, URISyntaxException {
        return csvHelper.csvToSubject(getInputStream(fileName));
    }

    private InputStream getInputStream(String fileName) throws IOException, URISyntaxException {
        URL resource = CSVHelperTest.class.getResource(fileName);
        Objects.requireNonNull(resource);
        return Files.newInputStream(Paths.get(resource.toURI()));
    }

    @Test
    public void validFile() {
        List<Subject> providedSubjects =
                assertDoesNotThrow(() -> getSubjectsFromFile("validSubjects.csv"));
        List<Subject> expectedSubjects = createExpectedSubjects();
        assertEquals(expectedSubjects, providedSubjects);
    }

    @Test
    public void validSubjectsWithUnorderedColumns() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> getSubjectsFromFile("validSubjectsWithUnorderedColumns.csv"));

        assertEquals("Wrong header [RATE] at column [0]" +
                System.lineSeparator() +
                "Headers should looks like: [SUBJECT;RATE;RATE_DATE;DESCRIPTION]", exception.getMessage());
    }

    @Test
    public void validSubjectsWithoutHeaderRow() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> getSubjectsFromFile("validSubjectsWithoutHeaderRow.csv"));

        assertEquals("Wrong header [MATH] at column [0]" +
                System.lineSeparator() +
                "Headers should looks like: [SUBJECT;RATE;RATE_DATE;DESCRIPTION]", exception.getMessage());
//        tu powinien lecie?? nasz wyj??tek, ??e nie mo??na zamportowa?? danych je??li nie mamy header??w i
//        powinno by?? podane jakie nazwy kolumn s?? poprawne i jakie?? warto??ci dla danej kolumny s?? poprawne
    }

    @Test
    public void fileWithOnlyHeaders() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> getSubjectsFromFile("fileWithOnlyHeaders.csv"));

        assertEquals("Csv file has no data.", exception.getMessage());
//        tu powinien lecie?? nasz wyj??tek, ??e nie mo??na zamportowa?? danych je??li nie ma ??adnych danych i
//        powinno by?? podane jakie nazwy kolumn s?? poprawne i jakie?? warto??ci dla danej kolumny s?? poprawne

    }

    private List<Subject> createExpectedSubjects() {
        return List.of(
                new Subject("CHEM", List.of(
                        createGrade(4.5, "2022-02-04", "produkcja metamemfataminy"),
                        createGrade(6.0, "2022-02-07", "produkcja metamemfataminy 3"))
                ),
                new Subject("BIOLOGY", List.of(
                        createGrade(4.0, "2022-02-03", "Test z uk??adu rozrodczego 2"),
                        createGrade(3.0, "2022-06-03", "Test z uk??adu rozrodczego 2"),
                        createGrade(2.5, "2022-02-23", "Test z uk??adu rozrodczego 4"))
                ),
                new Subject("MATH", List.of(
                        createGrade(1.0, "2022-02-02", "Sprawdzian z u??amk??w"),
                        createGrade(2.0, "2022-02-22", "Sprawdzian z u??amk??w 1"))
                ));
    }

    private Grade createGrade(double rateValue, String date, String description) {
        return new Grade(new Rate(rateValue), LocalDate.parse(date), description);
    }
}