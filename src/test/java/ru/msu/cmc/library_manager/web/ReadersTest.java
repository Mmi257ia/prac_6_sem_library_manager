package ru.msu.cmc.library_manager.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.model.Reader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ReadersTest extends CommonTest {
    @Test
    void readersTest() {
        driver.findElement(By.linkText("Читатели")).click();
        List<WebElement> rows = driver.findElements(By.id("contentRow"));
        List<Reader> readers = readerDAO.getAll();
        assertEquals(readers.size(), rows.size());

        for (int i = 0; i < readers.size(); i++) { // do all readers show?
            assertEquals(readers.get(i).getId().toString(), rows.get(i).findElement(By.id("idShow")).getText());
            assertEquals(readers.get(i).getName(), rows.get(i).findElement(By.id("nameShow")).getText());
            assertEquals(readers.get(i).getAddress(), rows.get(i).findElement(By.id("addressShow")).getText());
            assertEquals(readers.get(i).getPhone().toString(), rows.get(i).findElement(By.id("phoneShow")).getText());
        }

        // test filter
        Reader reader = readerDAO.getById(3);
        testFilter(reader.getId().toString(), "id", 1);
        testFilter(reader.getName(), "name", 1);
        testFilter(reader.getAddress(), "address", 1);
        testFilter(reader.getPhone().toString(), "phone", 1);

        // test adding
        WebElement row = driver.findElement(By.id("formRow"));
        row.findElement(By.name("name")).sendKeys("Test Name");
        row.findElement(By.name("address")).sendKeys("Test Address");
        row.findElement(By.name("phone")).sendKeys("123456789");
        row.findElement(By.id("addButton")).click();
        wait100();
        rows = driver.findElements(By.id("contentRow"));
        assertEquals(readers.size() + 1, rows.size());
        assertEquals("Test Name", rows.get(rows.size() - 1).findElement(By.id("nameShow")).getText());
        assertEquals("Test Address", rows.get(rows.size() - 1).findElement(By.id("addressShow")).getText());
        assertEquals("123456789", rows.get(rows.size() - 1).findElement(By.id("phoneShow")).getText());
        rows.get(rows.size() - 1).findElement(By.id("deleteButton")).click();
        wait100();
        rows = driver.findElements(By.id("contentRow"));
        assertEquals(readers.size(), rows.size()); // check delete

        for (WebElement currRow : rows) // check buttons
            if (currRow.findElement(By.id("idShow")).getText().equals(reader.getId().toString())) {
                assertEquals(rootUrl + "issue?readerId=" + reader.getId(),
                        currRow.findElement(By.id("issueButton")).findElement(By.xpath("./..")).getAttribute("href"));
                assertEquals(rootUrl + "readers/" + reader.getId(),
                        currRow.findElement(By.id("infoButton")).findElement(By.xpath("./..")).getAttribute("href"));
                break;
            }
    }
}
