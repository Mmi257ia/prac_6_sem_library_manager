package ru.msu.cmc.library_manager.web;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.model.Publisher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class PublisherTest extends CommonTest {
    @Test
    void publishersTest() {
        driver.findElement(By.linkText("Издательства")).click();
        List<WebElement> cells = driver.findElements(By.className("tableText"));
        List<Publisher> publishers = publisherDAO.getAll();
        assertEquals(publishers.size(), cells.size());

        for (int i = 0; i < publishers.size(); i++)
            assertEquals(publishers.get(i).getName(), cells.get(i).getText()); // do all publishers show?

        String testPublisherName = "Test Publisher";

        driver.findElement(By.name("name")).sendKeys(testPublisherName);
        driver.findElement(By.id("addNewPublisher")).click();
        wait100();

        cells = driver.findElements(By.className("tableText"));
        assertEquals(publishers.size() + 1, cells.size()); // did new publisher insert?
        boolean flag = false;
        for (WebElement cell : cells)
            if (cell.getText().equals(testPublisherName)) {
                flag = true;
                break;
            }
        assertTrue(flag);

        List<WebElement> rows = driver.findElements(By.className("tableRow"));
        for (WebElement row : rows) {
            String authorName = row.findElement(By.className("tableText")).getText();
            if (authorName.equals(testPublisherName)) {
                row.findElement(By.className("deleteButton")).click();
                break;
            }
        }
        wait100();
        cells = driver.findElements(By.className("tableText"));
        assertEquals(publishers.size(), cells.size()); // does new author delete?

        rows = driver.findElements(By.className("tableRow"));
        for (WebElement row : rows) {
            String authorName = row.findElement(By.className("tableText")).getText();
            if (authorName.equals("Эксмо")) {
                row.findElement(By.className("buttonSideBorders")).click(); // only Книги издательства matches
                break;
            }
        }
        wait100();
        assertEquals("http://localhost:8080/products?publisher=%D0%AD%D0%BA%D1%81%D0%BC%D0%BE",
                driver.getCurrentUrl()); // does Книги издательства work?
    }
}
