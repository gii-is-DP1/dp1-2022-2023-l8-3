<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="jugadores">

    <h2>Informaci&oacuten sobre jugadores</h2>


    <table class="table table-striped" style="width:40%; align:center">
        <thead>
        <tr>
		 <th style="width: 150px; color:olive">USER</th>
		  <th style="width: 150px; color:olive;">ATRIBUTES</th>
        </tr>
        </thead>
        <tbody>
	        
	            <tr>
					<td>
	                    <c:out value="${jugador.user.username}"/>
	                </td>
	                <th style="width: 120px">Username</th>
	                
				</tr>
				<tr>
	                <td>
	                    <c:out value="${jugador.firstName}"/>
	                </td>
	                <th style="width: 150px;">Nombre</th>
	                
				</tr>
				<tr>
	                <td>
	                    <c:out value="${jugador.lastName}"/>
	                </td>
	                <th style="width: 200px;">Apellido</th>
	                
				</tr>
				<tr>
					<td>
	                    <c:out value="${jugador.bacterias}"/>
	                </td>
	                <th style="width: 120px">Bacterias</th>
	                
				</tr>
				<tr>
					<td>
	                    <c:out value="${jugador.sarcinas}"/>
	                </td>
	                <th style="width: 120px">Sarcinas</th>
	                
				</tr>
				<tr>
	                <td>
	                	<c:choose>
	                		<c:when test="${jugador.estadoOnline}">online</c:when>
	                		<c:otherwise>offline</c:otherwise>
	                    </c:choose>
	                </td>
	                <th style="width: 120px">Estado</th>
	                
				</tr>
        </tbody>
    </table>

   

</petclinic:layout>