<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="partidas">

    <h2>Información sobre jugadores</h2>


    <table class="table table-striped">
    	<caption>Hola</caption>
        <thead>
        <tr>
            <th style="width: 150px;">Nombre</th>
            <th style="width: 200px;">Apellido</th>
            <th style="width: 120px">Estado</th>
        </tr>
        </thead>
        <tbody>
	        <c:forEach items="${selections}" var="jugador">
	            <tr>
	                
	                <td>
	                    <spring:url value="jugadores/{jugadorId}" var="EnlaceUrl">
        					<spring:param name="jugadorId" value="${jugador.id}"/>
    					</spring:url>
    					<a href="${fn:escapeXml(EnlaceUrl)}"><c:out value="${jugador.firstName}"/></a>
	                </td>
	                <td>
	                    <c:out value="${jugador.lastName}"/>
	                </td>
	                <td>
	                	<c:choose>
	                		<c:when test="${jugador.estadoOnline}">Online</c:when>
	                		<c:otherwise>Offline</c:otherwise>
	                    </c:choose>
	                </td>
	        	</tr>
	  		</c:forEach>
        </tbody>
    </table>
	<a class="btn btn-default" href='<spring:url value="/jugadores/new" htmlEscape="true"/>'>Añadir Jugador</a>
   

</petclinic:layout>