<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>


<petclinic:layout pageName="matchesList">
	<h2>Partidas</h2>



	<table id="matchesList" class="table table-striped">
		<thead>
			<tr>
				<th>Anfitrion</th>
				<th>Nombre Partida</th>
				<th>Fecha Inicio</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<form:form method="post">
				<c:forEach items="${match_list}" var="match">

					<tr>
						<td><c:out value="${match.jugador1.user.username}" /></td>
						<td><c:out value="${match.name}" /></td>
						<td><c:out value="${match.inicioPartida}" /></td>
						<td><input type="submit" value="UNIRME"
							class="btn btn-success"
							onclick="location.href = '/matches/${match.id}/currentMatch'" /></input></td>
					</tr>
				</c:forEach>
			</form:form>

		</tbody>

	</table>

	<h2>Partidas como espectador</h2>

	<table id="matches" class="table table-striped">
		<thead>
			<tr>
				<th>Anfitrion</th>
				<th>Jugador 2</th>
				<th>Fecha Inicio</th>
				<th></th>
			</tr>
		</thead>
		<tbody>

			<c:forEach items="${matches}" var="matches">
				<tr>
					<td><c:out value="${matches.jugador1.user.username}" /></td>
					<td><c:out value="${matches.jugador2.user.username}" /></td>

					<td><c:out value="${matches.inicioPartida}" /></td>
					<td><a class="btn btn-danger"
						href="<c:url value="/matches/${matches.id}/currentMatch" />">ENTRAR</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<table class="table-buttons">
		<tr>
			<td><a href="<spring:url value="/vets.xml" htmlEscape="true" />">View
					as XML</a></td>
		</tr>
	</table>
</petclinic:layout>