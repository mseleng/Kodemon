<%@ tag pageEncoding="utf-8" dynamic-attributes="dynattrs" trimDirectiveWhitespaces="true" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="head" fragment="true" %>
<%@ attribute name="body" fragment="true" required="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="kodemon" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${title}"/></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
    <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
    <jsp:invoke fragment="head"/>
</head>
<body>
<nav class="navbar navbar-inverse navbar-static-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><kodemon:a href="/"><f:message key="navigation.home"/></kodemon:a></li>
                <li><kodemon:a href="/fight/list?period=all"><f:message key="navigation.fightHistory"/></kodemon:a></li>
                <li><kodemon:a href="/gym/list"><f:message key="navigation.gyms"/></kodemon:a></li>
                <li><kodemon:a href="/user/list"><f:message key="navigation.trainers"/></kodemon:a></li>
                <c:choose>
                    <c:when test="${not empty authenticatedUser}">
                        <li><kodemon:a href="/fight/grass"><f:message key="navigation.visitGrass"/></kodemon:a></li>
                        <li><kodemon:a href="/user/detail/${authenticatedUser.userName}"><f:message key="navigation.myProfile"/></kodemon:a></li>
                        <li><kodemon:a href="/user/logout"><f:message key="navigation.logout"/></kodemon:a></li>
                    </c:when>
                    <c:otherwise>
                        <li><kodemon:a href="/login"><f:message key="navigation.login"/></kodemon:a></li>
                        <li><kodemon:a href="/register"><f:message key="navigation.register"/></kodemon:a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <c:if test="${not empty title}">
        <div class="page-header">
            <h1><c:out value="${title}"/></h1>
        </div>
    </c:if>

    <!-- alerts -->
    <c:if test="${not empty alert_danger}">
        <div class="alert alert-danger" role="alert">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            <c:out value="${alert_danger}"/></div>
    </c:if>
    <c:if test="${not empty alert_info}">
        <div class="alert alert-info" role="alert"><c:out value="${alert_info}"/></div>
    </c:if>
    <c:if test="${not empty alert_success}">
        <div class="alert alert-success" role="alert"><c:out value="${alert_success}"/></div>
    </c:if>
    <c:if test="${not empty alert_warning}">
        <div class="alert alert-warning" role="alert"><c:out value="${alert_warning}"/></div>
    </c:if>

    <jsp:invoke fragment="body"/>

    <footer class="footer">
        <p>Kodemon application</p>
    </footer>
</div>

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="https://code.jquery.com/ui/1.11.3/jquery-ui.js"></script>
<script>
    $(function () {
        $("#datepicker")
            .datepicker({
                maxDate: "-1",
                changeYear: true,
                yearRange: "-100:+0",
                dateFormat: "d.m.yy"
            })
            .datepicker('setDate', new Date());
    });
</script>
</body>
</html>

