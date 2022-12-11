<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="jugadorDetails">

	<h2>Informaci&oacute;n del jugador</h2>


	<table class="table table-striped">
		<tr>
			<th>Usuario</th>
			<td><b><c:out value="${jugador.user.username}" /></b></td>
		</tr>
		<tr>
			<th>Nombre</th>
			<td><c:out value="${jugador.firstName}" /></td>
		</tr>
		<tr>
			<th>Apellido</th>
			<td><c:out value="${jugador.lastName}" /></td>
		</tr>
		<tr>
			<th>Email</th>
			<td><c:out value="${jugador.user.email}" /></td>
		</tr>
		<tr>
			<th>Contrase&ntilde;a</th>
			<td><c:out value="${jugador.user.password}" /></td>
		</tr>
		<tr>
			<th>Estado</th>
			<td><c:choose>
					<c:when test="${jugador.estadoOnline}">Online</c:when>
					<c:otherwise>Offline</c:otherwise>
				</c:choose></td>
		</tr>
	</table>

	<spring:url value="{jugadorId}/edit" var="editUrl">
		<spring:param name="jugadorId" value="${jugador.id}" />
	</spring:url>
	<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar
		jugador</a>


	<sec:authorize access="hasAuthority('jugador')">
		<spring:url value="{jugadorId}/playerMatches" var="playerMatchesUrl">
			<spring:param name="jugadorId" value="${jugador.id}" />
		</spring:url>
		<a href="${fn:escapeXml(playerMatchesUrl)}" class="btn btn-default">Historial
			de partidas</a>
	</sec:authorize>


	<sec:authorize access="hasAuthority('jugador')">
		<spring:url value="{jugadorId}/playerFriends" var="amigosUrl">
			<spring:param name="jugadorId" value="${jugador.id}" />
		</spring:url>
		<a href="${fn:escapeXml(amigosUrl)}" class="btn btn-default">Lista
			de amigos</a>
	</sec:authorize>


	<sec:authorize access="hasAuthority('admin')">
		<spring:url value="{jugadorId}/delete" var="DeleteUrl">
			<spring:param name="jugadorId" value="${jugador.id}" />
		</spring:url>
		<a href="${fn:escapeXml(DeleteUrl)}" class="btn btn-default">Borrar
			jugador</a>
	</sec:authorize>

</petclinic:layout>