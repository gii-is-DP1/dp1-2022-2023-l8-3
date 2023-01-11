<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="404-error">

    <spring:url value="/resources/images/sarcina.png" var="sarcina"/>
    <div class="content">
    <img src="${sarcina}"/>
	</div>
    <h2>La p&aacute;gina no se ha encontrado.</h2>
    <p>La p&aacute;gina solicitada no est&aacute; disponible.</p>

<style type="text/css">

img{
filter: drop_shadow(2px 4px 8px #585858)

}

</style>


</petclinic:layout>
