<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="invitations">
    <h2>Invitaciones a partidas de amigos:</h2>
    <c:if test="${yaHayJugador}">
	    <div class="alert alert-info">
			La partida a la que le han invitado como jugador ya esta llena
		</div>
	</c:if>
	<c:if test="${partidaAunNoHaComenzado}">
	    <div class="alert alert-info">
			La partida a la que intenta unirse como espectador a&uacute;n no ha comenzado, por favor vuelva a intentarlo m&aacute;s tarde
		</div>
	</c:if>
	<c:if test="${maxNumeroEspectadores}">
	    <div class="alert alert-info">
			La partida a la que le han invitado como espectador tiene el n&uacute;mero m&aacute;ximo de espectadores
		</div>
	</c:if>
    <table id="invitationTable" class="table table-striped">
    	<caption>Here you will find the invitations, as player and guest, that the player has received</caption>
        <thead>
        <tr>
            <th>Amigo</th>
            <th>Tipo de invitaci&oacute;n</th>
            <th>Nombre de la partida</th>
            <th class="col-md-3"></th>
            <th class="col-md-3"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${invitaciones}" var="invitacion">
            <tr>
                <td>
                    <c:out value="${invitacion.match.jugador1.firstName}"/>
                </td>
                <td>
                	<b style="color: blue"><c:out value="${invitacion.tipo}"/></b>
                </td>
                <td>
                	<c:out value="${invitacion.match.name}"/>
                </td>
                <td>       
                    <form:form action="" method="post">
						<a class="btn btn-success" href="<c:url value="/aceptarInvitacion/${invitacion.id}" />">UNIRME</a>
					</form:form>
                </td>
                <td>
                	<a href="/rechazarInvitacion/${invitacion.id}" class="btn btn-danger">Rechazar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
</petclinic:layout>