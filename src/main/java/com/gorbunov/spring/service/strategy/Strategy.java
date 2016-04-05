package com.gorbunov.spring.service.strategy;

import com.gorbunov.spring.model.Vacancy;

import java.util.List;

public interface Strategy {
    List<Vacancy> getVacancies(String vacancySearch, String searchString);
}