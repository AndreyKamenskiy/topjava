<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <div>
        <form id="filter" method="get" action="meals?action=filter">
            <label for="fromDateInput">От даты (включая)</label>
            <input type="date" id="fromDateInput" autocomplete="off" name="fromDate"
                   value="${param.fromDate}">
            <label for="toDateInput">До даты (включая)</label>
            <input type="date" id="toDateInput" autocomplete="off" name="toDate"
                   value="${param.toDate}">

            <label for="fromTimeInput">От времени (включая)</label>
            <input type="time" id="fromTimeInput" autocomplete="off" name="fromTime"
                   value="${param.fromTime}">

            <label for="toTimeInput">До времени (исключая)</label>
            <input type="time" id="toTimeInput" autocomplete="off" name="toTime"
                   value="${param.toTime}">
            <button type="submit">Filter</button>
        </form>
    </div>

    <a href="meals?action=create&fromDate=${param.fromDate}&toDate=${param.toDate}&fromTime=${param.fromTime}&toTime=${param.toTime}">Add
        Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>
                    <a href="meals?action=update&id=${meal.id}&fromDate=${param.fromDate}&toDate=${param.toDate}&fromTime=${param.fromTime}&toTime=${param.toTime}">Update
                    </a></td>
                <td>
                    <a href="meals?action=delete&id=${meal.id}&fromDate=${param.fromDate}&toDate=${param.toDate}&fromTime=${param.fromTime}&toTime=${param.toTime}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>