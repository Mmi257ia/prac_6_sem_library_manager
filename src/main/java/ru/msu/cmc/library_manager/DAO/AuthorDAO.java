package ru.msu.cmc.library_manager.DAO;

import ru.msu.cmc.library_manager.model.Author;

import java.util.List;

public interface AuthorDAO extends GenericDAO<Author> {
    List<Author> getAuthorByName(String name);
}
