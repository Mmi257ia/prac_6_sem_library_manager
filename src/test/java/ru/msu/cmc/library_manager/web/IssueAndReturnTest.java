package ru.msu.cmc.library_manager.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class IssueAndReturnTest extends CommonTest {
    private void checkHistory63ReturnViaBook(String deadlineToCheck) {
        assertEquals("Управление библиотекой", driver.getTitle());
        driver.findElement(By.linkText("История")).click();
        wait100();
        WebElement row = driver.findElements(By.id("contentRow")).get(0);
        assertEquals("Выдача", row.findElement(By.id("eventShow")).getText());
        assertEquals("3", row.findElement(By.id("bookIdShow")).getText());
        assertEquals("Девочка с земли. Путешествие Алисы", row.findElement(By.id("bookNameShow")).getText());
        assertEquals("6", row.findElement(By.id("readerIdShow")).getText());
        assertEquals("Иновенков Игорь Николаевич", row.findElement(By.id("readerNameShow")).getText());
        assertEquals(new Date(new java.util.Date().getTime()).toString(), row.findElement(By.id("dateShow")).getText());
        assertEquals(deadlineToCheck, row.findElement(By.id("deadlineShow")).getText());

        row.findElement(By.id("bookIdShow")).click();
        wait100();
        driver.findElement(By.id("returnButton")).click();
        wait100();

        assertEquals("Управление библиотекой", driver.getTitle());
        driver.findElement(By.linkText("История")).click();
        wait100();
        row = driver.findElements(By.id("contentRow")).get(0);
        assertEquals("Возврат", row.findElement(By.id("eventShow")).getText());
        assertEquals("3", row.findElement(By.id("bookIdShow")).getText());
        assertEquals("Девочка с земли. Путешествие Алисы", row.findElement(By.id("bookNameShow")).getText());
        assertEquals("6", row.findElement(By.id("readerIdShow")).getText());
        assertEquals("Иновенков Игорь Николаевич", row.findElement(By.id("readerNameShow")).getText());
        assertEquals(new Date(new java.util.Date().getTime()).toString(), row.findElement(By.id("dateShow")).getText());
        assertEquals(deadlineToCheck, row.findElement(By.id("deadlineShow")).getText());
    }
    private void checkHistory63ReturnViaReader(String deadlineToCheck) {
        assertEquals("Управление библиотекой", driver.getTitle());
        driver.findElement(By.linkText("История")).click();
        wait100();
        WebElement row = driver.findElements(By.id("contentRow")).get(0);
        assertEquals("Выдача", row.findElement(By.id("eventShow")).getText());
        assertEquals("3", row.findElement(By.id("bookIdShow")).getText());
        assertEquals("Девочка с земли. Путешествие Алисы", row.findElement(By.id("bookNameShow")).getText());
        assertEquals("6", row.findElement(By.id("readerIdShow")).getText());
        assertEquals("Иновенков Игорь Николаевич", row.findElement(By.id("readerNameShow")).getText());
        assertEquals(new Date(new java.util.Date().getTime()).toString(), row.findElement(By.id("dateShow")).getText());
        assertEquals(deadlineToCheck, row.findElement(By.id("deadlineShow")).getText());

        row.findElement(By.id("readerIdShow")).click();
        wait100();
        driver.findElement(By.id("returnButton")).click();
        wait100();

        assertEquals("Управление библиотекой", driver.getTitle());
        driver.findElement(By.linkText("История")).click();
        wait100();
        row = driver.findElements(By.id("contentRow")).get(0);
        assertEquals("Возврат", row.findElement(By.id("eventShow")).getText());
        assertEquals("3", row.findElement(By.id("bookIdShow")).getText());
        assertEquals("Девочка с земли. Путешествие Алисы", row.findElement(By.id("bookNameShow")).getText());
        assertEquals("6", row.findElement(By.id("readerIdShow")).getText());
        assertEquals("Иновенков Игорь Николаевич", row.findElement(By.id("readerNameShow")).getText());
        assertEquals(new Date(new java.util.Date().getTime()).toString(), row.findElement(By.id("dateShow")).getText());
        assertEquals(deadlineToCheck, row.findElement(By.id("deadlineShow")).getText());
    }

    @Test
    void issueTestExplicit() {
        driver.findElement(By.linkText("Выдать книгу")).click();
        wait100();
        driver.findElement(By.name("readerId")).sendKeys("6");
        driver.findElement(By.name("bookId")).sendKeys("3");
        driver.findElement(By.name("deadline")).sendKeys("01-01-2025");
        driver.findElement(By.id("issueButton")).click();
        wait100();

        checkHistory63ReturnViaBook("2025-01-01");
    }

    @Test
    void issueTestViaReader() {
        driver.findElement(By.linkText("Читатели")).click();
        wait100();
        driver.findElements(By.id("contentRow")).get(5).findElement(By.id("issueButton")).click();
        wait100();
        driver.findElement(By.name("bookId")).sendKeys("3");
        driver.findElement(By.name("deadline")).sendKeys("01-02-2025");
        driver.findElement(By.id("issueButton")).click();
        wait100();

        checkHistory63ReturnViaReader("2025-02-01");
    }

    @Test
    void issueTestViaBook() {
        driver.findElement(By.linkText("Книги")).click();
        wait100();
        driver.findElements(By.id("contentRow")).get(1).findElement(By.id("infoButton")).click();
        wait100();
        driver.findElements(By.id("contentRow")).get(0).findElement(By.id("infoButton")).click();
        wait100();
        driver.findElement(By.id("issueButton")).click();
        wait100();
        driver.findElement(By.name("readerId")).sendKeys("6");
        driver.findElement(By.name("deadline")).sendKeys("01-03-2025");
        driver.findElement(By.id("issueButton")).click();
        wait100();

        checkHistory63ReturnViaBook("2025-03-01");
    }
}
