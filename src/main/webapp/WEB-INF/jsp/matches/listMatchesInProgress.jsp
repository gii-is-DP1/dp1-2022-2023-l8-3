<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="matches in progress">
	
	
    <h2>Partidas en curso</h2>


    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Creador de la partida</th>
            <th style="width: 200px;">Invitado</th>
            <th style="width: 200px;">Contaminacion j1</th>
            <th style="width: 200px;">Contaminacion j2</th>
            
        </tr>
        </thead>
        <tbody>
        	<c:choose>
        		<c:when test="${sinPartidas}"> <div class="alert alert-info">En este momento no hay partidas en juego</div></c:when>
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
			                <td><c:out value="${partida.contaminationNumberOfPlayer1}"/></td>
			                <td><c:out value="${partida.contaminationNumberOfPlayer2}"/></td>
			        	</tr>
			  		</c:forEach>
			  	</c:otherwise>
			</c:choose>
        </tbody>
    </table>
	
	<petclinic:pagination thisPage="${thisPage}" numberOfPages="${numberOfPages}" url="/matches/InProgress/"/>


</petclinic:layout>