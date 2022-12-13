<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>


<petclinic:layout pageName="createMatch">


	<h2 style="text-align: center">Nueva Partida</h2>

	<div class="seccion seccion1">

		<div style="text-align: center">
			<label for="check" style="color: blue">ELIGE UN NOMBRE PARA TU PARTIDA, SELECCIONA SU PRIVACIDAD Y A JUGAR!!</label>
		<br>
			<label for="check" style="color:blue">TUS AMIGOS NO SER&Aacute;N INVITADOS HASTA QUE CREES LA PARTIDA</label>
		</div>

	</div>
	<div class=" seccion invitaciones">
		<div class="scroll">
			<p style="font:fantasy; font-style:inherit ; font-size: x-large; text-align: center; justify-content: center; align-items: center;">INVITACIONES</p>
			<table style="width: 100%">
				<thead>
					<th style="text-align: center">AMIGO</th>
					<th style="text-align: center">ESTADO ONLINE</th>
					<th></th>

				</thead>
				<tbody>
		
					<c:forEach items="${players}" var="player">
						<c:if test="${actualPlayer!=player.user.username}">
							<tr>
								<td><c:out value="${player.user.username}" /></td>
								<c:choose>
									<c:when test="${player.estadoOnline==true}"><td><c:out value="En linea"/></td></c:when>
									<c:otherwise><td><c:out value="Desconectado"/></td></c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${amigosInvitados.contains(player)}">
										<td><label for="check" style="color: blue">INVITADO</label></td>
									</c:when>
									<c:otherwise>
										<td><a class="btn btn-warning" href="<c:url value="/invitarAmigo/${player.id}" />">INVITAR AMIGO</a></td>
									</c:otherwise>
								</c:choose>
								
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
		</div>

	</div>
	<form:form method="post">
		<div class="form-group">
			<input class="form-control" value="Nombre Partida" name="nombre">
			<select name="tipoPartida" class="form-control">
				<option value="false">Publica</option>
				<option value="true" selected>Privada</option>

			</select>

			<div class="seccion botones">
				<a class="button" href="#"
					onclick="javascript:window.history.back(-1);return false;">Volver
					atras</a> <input href="<c:url value="/matches/${match.id}/currentMatch" /> class="button" type="submit" value="Crear partida" />
			</div>
		</div>
	</form:form>








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
