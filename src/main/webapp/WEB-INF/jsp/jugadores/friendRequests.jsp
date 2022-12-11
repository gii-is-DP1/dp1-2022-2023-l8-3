<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="friendRequests">
	<h2>Friend requests</h2>

	<c:choose>
		<c:when test="${listPlayers.size() == 0}">
			<div><br> <label>You have not received a friend request!</label></div> <br>
		</c:when>
		<c:otherwise>
			<table id="friendRequests" class="table table-striped">
				<tbody>
					<div>
						<c:forEach items="${listPlayers}" var="player">
							<tr>
								<td><c:out value="${player.user.username}" /></td>
								<td>
									<a class="btn btn-success" href="<c:url value="/jugadores/friendRequests/${player.id}/${loggedPlayerId}/${true}" />">
									Accept</a>
									<a class="btn btn-danger" href="<c:url value="/jugadores/friendRequests/${player.id}/${loggedPlayerId}/${false}" />">
									Decline</a>
								</td>
							</tr>
						</c:forEach>
					</div>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>
	
</petclinic:layout>