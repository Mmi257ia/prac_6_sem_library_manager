package ru.msu.cmc.library_manager.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.lib.ProductWithCountAndAuthors;
import ru.msu.cmc.library_manager.model.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ProductsTest extends CommonTest {
    @Test
    void productsTest() {
        driver.findElement(By.linkText("Книги")).click();
        List<WebElement> rows = driver.findElements(By.id("contentRow"));
        List<Product> products = productDAO.getAll();
        assertEquals(products.size(), rows.size());

        for (int i = 0; i < products.size(); i++) { // do all products show?
            assertEquals(products.get(i).getIsbn(), rows.get(i).findElement(By.id("isbnShow")).getText());
            assertEquals(products.get(i).getName(), rows.get(i).findElement(By.id("nameShow")).getText());
            ProductWithCountAndAuthors PWCAA = new ProductWithCountAndAuthors(products.get(i), authorDAO, bookDAO, issueDAO);
            assertEquals(PWCAA.getAuthorsString(), rows.get(i).findElement(By.id("authorsShow")).getText());
            assertEquals(products.get(i).getPublisher().getName(), rows.get(i).findElement(By.id("publisherShow")).getText());
            Integer yearOfPublishing = products.get(i).getYearOfPublishing();
            if (yearOfPublishing == null)
                assertEquals("-", rows.get(i).findElement(By.id("yearOfPublishingShow")).getText());
            else
                assertEquals(yearOfPublishing.toString(), rows.get(i).findElement(By.id("yearOfPublishingShow")).getText());
            assertEquals("" + PWCAA.getCount(), rows.get(i).findElement(By.id("countShow")).getText());
            assertEquals("" + PWCAA.getTotal(), rows.get(i).findElement(By.id("totalShow")).getText());
        }

        // test filter
        Product product = productDAO.getById(2);
        ProductWithCountAndAuthors PWCAA = new ProductWithCountAndAuthors(product, authorDAO, bookDAO, issueDAO);
        testFilter(product.getIsbn(), "isbn", 1);
        testFilter(product.getName(), "name", 1);
        testFilter(PWCAA.getAuthorsString(), "authors", 3);
        testFilter(product.getPublisher().getName(), "publisher", 1);
        // test yearOfPublishing filter
        WebElement row = driver.findElement(By.id("formRow"));
        row.findElement(By.name("yearOfPublishingBegin")).sendKeys("2020");
        row.findElement(By.name("yearOfPublishingEnd")).sendKeys("2022");
        row.findElement(By.id("filterButton")).click();
        wait100();
        rows = driver.findElements(By.id("contentRow"));
        assertEquals(4, rows.size());
        for (WebElement rowIter : rows) {
            int yearOfPublishing = Integer.parseInt(rowIter.findElement(By.id("yearOfPublishingShow")).getText());
            assertTrue(yearOfPublishing >= 2020);
            assertTrue(yearOfPublishing <= 2022);
        }
        row = driver.findElement(By.id("formRow"));
        WebElement filterField = row.findElement(By.name("yearOfPublishingBegin"));
        for (int i = 0; i < 4; i++)
            filterField.sendKeys(Keys.BACK_SPACE); // reset filter
        filterField = row.findElement(By.name("yearOfPublishingEnd"));
        for (int i = 0; i < 4; i++)
            filterField.sendKeys(Keys.BACK_SPACE); // reset filter
        row.findElement(By.id("filterButton")).click();
        wait100();
        // test isPresent radiobox
        row = driver.findElement(By.id("formRow"));
        row.findElement(By.id("isPresent")).click();
        row.findElement(By.id("filterButton")).click();
        wait100();
        rows = driver.findElements(By.id("contentRow"));
        assertEquals(7, rows.size());
        for (WebElement rowIter : rows) {
            int countShow = Integer.parseInt(rowIter.findElement(By.id("countShow")).getText());
            assertTrue(countShow > 0);
        }
        row = driver.findElement(By.id("formRow"));
        row.findElement(By.id("filterButton")).click();
        wait100();

        // test adding
        row = driver.findElement(By.id("formRow"));
        row.findElement(By.name("isbn")).sendKeys("1234567890123");
        row.findElement(By.name("name")).sendKeys("Test Name");
        row.findElement(By.name("authors")).sendKeys("Кир Булычёв");
        row.findElement(By.name("publisher")).sendKeys("АСТ");
        row.findElement(By.name("yearOfPublishing")).sendKeys("2025");
        row.findElement(By.id("addButton")).click();
        wait100();
        rows = driver.findElements(By.id("contentRow"));
        assertEquals(products.size() + 1, rows.size());
        assertEquals("1234567890123", rows.get(rows.size() - 1).findElement(By.id("isbnShow")).getText());
        assertEquals("Test Name", rows.get(rows.size() - 1).findElement(By.id("nameShow")).getText());
        assertEquals("Кир Булычёв", rows.get(rows.size() - 1).findElement(By.id("authorsShow")).getText());
        assertEquals("АСТ", rows.get(rows.size() - 1).findElement(By.id("publisherShow")).getText());
        assertEquals("2025", rows.get(rows.size() - 1).findElement(By.id("yearOfPublishingShow")).getText());
        rows.get(rows.size() - 1).findElement(By.id("deleteButton")).click();
        wait100();
        rows = driver.findElements(By.id("contentRow"));
        assertEquals(products.size(), rows.size()); // check delete

        for (WebElement currRow : rows) // check buttons
            if (currRow.findElement(By.id("isbnShow")).getText().equals(product.getIsbn())) {
                assertEquals(rootUrl + "products/" + product.getId(),
                        currRow.findElement(By.id("infoButton")).findElement(By.xpath("./..")).getAttribute("href"));
                break;
            }
    }
}
