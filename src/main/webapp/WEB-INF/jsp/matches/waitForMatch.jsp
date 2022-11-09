<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>


<petclinic:layout pageName="currentMatch">

	<h2>Esperando a 2º jugador</h2>
	<h2>${match.id}</h2>

	<div>
		<label for="check" style="color: blue">Partida privada</label>
	</div>
	<div>
		<label for="check" style="color: red">${player}</label>
	</div>
	<div>
		<label for="check" style="color: green">${match.inicioPartida}</label>

	</div>

	<a class="button" href="<c:url value="/matches/${match.id}/currentMatch" />"></a>
	



	<style type="text/css">
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
</style>



</petclinic:layout>
