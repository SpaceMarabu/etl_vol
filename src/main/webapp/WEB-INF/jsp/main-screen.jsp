<%--<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html>--%>

<%--<body>--%>
<%--<h2>Cool</h2>--%>


<%--</body>--%>

<%--</html>--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value='/css/main-screen.css'/>" />
    <title>Dashboard</title>
</head>
<body>

<!-- Основной контейнер для макета -->
<div class="root-container">
    <meta http-equiv="refresh" content="60; URL=controlPanel">
    <div class="top-container">
        <!-- Блок с кнопками для запуска потоков и действий -->

        <div class="left-panel">
            <div class="start-stop-radio-container">
                <%--@elvariable id="screenState" type="com.klimov.etl.vol_work.entity.MainScreenStateService"--%>
                <h3 class="left-right-panel-h3">Запуск потоков</h3>
                <c:choose>
                    <c:when test="${screenState.pause}">
                        <form:form action="setUnpause" modelAttribute="screenState">
                            <%--                            <button class="button-on-off button-on-off-not-clicked" type="submit">Вкл.</button>--%>
                            <button class="button-on-off button-off-clicked" type="submit">Выкл.</button>
                        </form:form>
                    </c:when>
                    <c:otherwise>
                        <form:form action="setPause" modelAttribute="screenState">
                            <button class="button-on-off button-on-clicked" type="submit">Вкл.</button>
                            <%--                            <button class="button-on-off button-on-off-not-clicked" type="submit">Выкл.</button>--%>
                        </form:form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>


        <!-- Центральная часть с добавлением потоков -->
        <div class="center-panel">
            <h2 class="center-h2">Добавление потоков</h2>
            <%--@elvariable id="addingUserTask" type="com.klimov.etl.vol_work.entity.UserTaskFromUI"--%>
            <%--@elvariable id="listRunType" type="List<com.klimov.etl.vol_work.entity.RunType>"--%>
            <form:form action="addFlow" modelAttribute="addingUserTask">

                <form:select cssClass="input-field"
                             cssErrorClass="input-field error-field"
                             id="runType"
                             path="runType">
                    <option value="${addingUserTask.runType}">${addingUserTask.runType}</option>
                    <c:forEach var="runOption" items="${listRunType}">
                        <c:if test="${runOption != addingUserTask.runType}">
                            <option value="${runOption}">${runOption}</option>
                        </c:if>
                    </c:forEach>
                </form:select>

                <form:input class="input-field"
                            cssErrorClass="input-field error-field"
                            type="text"
                            name="dag_id"
                            placeholder="DAG ID"
                            value="${addingUserTask.dagId}"
                            path="dagId"/>

                <form:input class="input-field"
                            cssErrorClass="input-field error-field"
                            type="text"
                            name="comment"
                            placeholder="Номер задачи"
                            value="${addingUserTask.taskId}"
                            path="taskId"/>

                <form:input class="input-field"
                            cssErrorClass="input-field error-field"
                            type="text"
                            name="comment"
                            placeholder="Комментарий"
                            value="${addingUserTask.comment}"
                            path="comment"/>

                <form:textarea class="input-field text-area"
                               cssErrorClass="input-field text-area error-field"
                               type="text"
                               name="config"
                               placeholder="конфиг1&#10;конфиг2&#10;...&#10;конфиг3"
                               value="${addingUserTask.listConfRaw}"
                               path="listConfRaw"
                               htmlEscape="false"/>

                <button class="center-button" type="submit">ADD FLOW</button>
            </form:form>
        </div>

        <div class="right-container">
            <h3 class="left-right-panel-h3 right-panel-h3">Полезные ссылки</h3>
            <div class="links-container">
                <button class="button-right"
                        onclick="window.open('https://example.com/RDV', '_blank')">RDV карусель
                </button>
                <button class="button-right"
                        onclick="window.open('https://example.com/IDL', '_blank')">IDL карусель
                </button>
                <button class="button-right"
                        onclick="window.open('https://example.com/DF', '_blank')">DATA FIX
                </button>
                <button class="button-right"
                        onclick="window.open('https://example.com/MP', '_blank')">Мастер потоки
                </button>
                <button class="button-right"
                        onclick="window.open('https://example.com/SFERA', '_blank')">Сфера знаний
                </button>
                <button class="button-right"
                        onclick="window.open('https://example.com/GRAFANA', '_blank')">GRAFANA
                </button>
                <button class="button-right"
                        onclick="window.open('https://example.com/MIR', '_blank')">MIR XA
                </button>
            </div>
        </div>
    </div>


    <!-- Нижняя часть с таблицей для мониторинга состояния потоков -->
    <div class="bottom-panel">

        <c:if test="${screenState.dagObserveList.size() > 0}">
            <h3 class="line-pointer">Следим за статусом</h3>
            <table>
                <thead>
                <tr>
                    <th class="th-td-dag-id-comment">DAG ID</th>
                    <th>СТАТУС</th>
                    <th>ЗАДАЧА</th>
                    <th class="th-td-dag-id-comment">КОММЕНТАРИЙ</th>
                    <th>ЗАПУСК</th>
                    <th>ДЕЙСТВИЯ</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="dagRun" items="${screenState.dagObserveList}">
                    <c:url var="deleteButton" value="/deleteTask">
                        <c:param name="taskId" value="${dagRun.taskId}"/>
                    </c:url>
                    <tr>
                        <td class="th-td-dag-id-comment">
                            <a href="http://0.0.0.0:8081/dagrun/list/?_flt_1_dag_id=${fn:substring(dagRun.dagId, 2, -1)}"
                               target="_blank"
                            >${dagRun.dagId}</a>
                        </td>
                        <c:choose>
                            <c:when test="${dagRun.state == 'success'}">
                                <td class="status-success">${dagRun.state}</td>
                            </c:when>
                            <c:when test="${dagRun.state == 'failed' || dagRun.state == 'no runs'}">
                                <td class="status-failed">${dagRun.state}</td>
                            </c:when>
                            <c:otherwise>
                                <td class="other-dagrun-status">${dagRun.state}</td>
                            </c:otherwise>
                        </c:choose>
                        <td>${dagRun.taskId}</td>
                        <td class="th-td-dag-id-comment">${dagRun.comment}</td>
                        <td>${dagRun.startDate}</td>
                        <td>
                            <button class="button-manage-task"
                                    onclick="window.location.href='${deleteButton}'">DELETE
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <%--@elvariable id="runType" type="com.klimov.etl.vol_work.entity.RunType"--%>
        <c:if test="${screenState.dagRunList.size() > 0}">
        <h3 class="line-pointer">Запуск по конфигам</h3>
        <table>
            <thead>
            <tr>
                <th class="th-td-dag-id-comment">DAG ID</th>
                <th>СТАТУС</th>
                <th>ЗАДАЧА</th>
                <th class="th-td-dag-id-comment">КОММЕНТАРИЙ</th>
                <th>ЗАПУСК</th>
                <th>ДЕЙСТВИЯ</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="dagRun" items="${screenState.dagRunList}">
                <c:url var="deleteButton" value="/deleteTask">
                    <c:param name="taskId" value="${dagRun.taskId}"/>
                </c:url>
                <c:url var="resetButton" value="/resetTask">
                    <c:param name="taskId" value="${dagRun.taskId}"/>
                </c:url>
                <c:url var="resetButton" value="/resetTask">
                    <c:param name="taskId" value="${dagRun}"/>
                </c:url>
                <tr>
                    <td class="th-td-dag-id-comment">
                        <a href="http://0.0.0.0:8081/dagrun/list/?_flt_1_dag_id=${fn:substring(dagRun.dagId, 2, -1)}"
                           target="_blank"
                        >${dagRun.dagId}</a>
                    </td>
                    <c:choose>
                        <c:when test="${dagRun.state == 'success'}">
                            <td class="status-success">${dagRun.state}</td>
                        </c:when>
                        <c:when test="${dagRun.state == 'done'}">
                            <td class="status-done">${dagRun.state}</td>
                        </c:when>
                        <c:when test="${dagRun.state == 'failed' || dagRun.state == 'no runs'}">
                            <td class="status-failed">${dagRun.state}</td>
                        </c:when>
                        <c:otherwise>
                            <td class="other-dagrun-status">${dagRun.state}</td>
                        </c:otherwise>
                    </c:choose>
                    <td>${dagRun.taskId}</td>
                    <td class="th-td-dag-id-comment">${dagRun.comment}</td>
                    <td>${dagRun.startDate}</td>
                    <td>
                        <button class="button-manage-task"
                                onclick="window.location.href='${deleteButton}'">DELETE
                        </button>
                        <button class="button-manage-task"
                                onclick="window.location.href='${resetButton}'">RESET
                        </button>

                        <c:if test="${dagRun.conf.length() > 0}">
                        <button id="conf-button" class="button-manage-task"
                                onclick="copyToClipboard('${dagRun.conf}')">CONF
                        </button>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    </c:if>

</div>

<script src="${pageContext.request.contextPath}/scripts/copyToClipboard.js"></script>
</body>
</html>