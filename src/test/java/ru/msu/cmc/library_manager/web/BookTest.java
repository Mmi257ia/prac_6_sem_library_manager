package ru.msu.cmc.library_manager.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.lib.BookAndStatus;
import ru.msu.cmc.library_manager.lib.ProductWithCountAndAuthors;
import ru.msu.cmc.library_manager.model.Book;
import ru.msu.cmc.library_manager.model.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class BookTest extends CommonTest {
    @Test
    void bookTest() {
        driver.findElement(By.linkText("Книги")).click();
        driver.findElements(By.id("contentRow")).get(1).findElement(By.id("infoButton")).click();
        wait100();
        driver.findElements(By.id("contentRow")).get(5).findElement(By.id("infoButton")).click();
        wait100();
        assertEquals("Экземпляр книги", driver.getTitle());

        Book book = bookDAO.getById(4);
        // check info
        BookAndStatus BAS = new BookAndStatus(book, issueDAO);
        Product product = book.getProduct();
        ProductWithCountAndAuthors PWCAA = new ProductWithCountAndAuthors(product, authorDAO, bookDAO, issueDAO);
        assertEquals(book.getProduct().getName(),
                driver.findElement(By.id("productNameShow")).getText());
        assertEquals(rootUrl + "products/" + product.getId(),
                driver.findElement(By.id("productNameShow")).getAttribute("href"));
        assertEquals(product.getIsbn(), driver.findElement(By.id("isbnShow")).getText());
        assertEquals(PWCAA.getAuthorsString(), driver.findElement(By.id("authorsShow")).getText());
        assertEquals(rootUrl + "products?authors=%D0%9A%D0%B8%D1%80%20%D0%91%D1%83%D0%BB%D1%8B%D1%87%D1%91%D0%B2",
                driver.findElement(By.id("authorsShow")).getAttribute("href"));
        assertEquals(product.getPublisher().getName(), driver.findElement(By.id("publisherShow")).getText());
        assertEquals(rootUrl + "products?publisher=%D0%A0%D0%BE%D1%81%D0%BC%D1%8D%D0%BD",
                driver.findElement(By.id("publisherShow")).getAttribute("href"));
        assertEquals("Год издания: " + (product.getYearOfPublishing() == null ? "-" : + product.getYearOfPublishing()),
                driver.findElement(By.id("yearOfPublishingShow")).getText());
        assertEquals("" + book.getId(), driver.findElement(By.id("idShow")).getText());
        assertEquals(book.getReceivingDate().toString(),
                driver.findElement(By.id("receivingDateShow")).getText());
        if (BAS.isIssued())
            assertEquals("Не в библиотеке", driver.findElement(By.id("statusShow")).getText());
        else
            assertEquals("В библиотеке", driver.findElement(By.id("statusShow")).getText());

        // check events
        List<WebElement> rows = driver.findElements(By.id("contentRow"));
        assertEquals(1, rows.size());
        WebElement row = rows.get(0);
        assertEquals("Выдача", row.findElement(By.id("eventShow")).getText());
        assertEquals("4", row.findElement(By.id("readerIdShow")).getText());
        assertEquals("Васильев Валерий А", row.findElement(By.id("readerNameShow")).getText());
        assertEquals("2023-09-05", row.findElement(By.id("dateShow")).getText());
        assertEquals("2024-09-05", row.findElement(By.id("deadlineShow")).getText());

        // are links valid?
        String href = rootUrl + "readers/4";
        assertEquals(href, row.findElement(By.id("readerIdShow")).findElement(By.xpath("./..")).getAttribute("href"));
        assertEquals(href, row.findElement(By.id("readerNameShow")).findElement(By.xpath("./..")).getAttribute("href"));

        if (!BAS.isIssued())
            assertEquals(rootUrl + "issue?readerId=" + book.getId(),
                    driver.findElement(By.id("issueButton")).findElement(By.xpath("./..")).getAttribute("href"));
        else {
            assertEquals("Вернуть книгу", driver.findElement(By.id("returnButton")).getText());
            // deadline extend checking
            driver.findElement(By.id("extendInput")).sendKeys("01-01-2025");
            driver.findElement(By.id("extendButton")).click();
            wait100();
            assertEquals("2025-01-01", driver.findElement(By.id("deadlineShow")).getText());
            driver.findElement(By.id("extendInput")).sendKeys("05-09-2024");
            driver.findElement(By.id("extendButton")).click();
            wait100();
        }
    }
}
