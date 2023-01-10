<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="matchStatistics">
    <h2>Match statistics</h2> <br/>

	<c:if test="${match.ganadorPartida == 'FIRST_PLAYER'}">
	    <c:if test="${match.jugador1.user.username != null}">
	    	<h3>¡HA GANADO ${match.jugador1.user.username}!</h3> <br/>
		</c:if>
		<c:if test="${match.jugador1.user.username == null}">
			<h3>¡HA GANADO EL JUGADOR ANFITRIÓN!</h3> <br/>
		</c:if>
	</c:if>
	<c:if test="${match.ganadorPartida == 'SECOND_PLAYER'}">
		<c:if test="${match.jugador2.user.username != null}">
			<h3>¡HA GANADO ${match.jugador2.user.username}!</h3> <br/>
		</c:if>
		<c:if test="${match.jugador2.user.username == null}">
			<h3>¡HA GANADO EL JUGADOR INVITADO!</h3> <br/>
		</c:if>
	</c:if>
	<c:if test="${match.ganadorPartida == 'DRAW'}">
		<h3>¡EMPATE!</h3> <br/>
	</c:if>
	<c:if test="${match.jugador1.user.username != null}">
    	<p><b>${match.jugador1.user.username}:</b></p>
	</c:if>
	<c:if test="${match.jugador1.user.username == null}">
		<p><b>Anfitrión:</b></p>
	</c:if>
	<p>Número de contaminación: ${match.contaminationNumberOfPlayer1}</p>
	<p>Número de bacterias en la reserva: ${match.numberOfBacteriaOfPlayer1}</p>
	<p>Número de sarcinas en la reserva: ${match.numberOfSarcinaOfPlayer1}</p> <br/>
	<c:if test="${match.jugador2.user.username != null}">
		<p><b>${match.jugador2.user.username}:</b></p>
	</c:if>
	<c:if test="${match.jugador2.user.username == null}">
		<p><b>Invitado:</b></p>
	</c:if>
	<p>Número de contaminación: ${match.contaminationNumberOfPlayer2}</p>
	<p>Número de bacterias en la reserva: ${match.numberOfBacteriaOfPlayer2}</p>
	<p>Número de sarcinas en la reserva: ${match.numberOfSarcinaOfPlayer2}</p> <br/>
	<p><b>Algunos datos adicionales de la partida:</b></p>
	<p>La partida ha durado ${match.durationInMinutes()} minutos</p>
	<p>Se han realizado un total de ${match.totalMoves()} movimientos</p>
	<p>El disco número ${match.dishWithMoreMovements()[0]+1} ha sido el más popular de la partida, con ${match.dishWithMoreMovements()[1]} movimientos</p> <br/>
    
	<a class="btn btn-default" href="/">Back to Main Menu</a>
</petclinic:layout>