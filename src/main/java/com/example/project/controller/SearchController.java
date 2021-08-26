package com.example.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityManager;

@Controller
public class SearchController {
    @Autowired
    private EntityManager entityManager;


}
