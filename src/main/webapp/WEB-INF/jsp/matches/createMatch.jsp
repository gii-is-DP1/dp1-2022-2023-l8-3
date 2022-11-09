<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>


<petclinic:layout pageName="createMatch">

	<form:form class="contenido" modelAttribute="match">
		<h2>Nueva Partida</h2>

		<div class="seccion seccion1">
			<!--	Esto para cuando se haga
			<form:checkbox path="esPrivada" id="check"/>-->
			<div>
				<label for="check" style="color: blue">Partida privada</label>
			</div>
			<div>
				<label for="check" style="color: red">${player}</label>
			</div>
			<div>
				<label for="check" style="color: green">${match}</label>

			</div>
		</div>

		<div class=" seccion invitaciones">
			<div class="scroll">
				<h4>Invitaciones</h4>
				<table style="width: 100%">
					<thead>
						<th>Amigo</th>
						<th>EstadoOnline</th>
						<th></th>

					</thead>
					<tbody>

						<c:forEach items="${players}" var="player">
							<tr>
								<td><c:out value="${player.user.username}" /></td>
								<td><c:out value="${player.estadoOnline}" /></td>
								<td><a class="btn btn-warning"
									href="<c:url value="#" />">INVITAR AMIGO</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

		</div>

		<div class="seccion botones">
			<a class="button" href="#"
				onclick="javascript:window.history.back(-1);return false;">Volver
				atras</a> <input type="hidden" name="idMatch" value="${match.id}" /> <input
				class="button" type="submit" value="Crear partida" />
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
