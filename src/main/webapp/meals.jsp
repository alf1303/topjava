<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    th, td {
        padding: 8px;
    }
</style>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body style="font-size: 16px">
<h3><a href="index.html">Home</a> </h3>
<hr>
<h2>Meals</h2>
<h3><a href="meals?action=addMeal">Add meal</a> </h3>
<table border="1"; style="border-collapse: collapse; padding: 8px">
    <tr>
        <th style="font-weight: bold">Date</th>
        <th style="font-weight: bold">Description</th>
        <th style="font-weight: bold">Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${mealsToList}" var="meal">
        <tr style="color: ${meal.isExcess() ? 'red' : 'green'}">
            <td>
                <c:out value="${meal.getFormattedDateTime()}"/>
            </td>
            <td>
                <c:out value="${meal.getDescription()}"/>
            </td>
            <td>
                <c:out value="${meal.getCalories()}"/>
            </td>
            <td><a href="meals?action=edit&id=<c:out value='${meal.getId()}'/>">Edit</a> </td>
            <td><a href="meals?action=delete&id=<c:out value='${meal.id}' />">Delete</a> </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
