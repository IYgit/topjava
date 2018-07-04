<%--
  Created by IntelliJ IDEA.
  User: iy
  Date: 03.07.18
  Time: 21:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MealForm</title>
</head>
<body>
<section>
    <h3><a href="meals">Meals</a></h3>
    <h2>${param.action == 'update' ? 'Update meal' : 'Edit meal'}</h2>
    <form action="meals?action=${param.action}" method="post">
        <input type="hidden" name="id" value="${meal.getId()}">
        <dl>
            <dt>DateTime:</dt>
            <dd><input type="datetime-local" value="${meal.getDateTime()}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt>Description:</dt>
            <dd><input type="text" value="${meal.getDescription()}" name="description" required></dd>
        </dl>
        <dl>
            <dt>Calories:</dt>
            <dd><input type="number" value="${meal.getCalories()}" name="calories" required></dd>
        </dl>
        <button type="submit">Save</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </form>
</section>
</body>
</html>
