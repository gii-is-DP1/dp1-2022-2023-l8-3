<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">

    <spring:url value="/resources/images/sarcina.png" var="sarcina"/>
    <div class="content">
    <img src="${sarcina}"/>
	</div>
    <h2>Lo siento ${jugador.user.username}</h2><h2>${ mensaje}</h2>

    <p>Podras volver a jugar mañana :)</p>

<style type="text/css">

img{
filter: drop_shadow(2px 4px 8px #585858)

}

</style>


</petclinic:layout>
