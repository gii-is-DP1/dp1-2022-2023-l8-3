<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<petclinic:layout pageName="matchesList">
	<h2>Partidas</h2>


	<table id="matchesList" class="table table-striped">
		<thead>
			<tr>
				<th class="col-md-3">Anfitrion</th>
				<th class="col-md-3">Nombre Partida</th>
				<th class="col-md-3">Fecha Inicio</th>
				<th class="col-md-3"></th>
			</tr>
		</thead>
		<tbody>

			<c:forEach items="${match_list}" var="match">


				<tr>
					<form:form action="" method="post">
						<td><c:out value="${match.jugador1.user.username}" /></td>
						<td><c:out value="${match.name}" /></td>
						<td><c:out value="${match.inicioPartida}" /></td>
						<td><a class="btn btn-success" href="<c:url value="/matches/${match.id}/waitForMatch" />">UNIRME</a></td>
					</form:form>
				</tr>
			</c:forEach>


		</tbody>

	</table>

	<h2>Partidas como espectador</h2>

	<table id="matches" class="table table-striped">
		<thead>
			<tr>
				<th class="col-md-3">Anfitrion</th>
				<th class="col-md-3">Jugador 2</th>
				<th class="col-md-3">Fecha Inicio</th>
				<th class="col-md-3"></th>
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
	
	<div class="divBoton3d"">
	<button type="button" class="btn-3d" onclick="window.location.href='./Finished'">Historial de partidas</button>
	</div>
	
	<style type="text/css">
	th{
	text-align: center;
	
	}
	td{
	text-align: center;
	}
	
	
	.divBoton3d{
	display: flex;
	justify-content: center;
	align-items: center;
	
	}
	
	.btn-3d {
  padding: .6rem 1rem;
  border: 1px solid #995309;
  border-radius: 4px;
  background-color: #AEFF00;
  color: #fff;
  width:100%;
  height:50px;
  margin:20px;
  display: flex;
  
  justify-content:center;
  align-items:center;
  font-size: 1.5rem;
  text-shadow: 0 -1px 0 rgba(0,0,0,.5);
  box-shadow: 0 1px 0 rgba(255,255,255,.5) inset,
    0 1px 3px rgba(0,0,0,.2);
  background-image: -webkit-gradient(linear,left top,left bottom,color-stop(10%,#AEFF00),to(#AEFF00));
  background-image: linear-gradient(#f90 10%,#e76a00 100%);
}

.btn-3d:hover, .btn-3d:focus {
  background-color: #AEFF00;
  background-image: -webkit-gradient(linear,left top,left bottom,color-stop(10%,#f0a100),to(#AEFF00));
  background-image: linear-gradient(#f0a100 10%,#f70 100%);
}

.btn-3d:active {
  background-color: #AEFF00;
  box-shadow: 0 2px 3px 0 rgba(0,0,0,.2) inset;
  background-image: -webkit-gradient(linear,left top,left bottom,color-stop(10%,#f0a100),to(#AEFF00));
  background-image: linear-gradient(#f0a100 10%,#f70 100%);
}
	
	</style>
	
	
	
</petclinic:layout>