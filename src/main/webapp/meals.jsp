<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tu" uri="/WEB-INF/functions.tld" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mainStyle.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table class="drop-shadow">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="mealTo" items="${mealsToList}">
        <tr <c:if test="${mealTo.excess}">class="excess"</c:if>>
            <td>${tu:formatLocalDateTime(mealTo.dateTime, 'dd.MM.yyyy HH:mm')}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>