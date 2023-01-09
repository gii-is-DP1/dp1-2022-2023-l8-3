<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

<petclinic:layout pageName="home">
    <h2><fmt:message key="welcome"/></h2>
    <div class="row">
        <div class="col-md-12" style="display: flex; justify-content: center;">
        	<spring:url value="/resources/images/petris.png" htmlEscape="true" var="petsImage"/>
            <img class="img-responsive" src="${petsImage}"/>
        </div>
        <h4>Ranking global de victorias:</h4>
        <div class="col-md-12" style="display: flex; justify-content: center;">
			<table id="players" class="table table-striped">
				<thead>
					<tr>
						<th class="col-md-3">Nombre de usuario</th>
						<th class="col-md-3">N&uacute;mero de victorias</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${players}" var="player">
						<tr>
							<td><c:out value="${player.user.username}" /></td>
							<td><c:out value="${player.getNumberOfGamesWon()}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
        </div>
    </div>
</petclinic:layout>
