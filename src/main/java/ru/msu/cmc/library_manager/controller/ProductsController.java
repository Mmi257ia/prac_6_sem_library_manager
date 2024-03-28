package ru.msu.cmc.library_manager.controller;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.library_manager.DAO.*;
import ru.msu.cmc.library_manager.model.*;

import java.util.*;

@Controller
public class ProductsController {
    @Autowired
    private AuthorDAO authorDAO;
    @Autowired
    private PublisherDAO publisherDAO;
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private IssueDAO issueDAO;

    // Helper class to store books count and authors is objects
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    class ProductWithCountAndAuthors {
        Product product;
        int count;
        List<Author> authors;
        public ProductWithCountAndAuthors(Product product) {
            this.product = product;
            int res = 0;
            List<Book> books = bookDAO.getBooksByFilter(new BookDAO.Filter(product, null, null));
            for (Book book : books) {
                List<Book> bookInList = new ArrayList<Book>();
                bookInList.add(book);
                IssueDAO.Filter filter = new IssueDAO.Filter();
                filter.setBooks(bookInList);
                filter.setIsReturned(false);
                List<Issue> issues = issueDAO.getIssuesByFilter(filter);
                if (issues.isEmpty())
                    res++;
            }
            count = res;
            authors = new ArrayList<Author>();
            for (Integer authorId : product.getAuthors())
                if (authorId != null)
                    authors.add(authorDAO.getById(authorId));
        }
    }

    private List<Author> getAuthorsList(String authors) throws IllegalArgumentException {
        if (authors == null || authors.isEmpty())
            return null;
        String[] authorNames = authors.split(",");
        List<Author> authorsList = new ArrayList<Author>();
        for (String authorName : authorNames) {
            List<Author> queryResult = authorDAO.getAuthorByName(authorName);
            if (!queryResult.isEmpty())
                authorsList.add(queryResult.get(0));
            else
                throw new IllegalArgumentException(authorName); // if a wrong author name occured, throw
        }
        return authorsList;
    }

    private List<Integer> getAuthorIdsList(String authors) throws IllegalArgumentException {
        if (authors == null || authors.isEmpty())
            return null;
        String[] authorNames = authors.split(",");
        List<Integer> authorIdsList = new ArrayList<Integer>();
        for (String authorName : authorNames) {
            List<Author> queryResult = authorDAO.getAuthorByName(authorName);
            if (!queryResult.isEmpty())
                authorIdsList.add(queryResult.get(0).getId());
            else
                throw new IllegalArgumentException(authorName); // if a wrong author name occured, throw
        }
        return authorIdsList;
    }

    @GetMapping(value = "/products")
    public String getProducts(@RequestParam(required = false) String isbn,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String authors, // separated by commas
                              @RequestParam(required = false) String publisher,
                              @RequestParam(required = false) Integer yearOfPublishingBegin,
                              @RequestParam(required = false) Integer yearOfPublishingEnd,
                              @RequestParam(required = false) Integer present, // 1 => true, 0 => false
                              @NonNull Model model) {
        String isbnToFilterBy = isbn == null ? null : (isbn.isEmpty() ? null : isbn);
        String nameToFilterBy = name == null ? null : (name.isEmpty() ? null : name);
        List <Author> authorList;
        try {
            authorList = getAuthorsList(authors);
        } catch (IllegalArgumentException e) {
            // send empty list
            model.addAttribute("productsList", new ArrayList<ProductWithCountAndAuthors>());
            return "products";
        }
        List<Publisher> publishersList = publisherDAO.getPublisherByName(publisher);
        Publisher publisherObject = null;
        if (!publishersList.isEmpty())
            publisherObject = publishersList.get(0);

        ProductDAO.Filter filter = ProductDAO.Filter.getFilterByAuthors(isbnToFilterBy, nameToFilterBy, authorList,
                publisherObject, yearOfPublishingBegin, yearOfPublishingEnd);
        List<Product> productsListNoCount = productDAO.getProductsByFilter(filter);
        List<ProductWithCountAndAuthors> productsList = new ArrayList<ProductWithCountAndAuthors>();
        for (Product product : productsListNoCount) {
            ProductWithCountAndAuthors pwcaa = new ProductWithCountAndAuthors(product);
            if (present != null) {
                if (present == 0 && pwcaa.getCount() == 0 || present == 1 && pwcaa.getCount() > 0)
                    productsList.add(pwcaa);
            } else
                productsList.add(pwcaa);
        }
        model.addAttribute("productsList", productsList);

        model.addAttribute("isbn", isbn);
        model.addAttribute("name", name);
        model.addAttribute("authors", authors);
        model.addAttribute("publisher", publisher);
        model.addAttribute("yearOfPublishingBegin", yearOfPublishingBegin);
        model.addAttribute("yearOfPublishingEnd", yearOfPublishingEnd);
        model.addAttribute("present", present);
        return "products";
    }

    @PostMapping(value = "/products/add")
    public String postProductsAdd(@RequestParam(required = false) String isbn,
                                  @RequestParam String name,
                                  @RequestParam(required = false) String authors,
                                  @RequestParam(required = false) String publisher,
                                  @RequestParam(required = false) Integer yearOfPublishing) {
        Publisher publisherObject = null;
        if (publisher != null) {
            List<Publisher> publisherList = publisherDAO.getPublisherByName(publisher);
            if (!publisherList.isEmpty())
                publisherObject = publisherList.get(0);
        }

        List<Integer> authorsList = getAuthorIdsList(authors);
        Product product = new Product(null, isbn, name, authorsList, publisherObject, yearOfPublishing);
        productDAO.save(product);

        return "redirect:../products";
    }

    @PostMapping(value = "/products/delete")
    public String postProductsDelete(@RequestParam int id) {
        Product product = productDAO.getById(id);
        productDAO.delete(product);

        return "redirect:../products";
    }
}
