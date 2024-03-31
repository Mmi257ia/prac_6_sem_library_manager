package ru.msu.cmc.library_manager.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class HistoryTest extends CommonTest {
    @Test
    void historyTest() {
        driver.findElement(By.linkText("История")).click();
        List<WebElement> rows = driver.findElements(By.id("contentRow")).reversed();
        assertTrue(rows.size() >= 6); // we will use last 6 events to check
        String[] events = {"Выдача", "Возврат", "Выдача", "Выдача", "Возврат", "Выдача"};
        String[] bookIds = {"1", "1", "1", "12", "12", "17"};
        String[] bookNames = {"Шаг вперёд, два шага назад", "Шаг вперёд, два шага назад", "Шаг вперёд, два шага назад",
                "Война и мир", "Война и мир", "Властелин колец. Хранители Кольца"};
        String[] readerIds = {"1", "1", "2", "1", "1", "1"};
        String[] readerNames = {"Алмазова Инесса Михайловна", "Алмазова Инесса Михайловна", "Михайлов Роман Бальтазарович",
                "Алмазова Инесса Михайловна", "Алмазова Инесса Михайловна", "Алмазова Инесса Михайловна"};
        String[] dates = {"2023-01-01", "2023-02-01", "2023-02-02", "2023-02-10", "2023-03-14", "2023-03-25"};
        String[] deadlines = {"-", "-", "2025-02-02", "2024-02-10", "2024-02-10", "2024-03-25"};

        for (int i = 0; i < 6; i++) { // do needed events show?
            assertEquals(events[i], rows.get(i).findElement(By.id("eventShow")).getText());
            assertEquals(bookIds[i], rows.get(i).findElement(By.id("bookIdShow")).getText());
            assertEquals(bookNames[i], rows.get(i).findElement(By.id("bookNameShow")).getText());
            assertEquals(readerIds[i], rows.get(i).findElement(By.id("readerIdShow")).getText());
            assertEquals(readerNames[i], rows.get(i).findElement(By.id("readerNameShow")).getText());
            assertEquals(dates[i], rows.get(i).findElement(By.id("dateShow")).getText());
            assertEquals(deadlines[i], rows.get(i).findElement(By.id("deadlineShow")).getText());
        }

        WebElement lastRow = rows.get(0); // are links valid?
        String[] hrefs = {rootUrl + "books/1", rootUrl + "products?name=%D0%A8%D0%B0%D0%B3%20%D0%B2%D0%BF%D0%B5%D1%80%D1%91%D0%B4,%20%D0%B4%D0%B2%D0%B0%20%D1%88%D0%B0%D0%B3%D0%B0%20%D0%BD%D0%B0%D0%B7%D0%B0%D0%B4",
                rootUrl + "readers/1", rootUrl + "readers?name=%D0%90%D0%BB%D0%BC%D0%B0%D0%B7%D0%BE%D0%B2%D0%B0%20%D0%98%D0%BD%D0%B5%D1%81%D1%81%D0%B0%20%D0%9C%D0%B8%D1%85%D0%B0%D0%B9%D0%BB%D0%BE%D0%B2%D0%BD%D0%B0"};
        assertEquals(hrefs[0], lastRow.findElement(By.id("bookIdShow")).findElement(By.xpath("./..")).getAttribute("href"));
        assertEquals(hrefs[1], lastRow.findElement(By.id("bookNameShow")).findElement(By.xpath("./..")).getAttribute("href"));
        assertEquals(hrefs[2], lastRow.findElement(By.id("readerIdShow")).findElement(By.xpath("./..")).getAttribute("href"));
        assertEquals(hrefs[3], lastRow.findElement(By.id("readerNameShow")).findElement(By.xpath("./..")).getAttribute("href"));

        driver.findElement(By.id("dateBegin")).sendKeys("01-06-2023");
        driver.findElement(By.id("dateEnd")).sendKeys("01-10-2023");
        driver.findElement(By.id("readerId")).sendKeys("1");
        driver.findElement(By.id("filterButton")).click();
        wait100();
        rows = driver.findElements(By.id("contentRow"));
        assertEquals(5, rows.size());
        for (WebElement row : rows)
            assertEquals("1", row.findElement(By.id("readerIdShow")).getText()); // filter checking
    }
}
