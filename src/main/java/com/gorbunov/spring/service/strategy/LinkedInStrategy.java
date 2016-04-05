package com.gorbunov.spring.service.strategy;

import com.gorbunov.spring.model.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class LinkedInStrategy implements Strategy {
    private static final String URL_FORMAT = "http://www.linkedin.com/vsearch/j?keywords=java+%s";

    @Override
    public List<Vacancy> getVacancies(String vacancySearch, String searchString) {
        return new ArrayList<Vacancy>();
    }
}