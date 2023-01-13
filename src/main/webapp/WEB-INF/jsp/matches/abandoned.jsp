<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="abandono">

	<c:if test="${match.ganadorPartida == 'FIRST_PLAYER'}">
	    <c:if test="${match.jugador1.user.username != null}">
	    	<h1>EL GANADOR ES: <b>${winner.user.username}</b>, YA QUE EL JUGADOR INVITADO ABANDONÓ</h1>
		</c:if>
		<c:if test="${match.jugador1.user.username == null}">
			<h1>EL GANADOR ES EL JUGADOR ANFITRIÓN, YA QUE EL JUGADOR INVITADO ABANDONÓ</h1>
		</c:if>
	</c:if>
	<c:if test="${match.ganadorPartida == 'SECOND_PLAYER'}">
		<c:if test="${match.jugador2.user.username != null}">
			<h1>EL GANADOR ES: <b>${winner.user.username}</b>, YA QUE EL JUGADOR ANFITRIÓN ABANDONÓ</h1>
		</c:if>
		<c:if test="${match.jugador2.user.username == null}">
			<h1>EL GANADOR ES EL JUGADOR INVITADO, YA QUE EL JUGADOR ANFITRIÓN ABANDONÓ</h1>
		</c:if>
	</c:if>   

</petclinic:layout>