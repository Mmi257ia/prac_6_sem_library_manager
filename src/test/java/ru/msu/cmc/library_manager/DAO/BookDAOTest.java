package ru.msu.cmc.library_manager.DAO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.model.Book;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class BookDAOTest {
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private BookDAO bookDAO;

    @Test
    void testGetBooksByFilter() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        BookDAO.Filter[] filters = {
                new BookDAO.Filter(null, null, null),
                new BookDAO.Filter(productDAO.getById(2), null, null),
                new BookDAO.Filter(null, new Date(format.parse("2020-01-01").getTime()), null),
                new BookDAO.Filter(null, null, new Date(format.parse("2020-01-01").getTime())),
                new BookDAO.Filter(productDAO.getById(2), null, new Date(format.parse("2000-01-01").getTime())),
                new BookDAO.Filter(productDAO.getById(2), null, new Date(format.parse("1971-01-01").getTime())),
        };
        int[] queryResultSizes = {21, 7, 4, 17, 2, 0};
        for (int i = 0; i < filters.length; i++) {
            BookDAO.Filter filter = filters[i];
            List<Book> books = bookDAO.getBooksByFilter(filter);
            assertEquals(queryResultSizes[i], books.size());
            for (Book book : books) {
                if (filter.getProduct() != null)
                    assertEquals(filter.getProduct(), book.getProduct());
                if (filter.getReceivingDateBegin() != null)
                    assertTrue(book.getReceivingDate().after(filter.getReceivingDateBegin()) ||
                            book.getReceivingDate().equals(filter.getReceivingDateBegin()));
                if (filter.getReceivingDateEnd() != null)
                    assertTrue(book.getReceivingDate().before(filter.getReceivingDateEnd()) ||
                            book.getReceivingDate().equals(filter.getReceivingDateBegin()));
            }
        }
    }
}
