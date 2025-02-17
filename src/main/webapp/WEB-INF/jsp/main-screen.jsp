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
        <%@include file="/WEB-INF/css/mainScreen.css" %>
    </style>
</head>
<body>

<!-- Основной контейнер для макета -->
<div class="root-container">

    <div class="top-container">
        <!-- Блок с кнопками для запуска потоков и действий -->


        <div class="left-panel">
            <div class="start-stop-radio-container">
                <%--@elvariable id="screenState" type="com.klimov.etl.vol_work.entity.MainScreenState"--%>
                <c:choose>
                    <c:when test="${screenState.pause}">
                        <form:form action="setUnpause" modelAttribute="screenState">
                            <button class="button-on-off button-on-off-not-clicked" type="submit">Вкл.</button>
                            <button class="button-on-off button-off-clicked" type="submit">Выкл.</button>
                        </form:form>
                    </c:when>
                    <c:otherwise>
                        <form:form action="setPause" modelAttribute="screenState">
                            <button class="button-on-off button-on-clicked" type="submit">Вкл.</button>
                            <button class="button-on-off button-on-off-not-clicked" type="submit">Выкл.</button>
                        </form:form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>


        <!-- Центральная часть с добавлением потоков -->
        <div class="center-panel">
            <h2 class="center-h2">Добавление потоков</h2>
            <%--@elvariable id="addingUserTask" type="com.klimov.etl.vol_work.entity.UserTask"--%>
            <form:form action="addFlow" modelAttribute="addingUserTask">

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

                <button class="center-button" type="submit">ADD FLOW</button>
            </form:form>
        </div>


        <div class="right-container">
            <h3 class="right-panel-h3">Полезные ссылки</h3>
            <div class="links-container">
                <button class="button-right">RDV карусель</button>
                <button class="button-right">IDL карусель</button>
                <button class="button-right">DATA FIX</button>
                <button class="button-right">Мастер потоки</button>
                <button class="button-right">Сфера знаний</button>
                <button class="button-right">GRAFANA</button>
                <button class="button-right">MIR XA</button>
            </div>
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
                <td>
                    <button class="button-right red">DELETE</button>
                </td>
            </tr>
            <tr>
                <td>wf_card_wayn_ods</td>
                <td class="status-success">success</td>
                <td>20:00</td>
                <td>Комментарий</td>
                <td>2025-02-11</td>
                <td>
                    <button class="button-right red">DELETE</button>
                </td>
            </tr>
            <!-- Дополнительные строки -->
            </tbody>
        </table>
    </div>

</div>

</body>
</html>