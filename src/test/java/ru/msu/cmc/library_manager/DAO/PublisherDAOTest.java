package ru.msu.cmc.library_manager.DAO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.model.Publisher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class PublisherDAOTest {
    @Autowired
    private PublisherDAO publisherDAO;

    @Test
    void testGetPublisherByName() {
        String[] testNames = {
                "АСТ",
                "Эксмо",
                "Росмэн",
                "URSS",
                "Азбука",
                "Искатель",
                "Neoclassic",
        };
        for (String testName : testNames) {
            List<Publisher> publishers = publisherDAO.getPublisherByName(testName);
            assertEquals(1, publishers.size());

            Publisher publisher = publishers.get(0);
            assertEquals(testName, publisher.getName());
        }
    }
}
