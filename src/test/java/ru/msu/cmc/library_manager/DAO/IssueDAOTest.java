package ru.msu.cmc.library_manager.DAO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.model.Book;
import ru.msu.cmc.library_manager.model.Issue;
import ru.msu.cmc.library_manager.model.Reader;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class IssueDAOTest {
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private ReaderDAO readerDAO;
    @Autowired
    private IssueDAO issueDAO;

    @Test
    void testGetIssuesByFilter() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Book> bookList1 = new ArrayList<>();
        bookList1.add(bookDAO.getById(1));
        List <Book> bookList2 = new ArrayList<>(bookList1);
        bookList2.add(bookDAO.getById(2));
        List <Reader> readerList1 = new ArrayList<>();
        readerList1.add(readerDAO.getById(2));
        List <Reader> readerList2 = new ArrayList<>(readerList1);
        readerList2.add(readerDAO.getById(3));
        IssueDAO.Filter[] filters = {
                new IssueDAO.Filter(null, null, null, null,
                        null, null, null, null, null),
                new IssueDAO.Filter(bookList1, null, null, null,
                        null, null, null, null, null),
                new IssueDAO.Filter(bookList2, null, null, null,
                        null, null, null, null, null),
                new IssueDAO.Filter(null, readerList1, null, null,
                        null, null, null, null, null),
                new IssueDAO.Filter(null, readerList2, null, null,
                        null, null, null, null, null),
                new IssueDAO.Filter(null, null,
                        new Date(format.parse("2023-07-01").getTime()), null,
                        null, null, null, null, null),
                new IssueDAO.Filter(null, null,
                        null, new Date(format.parse("2023-07-01").getTime()),
                        null,null, null, null, null),
                new IssueDAO.Filter(null, null, null, null,
                        new Date(format.parse("2023-11-08").getTime()), null,
                        null, null, null),
                new IssueDAO.Filter(null, null, null, null,
                        null, new Date(format.parse("2023-11-08").getTime()),
                        null, null, null),
                new IssueDAO.Filter(null, null, null, null,
                        null, null,
                        new Date(format.parse("2024-03-01").getTime()), null, null),
                new IssueDAO.Filter(null, null, null, null,
                        null, null,
                        null, new Date(format.parse("2024-03-01").getTime()),null),
                new IssueDAO.Filter(null, null, null, null,
                        null, null, null, null, true),
                new IssueDAO.Filter(null, null, null, null,
                        null, null, null, null, false),
                new IssueDAO.Filter(bookList2, readerList2, null, null,
                        null, null, null, null, false),
        };
        int[] queryResultSizes = {13, 2, 3, 2, 3, 6, 7, 3, 6, 5, 3, 8, 5, 2};
        for (int i = 0; i < filters.length; i++) {
            IssueDAO.Filter filter = filters[i];
            List<Issue> issues = issueDAO.getIssuesByFilter(filter);
            assertEquals(queryResultSizes[i], issues.size());
            for (Issue issue : issues) {
                if (filter.getBooks() != null)
                    assertTrue(filter.getBooks().contains(issue.getBook()));
                if (filter.getReaders() != null)
                    assertTrue(filter.getReaders().contains(issue.getReader()));
                if (filter.getIssueDateBegin() != null)
                    assertTrue(issue.getIssued().after(filter.getIssueDateBegin()) ||
                            issue.getIssued().equals(filter.getIssueDateBegin()));
                if (filter.getIssueDateEnd() != null)
                    assertTrue(issue.getIssued().before(filter.getIssueDateEnd()) ||
                            issue.getIssued().equals(filter.getIssueDateEnd()));
                if (filter.getReturnDateBegin() != null)
                    assertTrue(issue.getReturned().after(filter.getReturnDateBegin()) ||
                            issue.getReturned().equals(filter.getReturnDateBegin()));
                if (filter.getReturnDateEnd() != null)
                    assertTrue(issue.getReturned().before(filter.getReturnDateEnd()) ||
                            issue.getReturned().equals(filter.getReturnDateEnd()));
                if (filter.getDeadlineBegin() != null)
                    assertTrue(issue.getDeadline().after(filter.getDeadlineBegin()) ||
                            issue.getDeadline().equals(filter.getDeadlineBegin()));
                if (filter.getDeadlineEnd() != null)
                    assertTrue(issue.getDeadline().before(filter.getDeadlineEnd()) ||
                            issue.getDeadline().equals(filter.getDeadlineEnd()));
            }
        }
    }
}
