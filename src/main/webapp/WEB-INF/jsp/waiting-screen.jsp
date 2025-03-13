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
        <%@include file="/css/waitingScreen.css" %>
    </style>
</head>
<body>

<!-- Основной контейнер для макета -->
<div class="root-container">
    <h3>Загрузка данных. Пожалуйста, подождите...</h3>
    <meta http-equiv="refresh" content="5; URL=controlPanel">

</div>

</body>
</html>