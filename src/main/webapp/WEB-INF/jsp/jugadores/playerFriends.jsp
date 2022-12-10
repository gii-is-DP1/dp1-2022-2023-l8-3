<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="friends">
	<h2>Amigos</h2>

	<table id="playerFriends" class="table table-striped">
		<tbody>				
			<div>
				<c:forEach items="${playerFriends}" var="friend">
					<tr>
						<td><c:out value="${friend.user.username}" /></td>
					</tr>
				</c:forEach>
			</div>					
		</tbody>
	</table>
	<div>
		<a class="btn btn-danger" href="<c:url value="/jugadores/${jugadorId}/friendRequests}" />">Solicitudes de amistad</a>
		<a class="btn btn-danger" href="<c:url value="/jugadores/addFriends" />">Añadir amigos</a>
	</div>

</petclinic:layout>