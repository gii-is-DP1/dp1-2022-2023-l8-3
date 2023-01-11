<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="achievements">
    <h2>Achievements</h2>

    <table id="achievementsTable" class="table table-striped">
    	<caption>Achievement list</caption>
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Difficulty</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${achievements}" var="achievement">
            <tr>
                <td>
                    <c:out value="${achievement.name}"/>
                </td>
                <td>                    
                      <c:out value="${achievement.actualDescription} "/>                                        
                </td>
                <td>       
                    <c:out value="${achievement.difficulty} "/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <petclinic:pagination thisPage="${thisPage}" numberOfPages="${numberOfPages}" url="${url}"/>

    
</petclinic:layout>