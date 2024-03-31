package ru.msu.cmc.library_manager.lib;

import lombok.*;
import ru.msu.cmc.library_manager.DAO.AuthorDAO;
import ru.msu.cmc.library_manager.DAO.BookDAO;
import ru.msu.cmc.library_manager.DAO.IssueDAO;
import ru.msu.cmc.library_manager.model.Author;
import ru.msu.cmc.library_manager.model.Book;
import ru.msu.cmc.library_manager.model.Issue;
import ru.msu.cmc.library_manager.model.Product;

import java.util.ArrayList;
import java.util.List;

// Helper class to store books count and authors is objects
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ProductWithCountAndAuthors {
    Product product;
    int count; // in library
    int total; // in db
    List<Author> authors;
    String authorsString;
    public ProductWithCountAndAuthors(Product product,
                                      @NonNull AuthorDAO authorDAO,
                                      @NonNull BookDAO bookDAO,
                                      @NonNull IssueDAO issueDAO) {
        this.product = product;
        int resCount = 0;
        List<Book> books = bookDAO.getBooksByFilter(new BookDAO.Filter(product, null, null));
        total = books.size();
        for (Book book : books) {
            List<Book> bookInList = new ArrayList<Book>();
            bookInList.add(book);
            IssueDAO.Filter filter = new IssueDAO.Filter();
            filter.setBooks(bookInList);
            filter.setIsReturned(false);
            List<Issue> issues = issueDAO.getIssuesByFilter(filter);
            if (issues.isEmpty())
                resCount++;
        }
        count = resCount;
        authors = new ArrayList<Author>();
        for (Integer authorId : product.getAuthors())
            if (authorId != null)
                authors.add(authorDAO.getById(authorId));

        StringBuilder stringBuilder = new StringBuilder();
        for (Author author : authors) {
            if (!stringBuilder.isEmpty())
                stringBuilder.append(", ");
            stringBuilder.append(author.getName());
        }
        authorsString = stringBuilder.toString();
    }
}