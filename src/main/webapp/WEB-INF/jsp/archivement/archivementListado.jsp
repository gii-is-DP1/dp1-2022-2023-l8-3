<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="archivement">
    <h2>Archivement</h2>

    <table id="archivementTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Badgeimage</th>
            <th>Threshold</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${archivement}" var="archivement">
            <tr>
                <td>
                    <c:out value="${archivement.name}"/>
                </td>
                <td>
                    <c:out value="${archivement.description}"/>
                </td>
                <td>
                    <c:if test="${archivement.badgeImage == ''}">none</c:if>
                    <c:if test="${archivement.badgeImage != ''}">
                        <img src="${archivement.badgeImage} " width=100px  /> 
                    </c:if>
                </td>
                <td>
                    <c:out value="${archivement.threshold}"/>
                </td>
                 <td>
                	<a href="<c:url value="/stadistics/archivement/${archivement.id}/edit" />"> <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
                </td>
                <td>
                	<a href="<c:url value="/stadistics/archivement/${archivement.id}/delete" />"> <span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
        <tr>
            <td>
             
            </td>            
        </tr>
    </table>
</petclinic:layout>
