<%--
  Created by IntelliJ IDEA.
  User: iy
  Date: 01.07.18
  Time: 19:32
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<br>
<br>

<c:if test="${!empty meals}">
    <table>
            <%-- table head--%>
        <tr align="left">
            <th width="200">Date</th>
            <th width="200">Description</th>
            <th width="200">Calories</th>
            <th width="100">Edit</th>
            <th width="100">Delete</th>
        </tr>

            <%-- table data--%>
        <c:forEach items="${meals}" var="meal">
            <%--set color--%>
            <c:choose>
                <c:when test="${!meal.isExceed()}">
                    <c:set var="color" value="green"/>
                </c:when>
                <c:otherwise>
                    <c:set var="color" value="red"/>
                </c:otherwise>
            </c:choose>
            <%-- set horizontal line--%>
            <tr>
                <td colspan="5">
                    <hr>
                </td>
            </tr>
            <tr style="color: ${color}">
                <td><%--deleting 'T' from date--%>
                    <c:forTokens items="${meal.getDateTime()}" delims="T" var="time">
                        ${time}
                    </c:forTokens>
                </td>
                <td>${meal.getDescription()}</td>
                <td>${meal.getCalories()}</td>
                <td><a href="meals?action=update&id=${meal.getId()}">Update</a></td>
            </tr>
        </c:forEach>
    </table>
</c:if>
</body>
</html>
