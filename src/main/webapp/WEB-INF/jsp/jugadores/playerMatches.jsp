<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="playerMatches">
	<h2>&Uacute;ltimas partidas</h2>
	<div class="table">
		<table id="playerMatchesTable" class="table table-striped">
			<thead>
				<tr>
					<th>Anfitri&oacute;n</th>
					<th>Invitado</th>
					<th>Ganador</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${playerMatches}" var="playerMatch">
					<tr>
						<td><c:out value="${playerMatch.jugador1.user.username}" /></td>
						<td><c:out value="${playerMatch.jugador2.user.username}" /></td>
						<td><c:out value="${playerMatch.ganadorPartida}" /></td>
						<td><a class="btn btn-warning"
							href="<c:url value="/matches/${playerMatch.id}/statistics" />">Ver
								estad&iacute;sticas</a></td>
					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>
	<div class="seccion botones">
		<a class="button" href="#"
			onclick="javascript:window.history.back(-1);return false;">Volver
			&aacute;tras</a>
	</div>

</petclinic:layout>


<script type="text/javascript">
	
</script>

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

.contenido {
	display: flex;
	align-items: center;
	flex-direction: column;
}

.seccion {
	display: flex;
	width: 100%;
	justify-content: center;
	align-items: center;
	padding: 10px;
}

.table {
	background-color: #FFFFE0;
}

div.scroll {
	background-color: #fed9ff;
	width: 70%;
	height: 40vh;
	overflow-x: hidden;
	overflow-y: auto;
	text-align: center;
	padding: 20px;
}
</style>
