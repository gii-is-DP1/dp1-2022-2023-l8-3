<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="friends">
	<h2>Amigos</h2>

	<c:choose>
		<c:when test="${playerFriends.size() == 0}">
			<div><br> <label>You don't have any friends yet!</label></div> <br>
		</c:when>
		<c:otherwise>
			<table id="playerFriends" class="table table-striped">
				<caption>Player friends</caption>
				<thead>
					<tr>
						<th>First name</th>
						<th>Last name</th>
						<th>Username</th>
						<th>Online status</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${playerFriends}" var="friend">
						<tr>
							<td><c:out value="${friend.firstName}" /></td>
							<td><c:out value="${friend.lastName}" /></td>
							<td><c:out value="${friend.user.username}" /></td>
							<td><c:out value="${friend.estadoOnline}" /></td>
							<td>
								<a href="<c:url value="/jugadores/${jugadorId}/playerFriends/${friend.id}/delete" />">
								<span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>
	<div>
		<a class="btn btn-warning" href="<c:url value="/jugadores/friendRequests" />">Solicitudes de amistad</a> 
		<a class="btn btn-warning" href="<c:url value="/jugadores/addFriends" />">Añadir amigos</a>
	</div>

</petclinic:layout>