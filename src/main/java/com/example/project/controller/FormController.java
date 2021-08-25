package com.example.project.controller;


import com.example.project.entity.Collection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class FormController {
    private static final String template = "Hello, %s!";

    @GetMapping("/greeting")
    public Collection greeting (@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Collection(String.format(template, name));
    }
}
