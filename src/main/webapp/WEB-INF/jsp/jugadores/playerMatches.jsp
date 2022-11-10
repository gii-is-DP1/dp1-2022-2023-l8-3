<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="playerMatches">
	<h2>Player Matches</h2>
	<table id="playerMatchesTable" class="table table-striped">
		<thead>
			<tr>
				<th>Anfitri&oacute;n</th>
				<th>Invitado</th>
				<th>Ganador</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${playerMatches}" var="playerMatch">
				<tr>
					<td><c:out value="${playerMatch.jugador1.user.username}" /></td>
					<td><c:out value="${playerMatch.jugador2.user.username}" /></td>
					<td><c:out value="${playerMatch.ganadorPartida}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>