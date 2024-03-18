package ru.msu.cmc.library_manager.DAO;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.model.Author;
import ru.msu.cmc.library_manager.model.Product;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ProductDAOTest {
    @Autowired
    private AuthorDAO authorDAO;
    @Autowired
    private PublisherDAO publisherDAO;
    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGetProductsByFilter() {
        List<Author> authorList1 = new ArrayList<Author>(authorDAO.getAuthorByName("Крис Метцен"));
        authorList1.add(authorDAO.getAuthorByName("Роберт Брукс").get(0));
        List<Integer> authorList2 = new ArrayList<Integer>();
        authorList2.add(3);
        ProductDAO.Filter[] filters = {
                new ProductDAO.Filter(null,
                        null, null, null,
                        null, null),
                new ProductDAO.Filter(null,
                        null, null, publisherDAO.getPublisherByName("АСТ").get(0),
                        null, null),
                ProductDAO.Filter.getFilterByAuthors(null,
                        null, authorDAO.getAuthorByName("Крис Метцен"), null,
                        null, null),
                ProductDAO.Filter.getFilterByAuthors(null,
                        null, authorList1, null,
                        null, null),
                ProductDAO.Filter.getFilterByAuthors(null,
                        null, authorDAO.getAuthorByName("Кир Булычёв"), null,
                        null, null),
                new ProductDAO.Filter(null,
                        null, authorList2, null,
                        null, null),
                new ProductDAO.Filter("9785382019963",
                        null, null, null,
                        null, null),
                new ProductDAO.Filter(null,
                        "Хоббит", null, null,
                        null, null),
                new ProductDAO.Filter(null,
                        null, null, null,
                        2021, null),
                new ProductDAO.Filter(null,
                        null, null, null,
                        null, 2021),
                new ProductDAO.Filter(null,
                        null, null, null,
                        2020, 2022),
                new ProductDAO.Filter("9785382019963",
                        "Путешествие Алисы", null, null,
                        null, null),
                new ProductDAO.Filter(null,
                        null, null, publisherDAO.getPublisherByName("АСТ").get(0),
                        2020, null),
        };
        int[] queryResultSizes = {11, 3, 1, 1, 3, 2, 1, 1, 7, 2, 4, 0, 2};
        for (int i = 0; i < filters.length; i++) {
            ProductDAO.Filter filter = filters[i];
            List<Product> products = productDAO.getProductsByFilter(filter);
            assertEquals(queryResultSizes[i], products.size());
            for (Product product : products) {
                if (filter.getIsbn() != null)
                    assertEquals(filter.getIsbn(), product.getIsbn());
                if (filter.getName() != null)
                    assertEquals(filter.getName(), product.getName());
                if (filter.getAuthors() != null)
                    assertTrue(product.getAuthors().containsAll(filter.getAuthors()));
                if (filter.getPublisher() != null)
                    assertEquals(filter.getPublisher(), product.getPublisher());
                if (filter.getYearOfPublishingBegin() != null)
                    assertTrue(product.getYearOfPublishing() >= filter.getYearOfPublishingBegin());
                if (filter.getYearOfPublishingEnd() != null)
                    assertTrue(product.getYearOfPublishing() <= filter.getYearOfPublishingEnd());
            }
        }
    }

    @BeforeEach
    void addEntityWithNulls() {
        String name = "A name";
        Product nulls = new Product(null, null, name, null, null, null);
        productDAO.save(nulls);
    }

    @AfterEach
    void deleteEntityWithNulls() {
        String name = "A name";
        List<Product> nullsList = productDAO.getProductsByFilter(new ProductDAO.Filter(null, name, null,
                null, null, null));
        productDAO.delete(nullsList.get(0));
    }
}
