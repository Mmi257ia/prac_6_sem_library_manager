package ru.msu.cmc.library_manager.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.DAO.BookDAO;
import ru.msu.cmc.library_manager.lib.BookAndStatus;
import ru.msu.cmc.library_manager.lib.ProductWithCountAndAuthors;
import ru.msu.cmc.library_manager.model.Book;
import ru.msu.cmc.library_manager.model.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ProductTest extends CommonTest {
    @Test
    void productTest() {
        driver.findElement(By.linkText("Книги")).click();
        driver.findElements(By.id("contentRow")).get(1).findElement(By.id("infoButton")).click();
        wait100();
        assertEquals("Экземпляры книги", driver.getTitle());

        Product product = productDAO.getById(2);
        ProductWithCountAndAuthors PWCAA = new ProductWithCountAndAuthors(product, authorDAO, bookDAO, issueDAO);
        // check info
        assertEquals("Экземпляры книги \"" + product.getName() + "\"",
                driver.findElement(By.className("pageName")).getText());
        assertEquals(product.getIsbn(), driver.findElement(By.id("isbnShow")).getText());
        assertEquals(PWCAA.getAuthorsString(), driver.findElement(By.id("authorsShow")).getText());
        assertEquals(rootUrl + "products?authors=%D0%9A%D0%B8%D1%80%20%D0%91%D1%83%D0%BB%D1%8B%D1%87%D1%91%D0%B2",
                driver.findElement(By.id("authorsShow")).getAttribute("href"));
        assertEquals(product.getPublisher().getName(), driver.findElement(By.id("publisherShow")).getText());
        assertEquals(rootUrl + "products?publisher=%D0%A0%D0%BE%D1%81%D0%BC%D1%8D%D0%BD",
                driver.findElement(By.id("publisherShow")).getAttribute("href"));
        assertEquals("Год издания: " + (product.getYearOfPublishing() == null ? "-" : + product.getYearOfPublishing()),
                driver.findElement(By.id("yearOfPublishingShow")).getText());

        // check books
        List<WebElement> rows = driver.findElements(By.id("contentRow"));
        List<Book> books = bookDAO.getBooksByFilter(new BookDAO.Filter(product, null, null));
        assertEquals(books.size(), rows.size());

        for (Book book : books) {
            BookAndStatus BAS = new BookAndStatus(book, issueDAO);
            WebElement row = rows.get(0);
            boolean flag = false;
            for (WebElement rowIter : rows)
                if (rowIter.findElement(By.id("idShow")).getText().equals("" + book.getId())) {
                    row = rowIter;
                    flag = true;
                    break;
                }
            assertTrue(flag);

            assertEquals(book.getReceivingDate().toString(), row.findElement(By.id("receivingDateShow")).getText());
            if (BAS.isIssued())
                assertEquals("У читателя", row.findElement(By.id("statusShow")).getText());
            else
                assertEquals("В библиотеке", row.findElement(By.id("statusShow")).getText());
            if (BAS.getDeadline() == null)
                assertEquals("-", row.findElement(By.id("deadlineShow")).getText());
            else
                assertEquals(BAS.getDeadline().toString(), row.findElement(By.id("deadlineShow")).getText());

            assertEquals(rootUrl + "books/" + book.getId(),
                    row.findElement(By.id("infoButton")).findElement(By.xpath("./..")).getAttribute("href"));
            if (!BAS.isIssued())
                assertEquals(rootUrl + "issue?bookId=" + book.getId(),
                        row.findElement(By.id("issueButton")).findElement(By.xpath("./..")).getAttribute("href"));
            else
                assertEquals("Вернуть книгу", row.findElement(By.id("returnButton")).getText());
        }

        driver.findElement(By.id("receivingDateInput")).sendKeys("01-01-2025");
        driver.findElement(By.id("addButton")).click();
        wait100();

        rows = driver.findElements(By.id("contentRow"));
        assertEquals(books.size() + 1, rows.size());
        WebElement row = rows.get(0);
        boolean flag = false;
        for (WebElement rowIter : rows)
            if (rowIter.findElement(By.id("receivingDateShow")).getText().equals("2025-01-01")) {
                row = rowIter;
                flag = true;
                break;
            }
        assertTrue(flag);
        row.findElement(By.id("deleteButton")).click();
        wait100();
        rows = driver.findElements(By.id("contentRow"));
        assertEquals(books.size(), rows.size());
    }
}
