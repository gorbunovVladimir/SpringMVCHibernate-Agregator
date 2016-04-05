package com.gorbunov.spring.service.strategy;

import com.gorbunov.spring.model.Vacancy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class E1Strategy implements Strategy {
    /*
    в модель можно:
    -Описание
    -Требования
    -Обязанности
    -Условия
      -график работы
      ...
    -(-дата размещения вакансии)

    на вкладке вакансии:
    -Контакты работодателя - через javascript
    */
    //view-source:http://ekb.zarplata.ru/vacancy?state%5B%5D=1&state%5B%5D=4&average_salary=1&categories_facets=1&city_id=994&currency_id=299&geo_id%5B%5D=994&highlight=1&q=%D0%9E%D1%85%D1%80%D0%B0%D0%BD%D0%BD%D0%B8%D0%BA&salary=0&search_type=simple&sort=date&searched_q=%D0%9E%D1%85%D1%80%D0%B0%D0%BD%D0%BD%D0%B8%D0%BA&limit=25&offset=0

    private static final String URL_FORMAT = "http://ekb.zarplata.ru/vacancy?state[]=1&state[]=4&average_salary=1&categories_facets=1&city_id=%d&currency_id=299&geo_id[]=%d&highlight=1&q=%s&salary=0&search_type=simple&sort=date&searched_q=%s&limit=25&offset=%d";

    public List<Vacancy> getVacancies(String vacancySearch, String searchString) {
        vacancySearch=vacancySearch.trim().replaceAll("\\s+","+");
        /*
		Екб geo_id%5B%5D=994
        Пышма geo_id%5B%5D=997
        Пермь geo_id%5B%5D=876
        Оренбург geo_id%5B%5D=846
        */
		int areaCode;
    	List<Vacancy> vacancies = new ArrayList<Vacancy>();
    	
    	if (searchString.equals("Екатеринбург")) areaCode=994;
    	else return vacancies;
		
		try {
            int pageNumber = 0;
            int pageNumberForSave=1;//?
            Document doc;
            while (true) {
                doc = getDocument(vacancySearch, areaCode, pageNumber);
				pageNumber+=25;
                if (doc == null) break;
                /*try {
                	BufferedWriter fWriter = new BufferedWriter(new FileWriter("d:/temp/vac-e1 - "+vacancySearch+" "+(pageNumberForSave)+".html"));
                    //BufferedWriter fWriter = new BufferedWriter(new FileWriter("./src/main/java" + this.getClass().getPackage().getName().replace('.', '/').replace("model","SaveDoc") + "/vac-e1 - "+vacancySearch+" "+(pageNumberForSave)+".html"));
                    pageNumberForSave++;//?
                    fWriter.write(doc.html());
                    fWriter.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }*/
                /*<div class="ra-elements-container">
                    <ul class="ra-elements-list-hidden">
                        <li>
                            <a href="/vacancy/Ohranniki?id=86690833">
                               <h3>Охранники</h3>
                            </a>
                            <a href="/company/view/13043433">ЧОО АЛЬТАИР </a>
                            <h3>Обязанности:</h3><p>Поддержание безопасности на объекте</p><h3>Требования:</h3><p>Наличие лицензии, отсутствие судимости</p><h3>Условия:</h3><p>Достойные условия труда, своевременный заработок, стабильность, ежемесячный премиальный фонд.</p>        <p>Екатеринбург</p>        <p>Зарплата: от 23 000 до 26 000 руб.</p>
                        </li>
                        <li>
                            <a href="/vacancy/Ohrannik?id=24888508">
                               <h3>Охранник</h3>
                            </a>
                            <a href="/company/view/4135508">ООО ТК Партнер </a>
                            <h3>Описание</h3><p>В связи с увеличением количества объектов приглашаем на работу охранников,<br>а также стажеров.<br>Графики: суточные<br>Заработная плата выдается своевременно.</p><h3>Требования:</h3><ul><li>опыт работы приветствуется</li><li>дисциплинированность</li><li>ответственное отношение к работе</li><li>отсутствие вредных привычек</li></ul>        <p>Екатеринбург</p>        <p>Зарплата: от 1 000 до 1 300 руб./смена</p>
                        </li>
                * */

                Elements elements = doc.select("div.ra-elements-container > ul.ra-elements-list-hidden > li");// select all li
                if (elements.size() == 0) break;
                for (Element element : elements) {
                    // title
                    String title = element.select("a").get(0).text();
                    // salary ;
                    String salary = element.select("p").last().text();
					if (salary.toLowerCase().contains("Зарплата: ".toLowerCase())){
                        salary=salary.replaceAll("Зарплата: ","");
					}
					else {salary="";}
                    // city
                    String city="";
                    try{
                        Elements li = element.select("p");
                        city=li.get(li.size()-2).text();
                    }
                    catch (Exception e){
                        city=searchString;
                    }
                    // company
                    String companyName;
                    try{
                        companyName = element.select("a").get(1).text();
                    }
                    catch (IndexOutOfBoundsException e){
                        companyName="";
                    }
                    // site
                    String siteName = "http://ekb.zarplata.ru/";
                    // url
                    String url = "http://ekb.zarplata.ru"+element.select("a").get(0).attr("href");
                    // urlLocalSiteCompany
                    String urlLocalSiteCompany;
                    try{
                        urlLocalSiteCompany = "http://ekb.zarplata.ru"+element.select("a").get(1).attr("href");
                    }
                    catch (IndexOutOfBoundsException e){
                        urlLocalSiteCompany="";
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

    protected Document getDocument(String vacancySearch,int areaCode, int page) throws IOException {
        // proxy settings
        /*??*/ //System.setProperty("http.proxyHost", "127.0.0.1");
        /*??*/ //System.setProperty("http.proxyPort", "8008");

        String url = String.format(URL_FORMAT, areaCode,areaCode,vacancySearch,vacancySearch, page);
        //System.out.println(url);
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36")
                .timeout(15*1000)
                .referrer("none")
                .get();
        return document;
    }
}