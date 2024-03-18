package ru.msu.cmc.library_manager.DAO.classes;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.library_manager.DAO.AuthorDAO;
import ru.msu.cmc.library_manager.model.Author;

import java.util.List;

@Repository
public class AuthorDAOClass extends GenericDAOClass<Author> implements AuthorDAO {
    public AuthorDAOClass() {
        super(Author.class);
    }

    @Override
    public List<Author> getAuthorByName(String name) {
        try(Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Author> criteriaQuery = builder.createQuery(entityClass);
            Root<Author> root =  criteriaQuery.from(entityClass);

            criteriaQuery.where(builder.equal(root.get("name"), name));

            Query<Author> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }
}
