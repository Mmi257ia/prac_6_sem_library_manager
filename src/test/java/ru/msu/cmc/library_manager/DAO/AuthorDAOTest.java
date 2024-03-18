package ru.msu.cmc.library_manager.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.model.Author;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class AuthorDAOTest {
    @Autowired
    private AuthorDAO authorDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGenericMethods() {
        int kirId = 2;
        String aName = "Имя автора";
        List<Author> allAuthors = authorDAO.getAll();
        assertEquals(7, allAuthors.size());

        allAuthors.get(kirId - 1).setName("Бул Кирычёв");
        authorDAO.update(allAuthors.get(1));
        Author bulKirychov = authorDAO.getById(kirId);
        assertEquals("Бул Кирычёв", bulKirychov.getName());

        authorDAO.delete(bulKirychov);
        assertNull(authorDAO.getById(kirId));

        Author newAuthor = new Author(null, aName);
        authorDAO.save(newAuthor);
        assertEquals(aName, authorDAO.getAuthorByName(aName).get(0).getName());
    }

    @Test
    void testGetAuthorByName() {
        String[] testNames = {
                "Владимир Ленин",
                "Кир Булычёв",
                "Лев Толстой",
                "Джон Рональд Руэл Толкин",
                "Борис Васильев",
                "Крис Метцен",
                "Роберт Брукс",
        };
        for (String testName : testNames) {
            List<Author> authors = authorDAO.getAuthorByName(testName);
            assertEquals(1, authors.size());

            Author author = authors.get(0);
            assertEquals(testName, author.getName());
        }
    }

    @BeforeEach
    @AfterAll
    void prepareAuthors() {
        String queryDelete = "DELETE FROM author";
        String querySequence = "ALTER SEQUENCE author_author_id_seq RESTART WITH 1";
        List<Author> content = new ArrayList<>();
        content.add(new Author(null, "Владимир Ленин"));
        content.add(new Author(null, "Кир Булычёв"));
        content.add(new Author(null, "Лев Толстой"));
        content.add(new Author(null, "Джон Рональд Руэл Толкин"));
        content.add(new Author(null, "Борис Васильев"));
        content.add(new Author(null, "Крис Метцен"));
        content.add(new Author(null, "Роберт Брукс"));
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(queryDelete, Integer.class).executeUpdate();
            session.createNativeQuery(querySequence, Integer.class).executeUpdate();
            session.getTransaction().commit();
            authorDAO.saveCollection(content);
        }
    }

}
