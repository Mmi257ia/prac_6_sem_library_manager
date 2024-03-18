package ru.msu.cmc.library_manager.DAO;

import lombok.*;
import ru.msu.cmc.library_manager.model.Author;
import ru.msu.cmc.library_manager.model.Product;
import ru.msu.cmc.library_manager.model.Publisher;

import java.util.ArrayList;
import java.util.List;

public interface ProductDAO extends GenericDAO<Product> {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    class Filter {
        private String isbn;
        private String name;
        private List<Integer> authors; // these authors should be a subset of entity's authors
        private Publisher publisher;
        private Integer yearOfPublishingBegin;
        private Integer yearOfPublishingEnd;

        public static Filter getFilterByAuthors(String isbn, String name, @NonNull List<Author> authors, Publisher publisher,
                                                Integer yearOfPublishingBegin, Integer yearOfPublishingEnd) {
            List<Integer> list = new ArrayList<Integer>();
            for (Author author : authors)
                list.add(author.getId());
            return new Filter(isbn, name, list, publisher, yearOfPublishingBegin, yearOfPublishingEnd);
        }
    }
    List<Product> getProductsByFilter(Filter filter);
}
