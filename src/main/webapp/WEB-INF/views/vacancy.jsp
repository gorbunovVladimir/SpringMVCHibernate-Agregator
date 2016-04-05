<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<html>
<head>
	<title>Vacancies Page</title>
	<style type="text/css">
		.tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}
		.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}
		.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}
		.tg .tg-4eph{background-color:#f9f9f9}
	</style>
</head>
<body>
<h3>
	To aggregate
</h3>
<h4>
	Choose property for agregator
</h4>
<c:url var="aggregateAction" value="/vacancy/aggregate" ></c:url>

<form:form action="${aggregateAction}" >
<table >
    <tr>
		<td width="120"></td>
		<td width="120"></td>
		<td width="120"></td>
		<td width="120"></td>
	</tr>
    <tr>
       <td>Choose source </td>
       <td>Yandex <input type="checkbox" name="strategy" value="YandexStrategy"></td>
       <td>E1 <input type="checkbox" name="strategy" value="E1Strategy"></td>
       <td>HH <input type="checkbox" name="strategy" value="HHStrategy"></td>
    </tr>
    <tr>
       <td>Choose the city </td>
       <td>Москва <input type="radio" name="city" value="Москва"></td>
       <td>Екатеринбург <input type="radio" name="city" value="Екатеринбург"></td>
    </tr>
    <tr>
       <td>Enter the name of vacancy </td>
       <td><input type="text" name="vacancyName"></td>
    </tr>
    <tr>
       <td><input type="submit" name="submit" value="Choose"></td>
    </tr>
  </table>
</form:form>

<br>
<h3>Vacancies List</h3>
<c:if test="${!empty listVacancies}">
	<table class="tg">
	<tr>
		<th width="30">Vacancy ID</th>
		<th width="120">Vacancy Title / Url of Vacancy</th>
		<th width="120">Vacancy City</th>
		<th width="120">Vacancy Company Name / Local Url of Site Company</th>
		<th width="120">Vacancy Salary</th>
		<th width="120">Source</th>
		<th width="60">Delete</th>
	</tr>
	<c:forEach items="${listVacancies}" var="vacancy">
		<tr>
			<td>${vacancy.id}</td>
			<td><a href="<c:url value='${vacancy.url}' />" >${vacancy.title}</a></td>
			<td>${vacancy.city}</td>
			<td><a href="<c:url value='${vacancy.urlLocalSiteCompany}' />" >${vacancy.companyName}</a></td>
			<td>${vacancy.salary}</td>
			<td>${vacancy.siteName}</td>
			<td><a href="<c:url value='/vacancy/remove/${vacancy.id}' />" >Delete</a></td>
			
		</tr>
	</c:forEach>
	</table>
</c:if>
<br>


<h4>
	Search Vacancies
</h4>
<c:url var="searchVacancies" value="/vacancies" ></c:url>

<form:form action="${searchVacancies}" method="get">
<table >
    <tr>
		<td width="130"></td>
		<td width="170"></td>
		
	</tr>
    <tr>
       <td>Choose value </td>
       <td><input type="text" name="search"></td>
    </tr>
    <tr>
       <td>Choose field </td>
       <td>Title <input type="radio" name="field" value="title"></td>
    </tr>
    <tr>
       <td> </td>
       <td>Salary <input type="radio" name="field" value="salary"></td>
    </tr>
    <tr>
       <td> </td>
       <td>City <input type="radio" name="field" value="city"></td>
    </tr>
    <tr>
       <td> </td>
       <td>Name of Company <input type="radio" name="field" value="companyName"></td>
    </tr>
    <tr>
       <td> </td>
       <td>Source <input type="radio" name="field" value="siteName"></td>
    </tr>
    <tr>
       <td> </td>
       <td>Url of Vacancy <input type="radio" name="field" value="url"></td>
    </tr>
    <tr>
       <td> </td>
       <td>Local Url of Site Company <input type="radio" name="field" value="urlLocalSiteCompany"></td>
    </tr>
    <tr>
       <td><input type="submit" name="submit" value="Search Vacancies"></td>
    </tr>
  </table>
</form:form>

<br>

<c:url var="removeAll" value="/vacancy/removeAll" ></c:url>
<form:form action="${removeAll}" >
<table >
    <tr>
		<td width="120"></td>
	</tr>
    <tr>
       <td><input type="submit" name="submit" value="Remove all records"></td>
    </tr>
  </table>
</form:form>


</body>
</html>
