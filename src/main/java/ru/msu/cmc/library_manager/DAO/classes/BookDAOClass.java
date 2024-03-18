package ru.msu.cmc.library_manager.DAO.classes;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.library_manager.DAO.BookDAO;
import ru.msu.cmc.library_manager.model.Book;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookDAOClass extends GenericDAOClass<Book> implements BookDAO {
    public BookDAOClass() {
        super(Book.class);
    }

    @Override
    public List<Book> getBooksByFilter(@NonNull Filter filter) {
        try(Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Book> criteriaQuery = builder.createQuery(entityClass);
            Root<Book> root = criteriaQuery.from(entityClass);
            List<Predicate> predicateList = new ArrayList<Predicate>();

            if (filter.getProduct() != null)
                predicateList.add(builder.equal(root.get("product"), filter.getProduct()));
            if (filter.getReceivingDateBegin() != null)
                predicateList.add(builder.greaterThanOrEqualTo(root.get("receivingDate"),
                        filter.getReceivingDateBegin()));
            if (filter.getReceivingDateEnd() != null)
                predicateList.add(builder.lessThanOrEqualTo(root.get("receivingDate"),
                        filter.getReceivingDateEnd()));

            if (!predicateList.isEmpty()) {
                Predicate resultPredicate = predicateList.get(0);
                for (int i = 1; i < predicateList.size(); i++)
                    resultPredicate = builder.and(resultPredicate, predicateList.get(i));
                criteriaQuery = criteriaQuery.where(resultPredicate);
            }
            Query<Book> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }
}
