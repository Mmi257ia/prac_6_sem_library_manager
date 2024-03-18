package ru.msu.cmc.library_manager.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.msu.cmc.library_manager.model.Book;
import ru.msu.cmc.library_manager.model.Issue;
import ru.msu.cmc.library_manager.model.Reader;

import java.sql.Date;
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
    }
    List<Issue> getIssuesByFilter(Filter filter);
}
