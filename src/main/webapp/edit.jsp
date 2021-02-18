<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<style>
    .around {
        background-color: antiquewhite;
        list-style-type: none;
        width: 500px;
        font-size: 18px;
    }
    .form-row {
        padding: 5px;
        display: flex;
        justify-content: flex-end;
    }
    .form-row > input{
        flex: 2;
        font-size: 16px;
    }
    .form-row > label {
        flex: 1;
    }
</style>
<html lang="ru">
<head>
    <title>Edit Meal</title>
</head>
<body style="font-size: 18px">
<h3><a href="index.html">Home</a> </h3>
<hr>
<h2>Edit meal</h2>
<form method="post" action="meals" name="AddMeal">
    <input type="hidden" name="mealId" value="${meal.getId()}"/>
    <ul class="around">
        <li class="form-row">
            <label>Date:</label>
            <input type="datetime-local" name="date" value="<c:out value="${meal.getDateTime()}"/>"/>
        </li>
        <li class="form-row">
            <label>Description:</label>
            <input type="text" name="description" value="<c:out value="${meal.getDescription()}"/>"/>
        </li>
        <li class="form-row">
            <label>Calories:</label>
            <input type="number" name="calories" value="<c:out value="${meal.getCalories()}"/>"/>
        </li>
        <li class="form-row">
            <input type="submit" value="Save"/>
            <input type="button" value="Cancel" onclick="history.back()"/>
        </li>
    </ul>
</form>
</body>
</html>
