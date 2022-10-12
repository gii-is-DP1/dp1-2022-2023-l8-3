<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="jugadores">

    <h2>jugador Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${jugador.firstName} ${jugador.lastName}"/></b></td>
        </tr>
        <tr>
            <th>ID</th>
            <td><c:out value="${jugador.id}"/></td>
        </tr>
  
    </table>

   

</petclinic:layout>