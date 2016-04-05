package com.gorbunov.spring;



import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gorbunov.spring.model.Vacancy;
import com.gorbunov.spring.service.VacancyService;



@Controller
public class VacancyController {
	
	private VacancyService vacancyService;
	
	@Autowired(required=true)
	@Qualifier(value="vacancyService")
	public void setVacancyService(VacancyService ps){
		this.vacancyService = ps;
	}
	
	@RequestMapping(value = "/vacancies", method = RequestMethod.GET) 
	public String listVacancies(Model model, @RequestParam(value = "search", required = false, defaultValue = "") String search, @RequestParam(value = "field", required = false, defaultValue = "") String field) {
		model.addAttribute("vacancy", new Vacancy());
		model.addAttribute("listVacancies", this.vacancyService.listVacancies(search,field));
		return "vacancy";
	}
	
	@RequestMapping(value= "/vacancy/aggregate", method = RequestMethod.POST)
	public String aggregateVacancy(HttpServletRequest request){
		String city = request.getParameter("city");
		String vacancySearch = request.getParameter("vacancyName");
		String strategies [] = request.getParameterValues("strategy") ;	
		if (strategies != null) {
			for (int i=0;i<strategies.length;i++){ 
				 this.vacancyService.aggregateVacancy(strategies[i], vacancySearch,city);}
		}
        return "redirect:/vacancies";
	}
	
	@RequestMapping("/vacancy/remove/{id}")
    public String removeVacancy(@PathVariable("id") int id){
		this.vacancyService.removeVacancy(id);
        return "redirect:/vacancies";
    }
	
    @RequestMapping("/vacancy/removeAll")
    public String removeVacancy(){
		this.vacancyService.removeAll();
        return "redirect:/vacancies";
    }
}
