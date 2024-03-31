package ru.msu.cmc.library_manager.web;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.DAO.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(locations = "classpath:application.properties")
public abstract class CommonTest {
    @Autowired
    protected ChromeDriver driver;
    @Autowired
    protected AuthorDAO authorDAO;
    @Autowired
    protected PublisherDAO publisherDAO;
    @Autowired
    protected ReaderDAO readerDAO;
    @Autowired
    protected ProductDAO productDAO;
    @Autowired
    protected BookDAO bookDAO;
    @Autowired
    protected IssueDAO issueDAO;

    @Value("${websiteUrl}")
    protected String rootUrl;

    protected void wait100() {
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(100));
    }

    protected void testFilter(String expected, String param, int expectedCount) {
        WebElement row = driver.findElement(By.id("formRow"));
        row.findElement(By.name(param)).sendKeys(expected);
        row.findElement(By.id("filterButton")).click();
        wait100();
        List<WebElement> rows = driver.findElements(By.id("contentRow"));
        assertEquals(expectedCount, rows.size());
        assertEquals(expected, rows.get(0).findElement(By.id(param + "Show")).getText());
        row = driver.findElement(By.id("formRow"));
        WebElement filterField = row.findElement(By.name(param));
        for (int i = 0; i < expected.length(); i++)
            filterField.sendKeys(Keys.BACK_SPACE); // reset filter
        row.findElement(By.id("filterButton")).click();
        wait100();
    }
}
