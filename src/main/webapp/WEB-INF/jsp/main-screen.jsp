<%--<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html>--%>

<%--<body>--%>
<%--<h2>Cool</h2>--%>


<%--</body>--%>

<%--</html>--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard</title>
    <style><%@include file="/WEB-INF/css/loginScreen.css"%></style>
</head>
<body>

<!-- Основной контейнер для макета -->
<div class="container">

    <!-- Блок с кнопками для запуска потоков и действий -->
    <div class="left-panel">
        <div class="action-buttons">
            <button class="button red">Запуск потоков</button>
            <button class="button green">Действия</button>
        </div>
        <div class="links">
            <button class="button yellow">RDV карусель</button>
            <button class="button orange">IDL карусель</button>
            <button class="button purple">DATA FIX</button>
            <button class="button blue">Мастер потоки</button>
            <button class="button teal">Сфера знаний</button>
            <button class="button brown">GRAFANA</button>
            <button class="button gray">MIR XA</button>
        </div>
    </div>

    <!-- Центральная часть с добавлением потоков -->
    <div class="center-panel">
        <h2>Добавление потоков</h2>
        <%--@elvariable id="addingUserTask" type="com.klimov.etl.vol_work.entity.UserTask"--%>
        <form:form action="addFlow" method="post" modelAttribute="addingUserTask">

            <form:input class="input-field"
            type="text"
            name="flow_name"
            placeholder="Отслеживание"
            path="runType"/>

            <form:input class="input-field"
            type="text"
            name="dag_id"
            placeholder="DAG ID"
            path="dagId"/>

            <form:input class="input-field"
            type="text"
            name="comment"
            placeholder="Комментарий"
            path="comment"/>

            <button class="button green" type="submit" >ADD FLOW</button>
        </form:form>
    </div>

    <!-- Правая панель с действиями и поиском -->
    <div class="right-panel">
        <div class="actions">
            <button class="button orange">ШАРА</button>
            <button class="button teal">СФЕРА Inno</button>
            <button class="button blue">ЧЕК ЛИСТ</button>
        </div>
        <div class="search">
            <form:input type="text" placeholder="Универсальный поиск" class="input-field">
            <select name="search-type" class="input-field">
                <option value="task">Поток ЯДРА</option>
                <!-- Дополнительные опции -->
            </select>
            <button class="button green">ИСКАТЬ</button>
        </div>
    </div>

    <!-- Нижняя часть с таблицей для мониторинга состояния потоков -->
    <div class="bottom-panel">
        <h3>Следим за статусом</h3>
        <table>
            <thead>
            <tr>
                <th>DAG ID</th>
                <th>СТАТУС</th>
                <th>ЗАДАЧА</th>
                <th>КОММЕНТАРИЙ</th>
                <th>ЗАПУСК</th>
                <th>ДЕЙСТВИЯ</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>wf_card_wayn_ods</td>
                <td class="status-running">running</td>
                <td>02:00, 10:00, 18:00</td>
                <td>Комментарий</td>
                <td>2025-02-12</td>
                <td><button class="button red">DELETE</button></td>
            </tr>
            <tr>
                <td>wf_card_wayn_ods</td>
                <td class="status-success">success</td>
                <td>20:00</td>
                <td>Комментарий</td>
                <td>2025-02-11</td>
                <td><button class="button red">DELETE</button></td>
            </tr>
            <!-- Дополнительные строки -->
            </tbody>
        </table>
    </div>

</div>

</body>
</html>