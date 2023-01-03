<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<style>
@import url('https://fonts.googleapis.com/css2?family=Press+Start+2P&display=swap');</style>



<petclinic:layout pageName="waitForMatch">

	<h2 id="partida">ENTRANDO A LA PARTIDA: ${match.name}</h2>
	<c:choose>
	<c:when test="${EresJugador1}">
	<h2 id="partida">Paciencia, estamos esperando al 2º jugador</h2>
	</c:when>
	<c:otherwise>
	<h2 id="partida">¿QUIERES SER EL SEGUNDO JUGADOR?</h2>
	</c:otherwise>
	</c:choose>
	<form:form method="post">
		<div id="partida">

		<!--	<c:choose>
				<c:when test="${match.esPrivada}">
					<c:out value="PARTIDA PRIVADA" />
				</c:when>
				<c:otherwise>
					<c:out value="PARTIDA PUBLICA" />
				</c:otherwise>
			</c:choose> -->
		</div>
		

		

		<c:choose>
			<c:when test="${!EresJugador1}">

				
					
					<input type="submit" class="btn-3d"
					value="ENTRAR EN LA PARTIDA">
			</c:when>
			<c:otherwise>
				<div class="centrado">
					<div class="loader"></div>

				</div>
			</c:otherwise>
		</c:choose>
		<c:if test="${EresJugador1}">
			<div><a class="button" id="abandonar" href="<c:url value="/matches/${match.id}/abandonedWaitMatch" />">Abandonar partida</a></div>
		</c:if>

	</form:form>



	<style type="text/css">
.centrado {
	display: flex;
	justify-content: center;
}

.loader {
	border: 16px solid #f3f3f3; /* Light grey */
	border-top: 16px solid #3498db; /* Blue */
	border-radius: 50%;
	width: 120px;
	height: 120px;
	animation: spin 2s linear infinite;
}


@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
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

#partida {

	position: relative;
	text-align: center;
	font-size: x-large;
	font-family: 'Press Start 2P', cursive;
	animation-name: anim;
	animation-duration: 5s;
	animation-iteration-count: infinite;
}

#abandonar{
	display: flex;
	width:15%;
	justify-content: center;
	margin-top: 3%;
}


@keyframes anim{
    0% {color: blue;} /*Amarillo*/
   25% {color: purple;} /*Naranja*/
   50% {color: red;} /*Negro*/
   75% {color: black;} /*Otra vez naranja*/
  100% {color: blue;} /*Otra vez amarillo*/
}

.btn-3d {
  padding: .6rem 1rem;
  border: 1px solid #995309;
  border-radius: 4px;
  background-color: #d9750b;
  color: #fff;
  width:100%;
  height:50px;
  margin:20px;

  font-size: 1.5rem;
  text-shadow: 0 -1px 0 rgba(0,0,0,.5);
  box-shadow: 0 1px 0 rgba(255,255,255,.5) inset,
    0 1px 3px rgba(0,0,0,.2);
  background-image: -webkit-gradient(linear,left top,left bottom,color-stop(10%,#f90),to(#e76a00));
  background-image: linear-gradient(#f90 10%,#e76a00 100%);
}

.btn-3d:hover, .btn-3d:focus {
  background-color: #e0811b;
  background-image: -webkit-gradient(linear,left top,left bottom,color-stop(10%,#f0a100),to(#f70));
  background-image: linear-gradient(#f0a100 10%,#f70 100%);
}

.btn-3d:active {
  background-color: #cf6a00;
  box-shadow: 0 2px 3px 0 rgba(0,0,0,.2) inset;
  background-image: -webkit-gradient(linear,left top,left bottom,color-stop(10%,#f0a100),to(#f70));
  background-image: linear-gradient(#f0a100 10%,#f70 100%);
}

</style>



</petclinic:layout>
