<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="matches finished">
	
	
    <h2>Partidas finalizadas</h2>
    
    <p>
		<c:out value="Total de partidas: ${gamesPlayed}" /> <br/>
	</p>
	<br/>
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Creador de la partida</th>
            <th style="width: 200px;">Invitado</th>
            <th style="width: 200px;">Ganador</th>
            <th style="width: 200px;">Estad&iacute;sticas de la partida</th>
            
        </tr>
        </thead>
        <tbody>
        	<c:choose>
        		<c:when test="${sinPartidas}"> <div class="alert alert-info">En este momento no hay partidas que ya hayan sido jugadas</div></c:when>
        		<c:otherwise>
			        <c:forEach items="${selections}" var="partida">
			            <tr>  
			                <td>
			                    <spring:url value="/jugadores/{jugadorId}" var="EnlaceUrl">
		        					<spring:param name="jugadorId" value="${partida.jugador1.id}"/>
		    					</spring:url>
		    					<a href="${fn:escapeXml(EnlaceUrl)}"><c:out value="${partida.jugador1.user.username}"/></a>
			                </td>
			                <td>
			                    <spring:url value="/jugadores/{jugadorId}" var="EnlaceUrl">
		        					<spring:param name="jugadorId" value="${partida.jugador2.id}"/>
		    					</spring:url>
		    					<a href="${fn:escapeXml(EnlaceUrl)}"><c:out value="${partida.jugador2.user.username}"/></a>
			                </td>
			                <td>
			                	<c:choose>
			                		<c:when test="${partida.ganadorPartida==firstPlayer}"><c:out value="${partida.jugador1.user.username}"/></c:when>
			                		<c:otherwise><c:out value="${partida.jugador2.user.username}"/></c:otherwise>
			                    </c:choose>
			                </td>
			                <td>
								<c:choose>
								
								<c:when test="${partida.abandonada==false}">
			                	<spring:url value="/matches/{idMatch}/statistics" var="estadisticasUrl">
									<spring:param name="idMatch" value="${partida.id}" />
								</spring:url>
								<a href="${fn:escapeXml(estadisticasUrl)}">Estad&iacute;sticas</a>
								</c:when>
								<c:otherwise>
								<spring:url value="/matches/{idMatch}/abandoned" var="abandonedUrl">
									<spring:param name="idMatch" value="${partida.id}" />
								</spring:url>
								<a href="${fn:escapeXml(abandonedUrl)}">ABANDONO</a>
								</c:otherwise>
								</c:choose>
			                </td>
			        	</tr>
			  		</c:forEach>
				</c:otherwise>
			</c:choose>
        </tbody>
    </table>

	<petclinic:pagination thisPage="${thisPage}" numberOfPages="${numberOfPages}" url="/matches/Finished/"/>

</petclinic:layout>