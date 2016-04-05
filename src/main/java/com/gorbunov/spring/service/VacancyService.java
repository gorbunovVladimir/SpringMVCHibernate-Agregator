package com.gorbunov.spring.service;

import java.util.List;

import com.gorbunov.spring.model.Vacancy;

public interface VacancyService {

	public List<Vacancy> listVacancies(String search, String field);
	public void removeVacancy(int id);
	public void aggregateVacancy(String strategyString, String vacancySearch, String searchString);
	public void removeAll();
}
