<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="addFriends">
    <h2>Add friends</h2>
	
	<form class="form-inline">
		<input type="text" name="keyword" class="form-control" id="keyword" th:value="${keyword}" required>
		<input type="submit" class="btn btn-info mb-2" value="Buscar">
	</form>
	<br>
    <table id="achievementsTable" class="table table-striped">
    	<caption>Search results</caption>
        <thead>	
	        <tr>
	            <th>Username</th>
	            <th></th>
	        </tr>
        </thead>
        <tbody>
	        <c:forEach items="${listPlayers}" var="player">
	            <tr>
	                <td>       
	                    <c:out value="${player.user.username} "/>
	                </td>
	                <td>
	                	<a class="btn btn-success" href="<c:url value="/jugadores/addFriends/${loggedPlayerId}/${player.id}" />">Add friend</a>
	                </td>
	            </tr>
	        </c:forEach>
        </tbody>
    </table>
    
</petclinic:layout>