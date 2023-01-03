<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="matchStatistics">
    <h2>Match statistics</h2> <br/>

    <table id="achievementsTable" class="table table-striped">
        <thead>
        <tr>
        	<c:if test="${match.ganadorPartida == 'FIRST_PLAYER'}">
            	<h3>¡HA GANADO ${match.jugador1.user.username}!</h3> <br/>
            </c:if>
            <c:if test="${match.ganadorPartida == 'SECOND_PLAYER'}">
            	<h3>¡HA GANADO ${match.jugador2.user.username}!</h3> <br/>
            </c:if>
            <c:if test="${match.ganadorPartida == 'DRAW'}">
            	<h3>¡EMPATE!</h3> <br/>
            </c:if>
        </tr>
        </thead>
        <tbody>
        	<div>
        		<p><b>${match.jugador1.user.username}:</b></p>
        		<p>Número de contaminación: ${match.jugador1.numeroDeContaminacion}</p>
        		<p>Número de bacterias en la reserva: ${match.jugador1.bacterias}</p>
        		<p>Número de sarcinas en la reserva: ${match.jugador1.sarcinas}</p> <br/>
        	</div>
        	<div>
        		<p><b>${match.jugador2.user.username}:</b></p>
        		<p>Número de contaminación: ${match.jugador2.numeroDeContaminacion}</p>
        		<p>Número de bacterias en la reserva: ${match.jugador2.bacterias}</p>
        		<p>Número de sarcinas en la reserva: ${match.jugador2.sarcinas}</p> <br/>
        	</div>
        	<div>
        		<p><b>Algunos datos adicionales de la partida:</b></p>
        		<p>La partida ha durado ${match.durationInMinutes()} minutos</p>
        		<p>Se han realizado un total de ${match.totalMoves()} movimientos</p>
        		<p>El disco número ${match.dishWithMoreMovements()[0]+1} ha sido el más popular de la partida, con ${match.dishWithMoreMovements()[1]} movimientos</p>
        	</div>
        </tbody>
    </table>
    
	<a class="btn btn-default" href="/">Back to Main Menu</a>

</petclinic:layout>