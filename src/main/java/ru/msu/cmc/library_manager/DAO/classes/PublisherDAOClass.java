package ru.msu.cmc.library_manager.DAO.classes;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.library_manager.DAO.PublisherDAO;
import ru.msu.cmc.library_manager.model.Publisher;

import java.util.List;

@Repository
public class PublisherDAOClass extends GenericDAOClass<Publisher> implements PublisherDAO {
    public PublisherDAOClass() {
        super(Publisher.class);
    }

    @Override
    public List<Publisher> getPublisherByName(String name) {
        try(Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Publisher> criteriaQuery = builder.createQuery(entityClass);
            Root<Publisher> root =  criteriaQuery.from(entityClass);

            criteriaQuery.where(builder.equal(root.get("name"), name));

            Query<Publisher> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }
}
