package com.gorbunov.spring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity bean with JPA annotations
 * Hibernate provides JPA implementation
 *
 */
@Entity
@Table(name="Vacancy")
public class Vacancy {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
    private String title;
    private String salary;
    private String city;
    private String companyName;
    private String siteName;
    private String url;//primary if not use yandexstrategy
    private String urlLocalSiteCompany;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlLocalSiteCompany() {
        return urlLocalSiteCompany;
    }

    public void setUrlLocalSiteCompany(String urlLocalSiteCompany) {
        this.urlLocalSiteCompany = urlLocalSiteCompany;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Vacancy vacancy = (Vacancy) o;

        if (city != null ? !city.equals(vacancy.city) : vacancy.city != null)
            return false;
        if (companyName != null ? !companyName.equals(vacancy.companyName) : vacancy.companyName != null)
            return false;
        if (salary != null ? !salary.equals(vacancy.salary) : vacancy.salary != null)
            return false;
        if (siteName != null ? !siteName.equals(vacancy.siteName) : vacancy.siteName != null)
            return false;
        if (title != null ? !title.equals(vacancy.title) : vacancy.title != null)
            return false;
        if (url != null ? !url.equals(vacancy.url) : vacancy.url != null)
            return false;
        if (urlLocalSiteCompany != null ? !urlLocalSiteCompany.equals(vacancy.urlLocalSiteCompany) : vacancy.urlLocalSiteCompany != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (salary != null ? salary.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (siteName != null ? siteName.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (urlLocalSiteCompany != null ? urlLocalSiteCompany.hashCode() : 0);
        return result;
    }
}