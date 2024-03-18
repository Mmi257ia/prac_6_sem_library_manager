package ru.msu.cmc.library_manager.DAO;

import ru.msu.cmc.library_manager.model.Publisher;

import java.util.List;

public interface PublisherDAO extends GenericDAO<Publisher>{
    List<Publisher> getPublisherByName(String name);
}
