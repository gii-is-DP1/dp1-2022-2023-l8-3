<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="jugadorDetails">

    <h2>Información del jugador</h2>


    <table class="table table-striped">
        <tr>
            <th>Usuario</th>
            <td><b><c:out value="${jugador.user.username}"/></b></td>
        </tr>
        <tr>
            <th>Nombre</th>
            <td><c:out value="${jugador.firstName}"/></td>
        </tr>
        <tr>
            <th>Apellido</th>
            <td><c:out value="${jugador.lastName}"/></td>
        </tr>
        <tr>
            <th>Email</th>
            <td><c:out value="${jugador.user.email}"/></td>
        </tr>
        <tr>
            <th>Contraseña</th>
            <td><c:out value="${jugador.user.password}"/></td>
        </tr>
        <tr>
            <th>Estado</th>
            <td>
            <c:choose>
	       		<c:when test="${jugador.estadoOnline}">Online</c:when>
	       		<c:otherwise>Offline</c:otherwise>
	      	</c:choose>
	      	</td>
        </tr>
    </table>

    <spring:url value="{jugadorId}/edit" var="editUrl">
        <spring:param name="jugadorId" value="${jugador.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar jugador</a>

    
    <spring:url value="{jugadorId}/delete" var="DeleteUrl">
        <spring:param name="jugadorId" value="${jugador.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(DeleteUrl)}" class="btn btn-default">Borrar jugador</a>
    
</petclinic:layout>