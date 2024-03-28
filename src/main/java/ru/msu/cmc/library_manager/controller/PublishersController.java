package ru.msu.cmc.library_manager.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.library_manager.DAO.PublisherDAO;
import ru.msu.cmc.library_manager.model.Publisher;

@Controller
public class PublishersController {
    @Autowired
    private PublisherDAO publisherDAO;

    @GetMapping(value = "/publishers")
    public String getPublishers(@NonNull Model model) {
        model.addAttribute("publishersList", publisherDAO.getAll());
        return "publishers";
    }

    @PostMapping(value = "/publishers/add")
    public String postPublishersAdd(@RequestParam String name) {
        Publisher publisher = new Publisher(null, name);
        publisherDAO.save(publisher);

        return "redirect:../publishers";
    }

    @PostMapping(value = "/publishers/delete")
    public String postPublishersDelete(@RequestParam int id) {
        Publisher publisher = publisherDAO.getById(id);
        publisherDAO.delete(publisher);

        return "redirect:../publishers";
    }
}
