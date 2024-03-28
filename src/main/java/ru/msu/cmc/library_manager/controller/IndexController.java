package ru.msu.cmc.library_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping(value = {"/index", "/"})
    public String getIndex() {
        return "index";
    }
}
