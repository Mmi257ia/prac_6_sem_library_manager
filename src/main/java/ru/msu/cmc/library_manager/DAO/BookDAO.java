package ru.msu.cmc.library_manager.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.msu.cmc.library_manager.model.Book;
import ru.msu.cmc.library_manager.model.Product;

import java.sql.Date;
import java.util.List;

public interface BookDAO extends GenericDAO<Book> {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    class Filter {
        private Product product;
        private Date receivingDateBegin;
        private Date receivingDateEnd;
    }
    List<Book> getBooksByFilter(Filter filter);
}
