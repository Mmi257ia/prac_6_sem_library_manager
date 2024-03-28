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

    public static List<Event> eventsFromIssues(List<Issue> issuesList) {
        if (issuesList == null)
            return null;
        List<Event> eventsList = new ArrayList<Event>();
        for (Issue issue : issuesList) {
            Book book = issue.getBook();
            Reader reader = issue.getReader();
            eventsList.add(new Event(issue.getId(), true, book.getId(), book.getProduct().getName(),
                    reader.getId(), reader.getName(), issue.getIssued(), issue.getDeadline()));
            if (issue.getReturned() != null)
                eventsList.add(new Event(issue.getId(), false, book.getId(), book.getProduct().getName(),
                        reader.getId(), reader.getName(), issue.getReturned(), issue.getDeadline()));
        }
        return eventsList;
    }

    @Override
    public int compareTo(Event o) {
        if (o == null)
            return 1;
        return this.date.compareTo(o.getDate());
    }
}
