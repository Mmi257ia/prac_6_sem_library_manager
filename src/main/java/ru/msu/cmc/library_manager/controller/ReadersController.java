package ru.msu.cmc.library_manager.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.library_manager.DAO.ReaderDAO;
import ru.msu.cmc.library_manager.model.Reader;

import java.util.Comparator;
import java.util.List;

@Controller
public class ReadersController {
    @Autowired
    private ReaderDAO readerDAO;

    @GetMapping(value = "/readers")
    public String getReaders(@RequestParam(required = false) Integer id,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String address,
                             @RequestParam(required = false) Long phone,
                             @NonNull Model model) {
        String nameToFilterBy = name == null ? null : (name.isEmpty() ? null : name);
        String addressToFilterBy = address == null ? null : (address.isEmpty() ? null : address);
        ReaderDAO.Filter filter = new ReaderDAO.Filter(nameToFilterBy, addressToFilterBy, phone);
        List<Reader> readersList = readerDAO.getReadersByFilter(filter);
        // if readerId is passed, we can return this id's bearer or empty list if they don't fit other constraints
        if (id != null)
            readersList.removeIf(reader -> !reader.getId().equals(id));

        readersList.sort(Comparator.comparingInt(Reader::getId));
        model.addAttribute("readersList", readersList);

        model.addAttribute("id", id);
        model.addAttribute("name", nameToFilterBy);
        model.addAttribute("address", addressToFilterBy);
        model.addAttribute("phone", phone);
        return "readers";
    }

    @PostMapping(value = "/readers/add")
    public String postReadersAdd(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) String address,
                                 @RequestParam(required = false) Long phone) {
        Reader reader = new Reader(null, name, address, phone);
        readerDAO.save(reader);

        return "redirect:../readers";
    }

    @PostMapping(value = "/readers/delete")
    public String postReadersDelete(@RequestParam int id) {
        Reader reader = readerDAO.getById(id);
        readerDAO.delete(reader);

        return "redirect:../readers";
    }
}
