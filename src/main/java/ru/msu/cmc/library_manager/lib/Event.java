package ru.msu.cmc.library_manager.lib;

import lombok.*;
import ru.msu.cmc.library_manager.model.Book;
import ru.msu.cmc.library_manager.model.Issue;
import ru.msu.cmc.library_manager.model.Reader;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

// Helper class for History and Reader pages
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Event implements Comparable<Event> {
    private int issueId;
    private boolean issue; // true => issue, false => return
    private Integer bookId;
    private String bookName;
    private Integer readerId;
    private String readerName;
    private Date date;
    private Date deadline;
    private boolean isCurrentlyIssued;

    public static List<Event> allEventsFromIssues(List<Issue> issuesList) {
        if (issuesList == null)
            return null;
        List<Event> eventsList = new ArrayList<Event>();
        for (Issue issue : issuesList) {
            Book book = issue.getBook();
            Reader reader = issue.getReader();
            eventsList.add(new Event(issue.getId(), true, book.getId(), book.getProduct().getName(),
                    reader.getId(), reader.getName(), issue.getIssued(), issue.getDeadline(),
                    issue.getReturned() == null));
            if (issue.getReturned() != null)
                eventsList.add(new Event(issue.getId(), false, book.getId(), book.getProduct().getName(),
                        reader.getId(), reader.getName(), issue.getReturned(), issue.getDeadline(),
                        false));
        }
        return eventsList;
    }

    public static List<Event> eventsFromIssuesSeparated(List<Issue> issuesList, List<Issue> returnsList) {
        if (issuesList == null && returnsList == null)
            return null;
        List<Event> eventsList = new ArrayList<>();
        if (issuesList != null)
            for (Issue issue : issuesList) {
                Book book = issue.getBook();
                Reader reader = issue.getReader();
                eventsList.add(new Event(issue.getId(), true, book.getId(), book.getProduct().getName(),
                        reader.getId(), reader.getName(), issue.getIssued(), issue.getDeadline(),
                        issue.getReturned() == null));
            }
        if (returnsList != null)
            for (Issue issue : returnsList) {
                if (issue.getReturned() == null)
                    continue;
                Book book = issue.getBook();
                Reader reader = issue.getReader();
                eventsList.add(new Event(issue.getId(), false, book.getId(), book.getProduct().getName(),
                        reader.getId(), reader.getName(), issue.getReturned(), issue.getDeadline(),
                        false));
            }
        return eventsList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Event other))
            return false;
        boolean res = this.issueId == other.issueId &&
                this.issue == other.issue &&
                this.isCurrentlyIssued == other.isCurrentlyIssued;
        if (this.bookId == null)
            res = res && other.bookId == null;
        else
            res = res && this.bookId.equals(other.bookId);
        if (this.bookName == null)
            res = res && other.bookName == null;
        else
            res = res && this.bookName.equals(other.bookName);
        if (this.readerId == null)
            res = res && other.readerId == null;
        else
            res = res && this.readerId.equals(other.readerId);
        if (this.readerName == null)
            res = res && other.readerName == null;
        else
            res = res && this.readerName.equals(other.readerName);
        if (this.date == null)
            res = res && other.date == null;
        else
            res = res && this.date.equals(other.date);
        if (this.deadline == null)
            res = res && other.deadline == null;
        else
            res = res && this.deadline.equals(other.deadline);

        return res;
    }

    @Override
    public int compareTo(@NonNull Event o) {
        if (this.equals(o))
            return 0;
        int res = this.date.compareTo(o.getDate());
        if (res == 0)
            res = this.issueId - o.getIssueId();
        if (res == 0)
            res = issue ? -1 : 1;
        return res;
    }
}
