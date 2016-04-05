package com.gorbunov.spring.service.strategy;

import com.gorbunov.spring.model.Vacancy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//import com.sun.deploy.net.URLEncoder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HHStrategy implements Strategy {

    /*
    в модель можно:
    -(-дата размещения вакансии)

    на вкладке вакансии:
    -+-Требуемый опыт работы (не всегда указывается)
    -По сути обширное discription, в котором может присутствовать
      -Описание
      -Требования
      -Обязанности
      -Условия
    -Адрес
      можно посмотреть на Яндекс.карте через javascript
    -(-Тип занятости)
    */

    private static final String URL_FORMAT = "http://hh.ru/search/vacancy?text=%s+%s&page=%d";

    public List<Vacancy> getVacancies(String vacancySearch, String searchString) {
        String tempVacancySearch=vacancySearch;
        try{
        vacancySearch=URLEncoder.encode(vacancySearch.trim(), "UTF-8");
        searchString=URLEncoder.encode(searchString, "UTF-8");
        }
        catch (UnsupportedEncodingException e){
        	System.err.println(e.toString());
        }
        
        List<Vacancy> vacancies = new ArrayList<Vacancy>();
        try {
            int pageNumber = 0;
            Document doc;
            while (true) {
                doc = getDocument(searchString,vacancySearch, pageNumber++);
                if (doc == null) break;
                /*try {
                    BufferedWriter fWriter = new BufferedWriter(new FileWriter("d:/temp/vac-hh - "+tempVacancySearch+" "+pageNumber+".html"));
                    //BufferedWriter fWriter = new BufferedWriter(new FileWriter("./src/main/java" + this.getClass().getPackage().getName().replace('.', '/').replace("model","SaveDoc") + "/vac-hh - "+tempVacancySearch+" "+pageNumber+".html"));
                    fWriter.write(doc.html());
                    fWriter.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }*/
                Elements elements = doc.select("[data-qa=vacancy-serp__vacancy]");
                if (elements.size() == 0) break;
                for (Element element : elements) {
                    // title
                    Element titleElement = element.select("[data-qa=vacancy-serp__vacancy-title]").first();
                    String title = titleElement.text();

                    // salary
                    Element salaryElement = element.select("[data-qa=vacancy-serp__vacancy-compensation]").first();
                    String salary = "";
                    if (salaryElement != null) {
                        salary = salaryElement.text();
                    }

                    // city
                    String city = element.select("[data-qa=vacancy-serp__vacancy-address]").first().text();

                    // company
                    String companyName="";
                    try{
                        companyName = element.select("[data-qa=vacancy-serp__vacancy-employer]").first().text();
                    }
                    catch (Exception e){
                        companyName = element.select("[class=search-result-item__company").first().text();
                    }
                    // site
                    String siteName = "http://hh.ru/";

                    // url
                    String url = titleElement.attr("href").replace("?query=" + vacancySearch+"%20"+searchString,"").replace("?query=" + vacancySearch.toLowerCase(),"");
                    // urlLocalSiteCompany
                    String urlLocalSiteCompany="";
                    try {
                        urlLocalSiteCompany = "http://hh.ru" + element.select("[data-qa=vacancy-serp__vacancy-employer]").first().attr("href");
                    }
                    catch (Exception e){
                    }


                    /*System.out.println("Title = " + title);
                    System.out.println("Salary = " + salary);
                    System.out.println("City = " + city);
                    System.out.println("CompanyName = " + companyName);
                    System.out.println("SiteName = " + siteName);
                    System.out.println("URL = " + url);
                    System.out.println("URLLocalSiteCompany = " + urlLocalSiteCompany);
                    System.out.println();
                    System.out.println();*/

                    // add vacancy to the list
                    Vacancy vacancy = new Vacancy();
                    vacancy.setTitle(title);
                    vacancy.setSalary(salary);
                    vacancy.setCity(city);
                    vacancy.setCompanyName(companyName);
                    vacancy.setSiteName(siteName);
                    vacancy.setUrl(url);
                    vacancy.setUrlLocalSiteCompany(urlLocalSiteCompany);
                    vacancies.add(vacancy);
                }
                //break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return vacancies;
    }

    protected Document getDocument(String searchString,String vacancySearch, int page) throws IOException {
        // proxy settings
        /*??*/ //System.setProperty("http.proxyHost", "127.0.0.1");
        /*??*/ //System.setProperty("http.proxyPort", "8008");

        String url = String.format(URL_FORMAT,vacancySearch, searchString, page);
        //System.out.println(url);
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36")
                .timeout(15*1000)
                .referrer("none")
                .get();

        return document;
    }
}