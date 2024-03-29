<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="playerMatches">

	<h2>Partidas finalizadas</h2>
	
	<p>
		<c:out value="Total de partidas: ${gamesPlayed}" /> <br/>
		<c:out value="Partidas ganadas: ${gamesWon}" /> <br/>
		<c:out value="Porcentaje de victorias: ${(gamesWon/gamesPlayed)*100}%" /> <br/>
		<c:out value="Tiempo total de juego: ${totalPlayingGame}" /> <br/>
		<c:out value="Duraci�n de la partida m�s larga: ${durationOfTheLongestGame} minutos" />
	</p>
	<br/>
	<table class="table table-striped">
		<caption>Player game history</caption>
		<thead>
			<tr>
				<th style="width: 150px;">Anfitri&oacute;n</th>
				<th style="width: 200px;">Invitado</th>
				<th style="width: 200px;">Ganador</th>
				<th style="width: 200px;">Estad&iacute;sticas de la partida</th>

			</tr>
		</thead>
		<tbody>
			<c:forEach items="${playerMatches}" var="playerMatch">
				<tr>
					<td><c:out value="${playerMatch.jugador1.user.username}" /></td>
					<td><c:out value="${playerMatch.jugador2.user.username}" /></td>
					<td>
						<c:choose>
							<c:when test="${playerMatch.ganadorPartida == 'FIRST_PLAYER'}">
								<c:out value="${playerMatch.jugador1.user.username}" />
							</c:when>
							<c:when test="${playerMatch.ganadorPartida == 'SECOND_PLAYER'}">
								<c:out value="${playerMatch.jugador2.user.username}" />
							</c:when>
							<c:when test="${playerMatch.ganadorPartida == 'DRAW'}">
								<c:out value="EMPATE" />
							</c:when>
						</c:choose>
					</td>
					<td>
						<c:choose>	
							<c:when test="${Boolean.FALSE.equals(playerMatch.abandonada)}">
				                <spring:url value="/matches/{idMatch}/statistics" var="estadisticasUrl">
									<spring:param name="idMatch" value="${playerMatch.id}" />
								</spring:url>
								<a href="${fn:escapeXml(estadisticasUrl)}">Estad&iacute;sticas</a>
							</c:when>
							<c:otherwise>
								<spring:url value="/matches/{idMatch}/abandoned" var="abandonedUrl">
									<spring:param name="idMatch" value="${playerMatch.id}" />
								</spring:url>
								<a href="${fn:escapeXml(abandonedUrl)}">ABANDONO</a>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="seccion botones">
		<a class="button" href="#"
			onclick="javascript:window.history.back(-1);return false;">Volver
			atr&aacute;s</a>
	</div>

	<style type="text/css">
.botones {
	
}

.button:hover {
	text-decoration: none;
	background-color: rgba(25, 25, 30, 0.05);
	color: green;
}

.button {
	text-decoration: none;
	background-color: #EEEEEE;
	color: #333333;
	padding: 2px 6px 2px 6px;
	border-top: 1px solid #CCCCCC;
	border-right: 1px solid #333333;
	border-bottom: 1px solid #333333;
	border-left: 1px solid #CCCCCC;
	border-radius: 5px;
	padding: 5px;
	margin-left: auto;
	margin-right: auto;
}

.seccion {
	display: flex;
	width: 100%;
	justify-content: center;
	align-items: center;
	padding: 10px;
}
</style>


</petclinic:layout>