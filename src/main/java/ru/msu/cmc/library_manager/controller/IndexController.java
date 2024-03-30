package ru.msu.cmc.library_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {
    @GetMapping(value = {"/index", "/"})
    public String getIndex() {
        return "index";
    }

    @GetMapping(value = "/styles/{file}.css")
    public String getStyles(@PathVariable String file) {
        return "../static/styles/" + file + ".css";
    }
}
