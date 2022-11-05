<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="currentMatch">

	<h2>Esperando a 2º jugador</h2>
	<h2>${match.id}</h2>


	<a href="<c:url value="/matches/1/currentMatch" />"> <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>






</petclinic:layout>
