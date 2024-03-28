package ru.msu.cmc.library_manager.controller;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.library_manager.DAO.AuthorDAO;
import ru.msu.cmc.library_manager.DAO.BookDAO;
import ru.msu.cmc.library_manager.DAO.IssueDAO;
import ru.msu.cmc.library_manager.DAO.ProductDAO;
import ru.msu.cmc.library_manager.lib.BookAndStatus;
import ru.msu.cmc.library_manager.model.Author;
import ru.msu.cmc.library_manager.model.Book;
import ru.msu.cmc.library_manager.model.Product;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private AuthorDAO authorDAO;
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private IssueDAO issueDAO;

    @GetMapping(value = "/products/{id}")
    public String getProduct(@PathVariable int id, @NonNull Model model) {
        Product product = productDAO.getById(id);
        model.addAttribute("product", product);

        List<Author> authorsList = new ArrayList<Author>();
        for (Integer authorId : product.getAuthors())
            if (authorId != null)
                authorsList.add(authorDAO.getById(authorId));
        model.addAttribute("authorsList", authorsList);

        BookDAO.Filter filter = new BookDAO.Filter(product, null, null);
        List<Book> booksNoStatusList = bookDAO.getBooksByFilter(filter);
        List<BookAndStatus> booksList = new ArrayList<BookAndStatus>();
        for (Book book : booksNoStatusList)
            booksList.add(new BookAndStatus(book, issueDAO));
        booksList.sort((a, b) -> { // available -> earliest deadline issued -> no-deadline issued
            if (!a.isIssued() && b.isIssued())
                return -1;
            else if (a.isIssued() && !b.isIssued())
                return 1;
            else if (a.isIssued() && b.isIssued()) {
                if (a.getDeadline() == null)
                    return 1;
                else if (b.getDeadline() == null)
                    return -1;
                else
                    return a.getDeadline().compareTo(b.getDeadline());
            }
            else
                return 0;
        });
        model.addAttribute("booksList", booksList);

        return "product";
    }

    @PostMapping(value = "/products/{id}/add")
    public String postProductAdd(@PathVariable int id,
                                 @RequestParam(required = false) String receivingDate) {
        Date dateSql = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            dateSql = new Date(format.parse(receivingDate).getTime());
        } catch (Exception ignored) {}

        Book book = new Book(null, productDAO.getById(id), dateSql);
        bookDAO.save(book);

        return "redirect:../../products/{id}";
    }

    @PostMapping(value = "/products/{id}/delete")
    public String postProductDelete(@PathVariable int id,
                                    @RequestParam int bookId) {
        Book book = bookDAO.getById(bookId);
        bookDAO.delete(book);

        return "redirect:../../products/{id}";
    }
}
