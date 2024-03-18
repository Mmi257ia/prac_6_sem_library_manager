package ru.msu.cmc.library_manager.DAO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.cmc.library_manager.model.Reader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ReaderDAOTest {
    @Autowired
    private ReaderDAO readerDAO;

    @Test
    void testGetReadersByFilter() {
        ReaderDAO.Filter[] filters = {
                new ReaderDAO.Filter(null, null, null),
                new ReaderDAO.Filter("Ивлев Никита Александрович", null, null),
                new ReaderDAO.Filter(null, "Самарская обл., с. Солнечная Поляна, СНТ \"Газовик\", уч. 13", null),
                new ReaderDAO.Filter(null, null, 84956610394L),
                new ReaderDAO.Filter("Иновенков Игорь Николаевич", null, 89284567891L),
                new ReaderDAO.Filter("Пётр", null, null),
        };
        int[] queryResultSizes = {6, 1, 1, 1, 0, 0};
        for (int i = 0; i < filters.length; i++) {
            ReaderDAO.Filter filter = filters[i];
            List<Reader> readers = readerDAO.getReadersByFilter(filter);
            assertEquals(queryResultSizes[i], readers.size());
            for (Reader reader : readers) {
                if (filter.getName() != null)
                    assertEquals(filter.getName(), reader.getName());
                if (filter.getPhone() != null)
                    assertEquals(filter.getPhone(), reader.getPhone());
                if (filter.getAddress() != null)
                    assertEquals(filter.getAddress(), reader.getAddress());
            }
        }
    }
}
