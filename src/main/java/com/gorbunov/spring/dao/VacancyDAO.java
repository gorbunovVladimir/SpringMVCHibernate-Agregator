package com.gorbunov.spring.dao;

import java.util.List;

import com.gorbunov.spring.model.Vacancy;

public interface VacancyDAO {

	public void addVacancy(Vacancy p);
	public List<Vacancy> listVacancies(String search, String field);
	public void removeVacancy(int id);
	public void removeAll();
}
