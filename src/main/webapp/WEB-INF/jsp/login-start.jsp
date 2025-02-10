<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<body>
<h2>Employee Info</h2>
<br>

<%--@elvariable id="credentials" type="com.klimov.etl.vol_work.entity.CredentialsInfo"--%>
<form:form action="checkAccess" modelAttribute="credentials">

    <form:input path="login"/>
    <br>
    <form:input path="password"/>
    <br>
    <input type="submit" value="OK">

    <c:if test="${credentials.authError}">
        Ошибка в введенных данных
    </c:if>



</form:form>

</body>

</html>