package ru.msu.cmc.library_manager.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.model.Author;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class AuthorTest extends CommonTest {
    @Test
    void authorsTest() {
        driver.findElement(By.linkText("Авторы")).click();
        List<WebElement> cells = driver.findElements(By.className("tableText"));
        List<Author> authors = authorDAO.getAll();
        assertEquals(authors.size(), cells.size());

        for (int i = 0; i < authors.size(); i++)
            assertEquals(authors.get(i).getName(), cells.get(i).getText()); // do all authors show?

        String testAuthorName = "Test Author";

        driver.findElement(By.name("name")).sendKeys(testAuthorName);
        driver.findElement(By.id("addNewAuthor")).click();
        wait100();

        cells = driver.findElements(By.className("tableText"));
        assertEquals(authors.size() + 1, cells.size()); // did new author insert?
        boolean flag = false;
        for (WebElement cell : cells)
            if (cell.getText().equals(testAuthorName)) {
                flag = true;
                break;
            }
        assertTrue(flag);

        List<WebElement> rows = driver.findElements(By.className("tableRow"));
        for (WebElement row : rows) {
            String authorName = row.findElement(By.className("tableText")).getText();
            if (authorName.equals(testAuthorName)) {
                row.findElement(By.className("deleteButton")).click();
                break;
            }
        }
        wait100();
        cells = driver.findElements(By.className("tableText"));
        assertEquals(authors.size(), cells.size()); // does new author delete?

        rows = driver.findElements(By.className("tableRow"));
        for (WebElement row : rows) {
            String authorName = row.findElement(By.className("tableText")).getText();
            if (authorName.equals("Борис Васильев")) {
                row.findElement(By.className("buttonSideBorders")).click(); // only Книги автора matches
                break;
            }
        }
        wait100();
        assertEquals(rootUrl + "products?authors=%D0%91%D0%BE%D1%80%D0%B8%D1%81%20%D0%92%D0%B0%D1%81%D0%B8%D0%BB%D1%8C%D0%B5%D0%B2",
                driver.getCurrentUrl()); // does Книги автора work?
    }
}
