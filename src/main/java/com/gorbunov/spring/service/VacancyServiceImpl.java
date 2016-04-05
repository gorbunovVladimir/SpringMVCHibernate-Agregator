package com.gorbunov.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gorbunov.spring.dao.VacancyDAO;
import com.gorbunov.spring.model.Vacancy;
import com.gorbunov.spring.service.strategy.E1Strategy;
import com.gorbunov.spring.service.strategy.HHStrategy;
import com.gorbunov.spring.service.strategy.Strategy;
import com.gorbunov.spring.service.strategy.YandexStrategy;

@Service
public class VacancyServiceImpl implements VacancyService {
	
	private VacancyDAO vacancyDAO;

	public void setVacancyDAO(VacancyDAO vacancyDAO) {
		this.vacancyDAO = vacancyDAO;
	}

	@Override
	@Transactional
	public List<Vacancy> listVacancies(String search, String field) {
		return this.vacancyDAO.listVacancies(search, field);
	}

	@Override
	@Transactional
	public void removeVacancy(int id) {
		this.vacancyDAO.removeVacancy(id);
	}
	@Override
	@Transactional
	public void aggregateVacancy(String strategyString, String vacancySearch, String searchString){
		List<Vacancy> vacancies=new ArrayList<Vacancy>();
		if (strategyString.equals("YandexStrategy")){
			Strategy yandex=new YandexStrategy();
			vacancies=yandex.getVacancies(vacancySearch, searchString);
		}
		if (strategyString.equals("HHStrategy")){
			Strategy hh=new HHStrategy();
			vacancies.addAll(hh.getVacancies(vacancySearch, searchString));
		}
		if (strategyString.equals("E1Strategy")){
			Strategy e1=new E1Strategy();
			vacancies.addAll(e1.getVacancies(vacancySearch, searchString));
		}
		for (int i=0;i<vacancies.size();i++){
			this.vacancyDAO.addVacancy(vacancies.get(i));
		}
			
	}
	@Override
	@Transactional
	public void removeAll(){
		this.vacancyDAO.removeAll();
	}

}
