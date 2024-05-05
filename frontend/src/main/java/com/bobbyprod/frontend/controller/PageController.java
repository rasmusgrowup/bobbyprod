package com.bobbyprod.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String showIndex() {
        return "index"; // This will render index.html template
    }

    @GetMapping("/components")
    public String showComponentsPage() {
        return "components"; // This will render components.html template
    }
}
