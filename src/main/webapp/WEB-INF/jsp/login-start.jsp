<%--<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>--%>
<%--<%@ taglib prefix="c" uri="jakarta.tags.core" %>--%>
<%--<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html>--%>

<%--<body>--%>
<%--<h2>Employee Info</h2>--%>
<%--<br>--%>

<%--&lt;%&ndash;@elvariable id="credentials" type="com.klimov.etl.vol_work.entity.CredentialsInfo"&ndash;%&gt;--%>
<%--<form:form action="checkAccess" modelAttribute="credentials">--%>

<%--    <form:input path="login"/>--%>
<%--    <br>--%>
<%--    <form:input path="password" type="password"/>--%>
<%--    <br>--%>
<%--    <input type="submit" value="OK">--%>

<%--    <c:if test="${credentials.authError}">--%>
<%--        Ошибка в введенных данных--%>
<%--    </c:if>--%>



<%--</form:form>--%>

<%--</body>--%>

<%--</html>--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login Page</title>
    <style>

        html, body {
            height: 100%;
            width: 100%;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            font-family: "Roboto Thin", sans-serif;
            background-color: #f8f9fa;
        }

        h2 {
            font-size: 2.5em;
        }

        .form-container {
            width: 20vw;
            height: 30vh;
            padding: 0;
            margin: 0;
            /*padding: 2%;*/
            background: white;
            /*box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);*/
            border-radius: 10px;
            text-align: center;
            box-sizing: border-box;
        }

        .input_form {
            width: 100%;
            height: 20%;
            padding: 0;
            margin: 0;
            /*margin: 0.1em 0.1em 0.1em 0;*/
            border: 1px solid #ccc;
            border-radius: 5px;
            /*box-sizing: border-box;*/
            background-color: #fcfdff;
            font-size: 1.3em;
        }

        .input_button {
            width: 100%;
            height: 45%;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
            background-color: #007bff;
            font-size: 1.3em;
            color: white;
            /*padding: 1%;*/
            /*margin: 5% 5% 15% 0;*/
        }

        .input_button:hover {
            background-color: #0056b3;
        }

        .error-message {
            color: red;
            margin-top: 10px;
        }
    </style>
</head>
<body>

<!-- Форма логина -->
<div class="form-container">
    <h2>Sign In</h2>
    <%--@elvariable id="credentials" type="com.klimov.etl.vol_work.entity.CredentialsInfo"--%>
    <form:form action="checkAccess" modelAttribute="credentials">
        <form:input cssClass="input_form" path="login" id="login"/>
        <br>
        <form:input cssClass="input_form" path="password" type="password" id="password"/>
        <br>

        <input class="input_button" type="submit" value="Войти">

        <c:if test="${credentials.authError}">
            <p class="error-message">Ошибка в введенных данных</p>
        </c:if>
    </form:form>
</div>

</body>
</html>