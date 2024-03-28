package ru.msu.cmc.library_manager.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.msu.cmc.library_manager.model.Book;
import ru.msu.cmc.library_manager.model.Issue;
import ru.msu.cmc.library_manager.model.Reader;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public interface IssueDAO extends GenericDAO<Issue> {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    class Filter {
        private List<Book> books; // must return all issues of any of these books
        private List<Reader> readers; // same
        private Date issueDateBegin;
        private Date issueDateEnd;
        private Date returnDateBegin;
        private Date returnDateEnd;
        private Date deadlineBegin;
        private Date deadlineEnd;
        private Boolean isReturned;

        public void addReader(Reader reader) {
            if (reader != null) {
                if (readers == null)
                    readers = new ArrayList<Reader>();
                readers.add(reader);
            }
        }

        public void addBook(Book book) {
            if (book != null) {
                if (books == null)
                    books = new ArrayList<Book>();
                books.add(book);
            }
        }
    }
    List<Issue> getIssuesByFilter(Filter filter);
}
