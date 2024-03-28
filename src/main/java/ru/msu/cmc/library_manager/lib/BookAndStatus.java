package ru.msu.cmc.library_manager.lib;

import lombok.*;
import ru.msu.cmc.library_manager.DAO.IssueDAO;
import ru.msu.cmc.library_manager.model.Book;
import ru.msu.cmc.library_manager.model.Issue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

// Helper class for storing book and its status
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookAndStatus {
    private Book book;
    private boolean issued; // true => issued and not returned, false => available
    private Date deadline;

    public BookAndStatus(Book book, IssueDAO issueDAO) {
        this.book = book;
        List<Book> bookInList = new ArrayList<Book>();
        bookInList.add(book);
        IssueDAO.Filter filter = new IssueDAO.Filter();
        filter.setBooks(bookInList);
        filter.setIsReturned(false);
        List<Issue> issues = issueDAO.getIssuesByFilter(filter);
        if (issues.isEmpty())
            issued = false;
        else {
            issued = true;
            deadline = issues.get(0).getDeadline();
        }
    }
}
