<%--<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html>--%>

<%--<body>--%>
<%--<h2>Cool</h2>--%>


<%--</body>--%>

<%--</html>--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard</title>
    <style>
        <%@include file="/WEB-INF/css/waitingScreen.css" %>
    </style>
</head>
<body>

<!-- Основной контейнер для макета -->
<div class="root-container">
    <h3>Загрузка данных. Пожалуйста, подождите...</h3>
    <meta http-equiv="refresh" content="10; URL=control_panel">

</div>

</body>
</html>