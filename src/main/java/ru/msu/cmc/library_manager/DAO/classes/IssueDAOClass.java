package ru.msu.cmc.library_manager.DAO.classes;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.library_manager.DAO.IssueDAO;
import ru.msu.cmc.library_manager.model.*;

import java.util.ArrayList;
import java.util.List;

@Repository
public class IssueDAOClass extends GenericDAOClass<Issue> implements IssueDAO {
    public IssueDAOClass() {
        super(Issue.class);
    }

    @Override
    public List<Issue> getIssuesByFilter(@NonNull Filter filter) {
        try(Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Issue> criteriaQuery = builder.createQuery(entityClass);
            Root<Issue> root = criteriaQuery.from(entityClass);
            List<Predicate> predicateList = new ArrayList<Predicate>();
            if (filter.getBooks() != null)
                if (!filter.getBooks().isEmpty()) {
                    Predicate booksPredicate;
                    booksPredicate = builder.equal(root.get("book"), filter.getBooks().get(0));
                    for (Book book : filter.getBooks())
                        booksPredicate = builder.or(booksPredicate, builder.equal(root.get("book"), book));
                    predicateList.add(booksPredicate);
                }
            if (filter.getReaders() != null)
                if (!filter.getReaders().isEmpty()) {
                    Predicate readersPredicate;
                    readersPredicate = builder.equal(root.get("reader"), filter.getReaders().get(0));
                    for (Reader reader : filter.getReaders())
                        readersPredicate = builder.or(readersPredicate, builder.equal(root.get("reader"), reader));
                    predicateList.add(readersPredicate);
                }
            if (filter.getIssueDateBegin() != null)
                predicateList.add(builder.greaterThanOrEqualTo(root.get("issued"),
                        filter.getIssueDateBegin()));
            if (filter.getIssueDateEnd() != null)
                predicateList.add(builder.lessThanOrEqualTo(root.get("issued"),
                        filter.getIssueDateEnd()));
            if (filter.getReturnDateBegin() != null)
                predicateList.add(builder.greaterThanOrEqualTo(root.get("returned"),
                        filter.getReturnDateBegin()));
            if (filter.getReturnDateEnd() != null)
                predicateList.add(builder.lessThanOrEqualTo(root.get("returned"),
                        filter.getReturnDateEnd()));
            if (filter.getDeadlineBegin() != null)
                predicateList.add(builder.greaterThanOrEqualTo(root.get("deadline"),
                        filter.getDeadlineBegin()));
            if (filter.getDeadlineEnd() != null)
                predicateList.add(builder.lessThanOrEqualTo(root.get("deadline"),
                        filter.getDeadlineEnd()));
            if (filter.getIsReturned() != null) {
                if (filter.getIsReturned())
                    predicateList.add(builder.isNotNull(root.get("returned")));
                else
                    predicateList.add(builder.isNull(root.get("returned")));
            }


            if (!predicateList.isEmpty()) {
                Predicate resultPredicate = predicateList.get(0);
                for (int i = 1; i < predicateList.size(); i++)
                    resultPredicate = builder.and(resultPredicate, predicateList.get(i));
                criteriaQuery = criteriaQuery.where(resultPredicate);
            }
            Query<Issue> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }
}
