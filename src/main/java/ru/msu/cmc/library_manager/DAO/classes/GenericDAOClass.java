package ru.msu.cmc.library_manager.DAO.classes;

import jakarta.persistence.criteria.CriteriaQuery;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.library_manager.DAO.GenericDAO;
import ru.msu.cmc.library_manager.model.GenericEntity;

import java.util.Collection;
import java.util.List;

@Repository
public abstract class GenericDAOClass<T extends GenericEntity> implements GenericDAO<T> {
    @Autowired
    protected SessionFactory sessionFactory;
    protected Class<T> entityClass;
    public GenericDAOClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    @Override
    public T getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(entityClass, id);
        }
    }

    @Override
    public List<T> getAll() {
        try(Session session = sessionFactory.openSession()) {
            CriteriaQuery<T> criteriaQuery = session.getCriteriaBuilder().createQuery(entityClass);
            criteriaQuery.from(entityClass);
            Query<T> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }

    @Override
    public void save(T entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        }
    }

    @Override
    public void saveCollection(@NonNull Collection<T> entities) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            for (T entity : entities)
                session.persist(entity);
            transaction.commit();
        }
    }

    @Override
    public void delete(T entity) {
        if (entity == null)
            return;
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        }
    }

    @Override
    public void update(@NonNull T entity) {
        if (entity.getId() != null && this.getById(entity.getId()) != null)
            try(Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                session.merge(entity);
                transaction.commit();
            }
    }
}
