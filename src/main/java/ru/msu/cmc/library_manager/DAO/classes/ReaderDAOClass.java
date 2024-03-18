package ru.msu.cmc.library_manager.DAO.classes;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.library_manager.DAO.ReaderDAO;
import ru.msu.cmc.library_manager.model.Reader;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ReaderDAOClass extends GenericDAOClass<Reader> implements ReaderDAO {
    public ReaderDAOClass() {
        super(Reader.class);
    }

    @Override
    public List<Reader> getReadersByFilter(@NonNull Filter filter) {
        try(Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Reader> criteriaQuery = builder.createQuery(entityClass);
            Root<Reader> root = criteriaQuery.from(entityClass);
            List<Predicate> predicateList = new ArrayList<Predicate>();

            if (filter.getName() != null)
                predicateList.add(builder.equal(root.get("name"), filter.getName()));
            if (filter.getPhone() != null)
                predicateList.add(builder.equal(root.get("phone"), filter.getPhone()));
            if (filter.getAddress() != null)
                predicateList.add(builder.equal(root.get("address"), filter.getAddress()));

            if (!predicateList.isEmpty()) {
                Predicate resultPredicate = predicateList.get(0);
                for (int i = 1; i < predicateList.size(); i++)
                    resultPredicate = builder.and(resultPredicate, predicateList.get(i));
                criteriaQuery = criteriaQuery.where(resultPredicate);
            }
            Query<Reader> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }
}
