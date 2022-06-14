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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        List<Subject> providedSubjects =
                assertDoesNotThrow(() -> getSubjectsFromFile("validSubjectsWithUnorderedColumns.csv"));
        List<Subject> expectedSubjects = createExpectedSubjects();
        assertEquals(expectedSubjects, providedSubjects);
    }

    @Test
    public void validSubjectsWithoutHeaderRow() {
        List<Subject> providedSubjects =
                assertDoesNotThrow(() -> getSubjectsFromFile("validSubjectsWithoutHeaderRow.csv"));
//        tu powinien lecieć nasz wyjątek, że nie można zamportować danych jeśli nie mamy headerów i
//        powinno być podane jakie nazwy kolumn są poprawne i jakieś wartości dla danej kolumny są poprawne
    }

    @Test
    public void fileWithOnlyHeaders() {
        List<Subject> providedSubjects =
                assertDoesNotThrow(() -> getSubjectsFromFile("fileWithOnlyHeaders.csv"));
//        tu powinien lecieć nasz wyjątek, że nie można zamportować danych jeśli nie ma żadnych danych i
//        powinno być podane jakie nazwy kolumn są poprawne i jakieś wartości dla danej kolumny są poprawne
    }

    private List<Subject> createExpectedSubjects() {
        return List.of(
                new Subject("CHEM", List.of(
                        createGrade(4.5, "2022-02-04", "produkcja metamemfataminy"),
                        createGrade(6.0, "2022-02-07", "produkcja metamemfataminy 3"))
                ),
                new Subject("BIOLOGY", List.of(
                        createGrade(4.0, "2022-02-03", "Test z układu rozrodczego 2"),
                        createGrade(3.0, "2022-06-03", "Test z układu rozrodczego 2"),
                        createGrade(2.5, "2022-02-23", "Test z układu rozrodczego 4"))
                ),
                new Subject("MATH", List.of(
                        createGrade(1.0, "2022-02-02", "Sprawdzian z ułamków"),
                        createGrade(2.0, "2022-02-22", "Sprawdzian z ułamków 1"))
                ));
    }

    private Grade createGrade(double rateValue, String date, String description) {
        return new Grade(new Rate(rateValue), LocalDate.parse(date), description);
    }
}