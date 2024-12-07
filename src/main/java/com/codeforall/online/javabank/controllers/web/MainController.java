package com.codeforall.online.javabank.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsible for rendering the initial page of the application
 */
@Controller
public class MainController {

    /**
     * Render the home page view
     * @return the view
     */
    @RequestMapping("/")
    public String home() {
        return "redirect:/customer/list";
    }
}

