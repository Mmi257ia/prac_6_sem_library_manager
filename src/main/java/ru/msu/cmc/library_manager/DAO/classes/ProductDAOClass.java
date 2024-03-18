package ru.msu.cmc.library_manager.DAO.classes;

import jakarta.persistence.criteria.*;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.library_manager.DAO.ProductDAO;
import ru.msu.cmc.library_manager.model.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public class ProductDAOClass extends GenericDAOClass<Product> implements ProductDAO {
    public ProductDAOClass() {
        super(Product.class);
    }

    @Override
    public List<Product> getProductsByFilter(@NonNull Filter filter) {
        try(Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Product> criteriaQuery = builder.createQuery(entityClass);
            Root<Product> root = criteriaQuery.from(entityClass);
            List<Predicate> predicateList = new ArrayList<Predicate>();

            if (filter.getIsbn() != null)
                predicateList.add(builder.equal(root.get("isbn"), filter.getIsbn()));
            if (filter.getName() != null)
                predicateList.add(builder.equal(root.get("name"), filter.getName()));
            if (filter.getPublisher() != null)
                predicateList.add(builder.equal(root.get("publisher"), filter.getPublisher()));
            if (filter.getYearOfPublishingBegin() != null)
                predicateList.add(builder.greaterThanOrEqualTo(root.get("yearOfPublishing"),
                        filter.getYearOfPublishingBegin()));
            if (filter.getYearOfPublishingEnd() != null)
                predicateList.add(builder.lessThanOrEqualTo(root.get("yearOfPublishing"),
                        filter.getYearOfPublishingEnd()));

            if (!predicateList.isEmpty()) {
                Predicate resultPredicate = predicateList.get(0);
                for (int i = 1; i < predicateList.size(); i++)
                    resultPredicate = builder.and(resultPredicate, predicateList.get(i));
                criteriaQuery = criteriaQuery.where(resultPredicate);
            }
            Query<Product> query = session.createQuery(criteriaQuery);
            List<Product> result = query.getResultList();
            if (filter.getAuthors() != null) {
                result.removeIf(p -> {
                        if (p.getAuthors() == null)
                            return true;
                        else
                            return !new HashSet<>(p.getAuthors()).containsAll(filter.getAuthors());
                });
            }
            return result;
        }
    }
}
