<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
<%--    <style><%@include file="/WEB-INF/css/loginScreen.css"%></style>--%>
    <link rel="stylesheet" href="<c:url value='/css/loginScreen.css'/>" />
    <title>Login Page</title>
</head>
<body>


<div class="form-container">
    <h2>ETL-VOL</h2>
    <%--@elvariable id="credentials" type="com.klimov.etl.vol_work.entity.CredentialsInfo"--%>
    <form:form action="checkAccess" modelAttribute="credentials">

        <form:input cssClass="input_form"
                    path="login"
                    id="login"
                    placeholder="username"/>
        <form:input cssClass="input_form"
                    path="password"
                    type="password"
                    id="password"
                    placeholder="password"/>

        <input class="input_button" type="submit" value="Войти">

        <c:if test="${credentials.authError}">
            <p class="error-message">Ошибка в введенных данных</p>
        </c:if>

    </form:form>
</div>

</body>
</html>