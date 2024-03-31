package ru.msu.cmc.library_manager.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class HeaderTest extends CommonTest {
    @Test
    void headerTest() {
        assertEquals("Управление библиотекой", driver.getTitle());

        String[][] linksAndHeaders = {
                {"Авторы", "Авторы"},
                {"Издательства", "Издательства"},
                {"Читатели", "Читатели"},
                {"Книги", "Книги"},
                {"История", "История"},
                {"Выдать книгу", "Выдача книги"},
                {"Домашняя страница", "Управление библиотекой"}
        };

        WebElement button;
        for (String[] pair : linksAndHeaders) {
            button = driver.findElement(By.linkText(pair[0]));
            button.click();
            wait100();
            assertEquals(pair[1], driver.getTitle());
        }
    }
}
