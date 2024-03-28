package ru.msu.cmc.library_manager.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.library_manager.DAO.BookDAO;
import ru.msu.cmc.library_manager.DAO.IssueDAO;
import ru.msu.cmc.library_manager.lib.BookAndStatus;
import ru.msu.cmc.library_manager.lib.Event;
import ru.msu.cmc.library_manager.model.Book;
import ru.msu.cmc.library_manager.model.Issue;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

@Controller
public class BookController {
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private IssueDAO issueDAO;

    @GetMapping(value = "/books/{id}")
    public String getBook(@PathVariable int id, @NonNull Model model) {
        Book book = bookDAO.getById(id);
        model.addAttribute("book", book);

        IssueDAO.Filter issueFilter = new IssueDAO.Filter();
        issueFilter.addBook(book);
        List<Event> eventsList = Event.eventsFromIssues(issueDAO.getIssuesByFilter(issueFilter));
        eventsList.sort(Comparator.reverseOrder());
        model.addAttribute("eventsList", eventsList);
        BookAndStatus bookAndStatus = new BookAndStatus(book, issueDAO);
        if (bookAndStatus.isIssued())
            model.addAttribute("issued", true);
        else
            model.addAttribute("issued", false);

        return "book";
    }

    @PostMapping(value = "/books/{id}/edit")
    public String postBookEdit(@PathVariable int id,
                               @RequestParam String receivingDate) {
        Book book = bookDAO.getById(id);
        Date dateSql = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            dateSql = new Date(format.parse(receivingDate).getTime());
        } catch (Exception ignored) {}
        book.setReceivingDate(dateSql);
        bookDAO.update(book);

        return "redirect:../../books/{id}";
    }

    @PostMapping(value = "/books/{id}/extend")
    public String postBookExtend(@PathVariable int id,
                                 @RequestParam int issueId,
                                 @RequestParam(required = false) String date) {
        Date dateSql = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            dateSql = new Date(format.parse(date).getTime());
        } catch (Exception ignored) {}

        Issue issue = issueDAO.getById(issueId);
        issue.setDeadline(dateSql);
        issueDAO.update(issue);

        return "redirect:../../books/{id}";
    }
}
