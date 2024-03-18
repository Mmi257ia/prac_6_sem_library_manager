package ru.msu.cmc.library_manager.DAO;

import ru.msu.cmc.library_manager.model.GenericEntity;

import java.util.Collection;
import java.util.List;

public interface GenericDAO<T extends GenericEntity> {
    T getById(int id);
    List<T> getAll();
    void save(T entity);
    void saveCollection(Collection<T> entities);
    void delete(T entity);
    void update(T entity);
}
