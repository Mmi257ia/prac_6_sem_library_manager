package ru.msu.cmc.library_manager.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.msu.cmc.library_manager.model.Reader;

import java.util.List;

public interface ReaderDAO extends GenericDAO<Reader> {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    class Filter {
        private String name;
        private String address;
        private Long phone;
    }
    List<Reader> getReadersByFilter(Filter filter);
}
