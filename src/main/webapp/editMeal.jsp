<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit meal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mainStyle.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<br>
<form id="myForm" method="post">
    <label for="dateTime">DateTime</label>
    <input type="datetime-local" id="dateTime" name="dateTime" required <c:if test="${not empty meal}">value
            ="${meal.dateTime}"</c:if>><br><br>

    <label for="description">Description</label>
    <input type="text" id="description" name="description" maxlength="255" required <c:if test="${not empty meal}">value
            ="${meal.description}"</c:if>><br><br>

    <label for="calories">Calories</label>
    <input type="number" id="calories" name="calories" min="0" max="10000" required <c:if test="${not empty meal}">value
            ="${meal.calories}"</c:if>><span class="error" id="caloriesError"></span><br><br>

    <button type="submit">Save</button>
    <button type="button" onclick="window.location.href='meals'">Cancel</button>
</form>

</body>
</html>
