package ru.msu.cmc.library_manager.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.model.Reader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ReaderTest extends CommonTest {
    @Test
    void readerTest() {
        driver.findElement(By.linkText("Читатели")).click();
        driver.findElements(By.id("contentRow")).get(0).findElement(By.id("infoButton")).click();
        wait100();
        assertEquals("Читатель", driver.getTitle());

        Reader reader = readerDAO.getById(1);
        // check personal info
        assertEquals("Читатель " + reader.getName(), driver.findElement(By.className("pageName")).getText());
        assertEquals("" + reader.getId(), driver.findElement(By.id("idShow")).getText());
        assertEquals(reader.getName(), driver.findElement(By.id("nameInput")).getAttribute("value"));
        assertEquals(reader.getAddress(), driver.findElement(By.id("addressInput")).getAttribute("value"));
        assertEquals("" + reader.getPhone(), driver.findElement(By.id("phoneInput")).getAttribute("value"));

        // check editing info
        driver.findElement(By.id("nameInput")).sendKeys("Q");
        driver.findElement(By.id("editButton")).click();
        wait100();
        assertEquals(reader.getName() + "Q", driver.findElement(By.id("nameInput")).getAttribute("value"));
        driver.findElement(By.id("nameInput")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.id("editButton")).click();
        wait100();
        driver.findElement(By.id("addressInput")).sendKeys("Q");
        driver.findElement(By.id("editButton")).click();
        wait100();
        assertEquals(reader.getAddress() + "Q", driver.findElement(By.id("addressInput")).getAttribute("value"));
        driver.findElement(By.id("addressInput")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.id("editButton")).click();
        wait100();
        driver.findElement(By.id("phoneInput")).sendKeys("5");
        driver.findElement(By.id("editButton")).click();
        wait100();
        assertEquals(reader.getPhone() + "5", driver.findElement(By.id("phoneInput")).getAttribute("value"));
        driver.findElement(By.id("phoneInput")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.id("editButton")).click();
        wait100();

        // check history (last 5 entries)
        List<WebElement> rows = driver.findElements(By.id("contentRow")).reversed();
        assertTrue(rows.size() >= 5); // we will use last 6 events to check
        String[] events = {"Выдача", "Возврат", "Выдача", "Возврат", "Выдача"};
        String[] bookIds = {"1", "1", "12", "12", "17"};
        String[] bookNames = {"Шаг вперёд, два шага назад", "Шаг вперёд, два шага назад",
                "Война и мир", "Война и мир", "Властелин колец. Хранители Кольца"};
        String[] dates = {"2023-01-01", "2023-02-01",    "2023-02-10", "2023-03-14", "2023-03-25"};
        String[] deadlines = {"-", "-", "2024-02-10", "2024-02-10", "2024-03-25"};

        for (int i = 0; i < 5; i++) { // do needed events show?
            assertEquals(events[i], rows.get(i).findElement(By.id("eventShow")).getText());
            assertEquals(bookIds[i], rows.get(i).findElement(By.id("bookIdShow")).getText());
            assertEquals(bookNames[i], rows.get(i).findElement(By.id("bookNameShow")).getText());
            assertEquals(dates[i], rows.get(i).findElement(By.id("dateShow")).getText());
            assertEquals(deadlines[i], rows.get(i).findElement(By.id("deadlineShow")).getText());
        }

        WebElement lastRow = rows.get(4); // are links valid?
        String[] hrefs = {rootUrl + "books/17",
                rootUrl + "products?name=%D0%92%D0%BB%D0%B0%D1%81%D1%82%D0%B5%D0%BB%D0%B8%D0%BD%20%D0%BA%D0%BE%D0%BB%D0%B5%D1%86.%20%D0%A5%D1%80%D0%B0%D0%BD%D0%B8%D1%82%D0%B5%D0%BB%D0%B8%20%D0%9A%D0%BE%D0%BB%D1%8C%D1%86%D0%B0"};
        assertEquals(hrefs[0], lastRow.findElement(By.id("bookIdShow")).findElement(By.xpath("./..")).getAttribute("href"));
        assertEquals(hrefs[1], lastRow.findElement(By.id("bookNameShow")).findElement(By.xpath("./..")).getAttribute("href"));

        // deadline extend checking
        lastRow.findElement(By.id("extendInput")).sendKeys("26-03-2024");
        lastRow.findElement(By.id("extendButton")).click();
        wait100();
        lastRow = driver.findElements(By.id("contentRow")).reversed().get(4);
        assertEquals("2024-03-26", lastRow.findElement(By.id("deadlineShow")).getText());
        lastRow.findElement(By.id("extendInput")).sendKeys("25-03-2024");
        lastRow.findElement(By.id("extendButton")).click();
        wait100();

        lastRow = driver.findElements(By.id("contentRow")).reversed().get(4);
        assertEquals("Вернуть книгу", lastRow.findElement(By.id("returnButton")).getText());
        assertEquals(rootUrl + "issue?readerId=" + reader.getId(),
                driver.findElement(By.id("issueButton")).findElement(By.xpath("./..")).getAttribute("href"));
    }
}
