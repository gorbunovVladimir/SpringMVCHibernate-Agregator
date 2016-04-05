package com.gorbunov.spring.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.gorbunov.spring.model.Vacancy;

@Repository
public class VacancyDAOImpl implements VacancyDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(VacancyDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	@Override
	public void addVacancy(Vacancy p) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(p);
		logger.info("Vacancy saved successfully, Vacancy Details="+p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Vacancy> listVacancies(String search, String field) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Vacancy> vacanciesList;
		if (null == search || search.trim().isEmpty()) {
		  vacanciesList = session.createQuery("from Vacancy").list();
		  for(Vacancy p : vacanciesList){
			logger.info("Vacancy List::"+p);
		  }
		 }
		else {
          Criteria crit = session.createCriteria(Vacancy.class);
          crit.add(Restrictions.like(field, search ,MatchMode.ANYWHERE));// "Mou%"
          vacanciesList = crit.list();
          for(Vacancy p : vacanciesList){
			logger.info("Vacancy List form search::"+p);
		  }
         }
		return vacanciesList;
	}
 
	@Override
	public void removeVacancy(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Vacancy p = (Vacancy) session.load(Vacancy.class, new Integer(id));
		if(null != p){
			session.delete(p);
		}
		logger.info("Vacancy deleted successfully, vacancy details="+p);
	}
	
	@Override
	public void removeAll() {
		Session session = this.sessionFactory.getCurrentSession();
		session.createQuery("DELETE Vacancy").executeUpdate();
		logger.info("All records deleted successfully");
	}

}
