package ru.msu.cmc.library_manager.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.library_manager.DAO.BookDAO;
import ru.msu.cmc.library_manager.DAO.IssueDAO;
import ru.msu.cmc.library_manager.DAO.ReaderDAO;
import ru.msu.cmc.library_manager.lib.BookAndStatus;
import ru.msu.cmc.library_manager.model.Book;
import ru.msu.cmc.library_manager.model.Issue;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IssueController {
    @Autowired
    private ReaderDAO readerDAO;
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private IssueDAO issueDAO;

    @GetMapping(value = "/issue")
    public String getIssue(@RequestParam(required = false) Integer readerId,
                           @RequestParam(required = false) Integer bookId,
                           @NonNull Model model) {
        if (readerId != null)
            model.addAttribute("reader", readerDAO.getById(readerId));
        if (bookId != null)
            model.addAttribute("book", bookDAO.getById(bookId));
        model.addAttribute("readerId", readerId);
        model.addAttribute("bookId", bookId);
        return "issue";
    }

    @PostMapping(value = "/issue")
    public String postIssue(@RequestParam int readerId,
                            @RequestParam int bookId,
                            @RequestParam(required = false) String deadline) {
        Date dateSql = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            dateSql = new Date(format.parse(deadline).getTime());
        } catch (Exception ignored) {}

        Book book = bookDAO.getById(bookId);
        BookAndStatus bookAndStatus = new BookAndStatus(book, issueDAO);
        if (bookAndStatus.isIssued())
            return "redirect:../error";
        Issue issue = new Issue(null, book, readerDAO.getById(readerId),
                new Date(new java.util.Date().getTime()), null, dateSql);
        issueDAO.save(issue);

        return "redirect:/index";
    }

    @PostMapping(value = "/return")
    public String postReturn(@RequestParam int bookId) {
        Book book = bookDAO.getById(bookId);
        List<Book> bookList = new ArrayList<Book>();
        bookList.add(book);
        List<Issue> issueList = issueDAO.getIssuesByFilter(new IssueDAO.Filter(
                bookList, null, null, null,
                null, null, null, null, false));
        if (issueList.size() != 1)
            return "redirect:../error";
        Issue issue = issueList.get(0);

        issue.setReturned(new Date(new java.util.Date().getTime()));
        issueDAO.update(issue);

        return "redirect:/index";
    }
}
