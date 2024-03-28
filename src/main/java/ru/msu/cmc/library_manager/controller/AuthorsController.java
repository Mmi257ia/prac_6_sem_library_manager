package ru.msu.cmc.library_manager.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.library_manager.DAO.AuthorDAO;
import ru.msu.cmc.library_manager.model.Author;

@Controller
public class AuthorsController {
    @Autowired
    private AuthorDAO authorDAO;

    @GetMapping(value = "/authors")
    public String getAuthors(@NonNull Model model) {
        model.addAttribute("authorsList", authorDAO.getAll());
        return "authors";
    }

    @PostMapping(value = "/authors/add")
    public String postAuthorsAdd(@RequestParam String name) {
        Author author = new Author(null, name);
        authorDAO.save(author);

        return "redirect:../authors";
    }

    @PostMapping(value = "/authors/delete")
    public String postAuthorsDelete(@RequestParam int id) {
        Author author = authorDAO.getById(id);
        authorDAO.delete(author);

        return "redirect:../authors";
    }
}
