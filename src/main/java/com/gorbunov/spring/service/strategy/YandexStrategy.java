package com.gorbunov.spring.service.strategy;

import com.gorbunov.spring.model.Vacancy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YandexStrategy implements Strategy {
 /*
    в модель можно:
    -Требования
        - Требования не указаны
        - Опыт работы: без опыта.
        - Опыт работы: 1 год.
        - Опыт работы: от 1 года
        - Опыт работы: от 2 лет.
        - Опыт работы: от 1 до 3 лет. / Опыт работы: от 3 до 6 лет.
        - Опыт работы: от 3 лет.
        - Опыт работы: от 6 лет.
       ...
<div class="serp-vacancy__requirements"><strong class="serp-vacancy__requirements-label">Требования:</strong> Опыт работы: от 1 до 3 лет. Знание Java (Core,collections,concurrency); Знание принципов ООП и шаблонов проектирования; Опыт разработки web-приложений на Java; Spring Framework ...</div>
<div class="serp-vacancy__requirements">Требования не указаны</div>
 */

    //https://rabota.yandex.ru/search?text=java&rid=54&page_num=2
    private static final String URL_FORMAT = "https://rabota.yandex.ru/search?text=%s&rid=%d&page_num=%d";

    public List<Vacancy> getVacancies(String vacancySearch, String searchString) {
        /*Екб rid=54
          СО rid=11162
          М rid=213
          М и МО rid=1 */
    	int areaCode;
    	List<Vacancy> vacancies = new ArrayList<Vacancy>();
    	
    	if (searchString.equals("Екатеринбург")) areaCode=54;
    	else if (searchString.equals("Москва")) areaCode=213;
    	else return vacancies;
        

        try {
            int pageNumber = 1;
            int pageNumberForSave=1;//?
            int numberOfVacancies=Integer.MAX_VALUE;
            Document doc;
            while (true) {
                doc = getDocument(vacancySearch, areaCode, pageNumber++);
                
                /*try {
                	BufferedWriter fWriter = new BufferedWriter(new FileWriter("d:/temp/vac-yandex-"+vacancySearch+" "+pageNumberForSave+".html"));
                    //BufferedWriter fWriter = new BufferedWriter(new FileWriter("./src/main/java" + this.getClass().getPackage().getName().replace('.', '/').replace("model","SaveDoc")+"/vac-yandex-"+vacancySearch+" "+pageNumberForSave+".html"));
                    pageNumberForSave++;//?
                    fWriter.write(doc.html());
                    fWriter.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }*/
                if (numberOfVacancies==Integer.MAX_VALUE){
                    try{
                        String numberOfVac = doc.select("[class=search-results__heading-content search-results__heading-content_type_vacancies]").text().trim().replaceAll("vacancySearch — ", "").replaceAll("\\D+", "").trim();
                        numberOfVacancies = Integer.parseInt(numberOfVac);
                    }
                    catch (Exception e){
                    }
                }
                Elements elements = doc.select("[class=serp-vacancy stat i-bem]");

                if (elements.size() == 0) {break;}
                for (Element element : elements) {
                    Element titleElement=null;
                    //titleElement = element.select("[class=heading heading_level_3 serp-vacancy__name]").first();
                    String title;
                    // title
                    try{
                        titleElement = element.select("[class=link link_redir_yes stat__click i-bem]").first();
                        title = titleElement.text();
                    }
                    catch (NullPointerException e){
                        try{
                            titleElement = element.select("[class=link link_redir_yes link_upped_yes stat__click i-bem]").first();
                            title = titleElement.text();
                        }
                        catch (NullPointerException ee){
                            try{
                                titleElement = element.select("[class=link link_redir_yes stat__click i -bem]").first();
                                title = titleElement.text();
                            }
                            catch (NullPointerException eee){
                                title="";
                            }
                        }
                    }
                    // salary
                    Element salaryElement = element.select("[class=serp-vacancy__salary]").first();
                    String salary = "";
                    if (salaryElement != null) {
                        salary = salaryElement.text().replaceAll(" ","");
                    }
                    // city
                    String city="",metro="";
                    try{
                        metro=element.select("[class=metro-item__name]").first().text();
                        city =searchString+", метро "+metro+", "+ element.select("[class=link link_minor_yes]").first().text();
                    }
                    catch (NullPointerException e){
                        try{
                            metro=element.select("[class=metro-item__name]").first().text();
                            city =searchString+", метро "+metro;
                        }
                        catch (NullPointerException ee){
                            try{
                                city =element.select("[class=link link_minor_yes]").first().text();
                            }
                            catch (NullPointerException eee){
                                city =searchString;
                            }
                        }
                    }

                    // company
                    String companyName="";
                    try{
                        companyName = element.select("[class=link link_nav_yes link_minor_yes i-bem").first().text();
                    }
                    catch(NullPointerException e){
                    }
                    // site
                    String siteName = "http://rabota.yandex.ru/";

                    // url
                    String url = titleElement.attr("href").replaceAll("\\?utm_source.+", "");
                    // urlLocalSiteCompany
                    String urlLocalSiteCompany="";
                    try{
                        urlLocalSiteCompany = "http://rabota.yandex.ru" + element.select("[class=link link_nav_yes link_minor_yes i-bem").first().attr("href").replace("?rid=" + areaCode, "");
                    }
                    catch(NullPointerException e){}

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
                if (numberOfVacancies<=(pageNumber*10))break;
                
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
        /*??*/ //System.setProperty("http.proxyPort", "9666");

        String url = String.format(URL_FORMAT, vacancySearch, areaCode, page);
        Map<String, String> cookies = new HashMap<String, String>();
        cookies.put("ys", "translatechrome_chr.8-17-0#searchextchrome.8-12-4");
        //System.out.println(url);
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36")
                .timeout(15 * 1000)
                .referrer("none")
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("accept-language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
                .header("accept-encoding", "gzip, deflate")
                .header("connection", "keep-alive")
                .cookies(cookies)
                .get();

        return document;
    }
}