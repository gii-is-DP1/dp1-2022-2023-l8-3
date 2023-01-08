<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="403-error">

    <spring:url value="/resources/images/sarcina.png" var="sarcina"/>
    <div class="content">
    <img src="${sarcina}"/>
	</div>
    <h2>Error 403: Prohibido.</h2>
    <p>Tu cliente no tiene permiso para acceder a esta URL.</p>

<style type="text/css">

img{
filter: drop_shadow(2px 4px 8px #585858)

}

</style>


</petclinic:layout>
