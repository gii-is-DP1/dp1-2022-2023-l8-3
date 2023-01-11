<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="500-error">

    <spring:url value="/resources/images/sarcina.png" var="sarcina"/>
    <div class="content">
    <img src="${sarcina}"/>
	</div>
    <h2>Algo sali&oacute; mal!</h2>
    <p>El servidor encontr&oacute; un error temporal y no pudo completar la petici&oacute;n.</p>

<style type="text/css">

img{
filter: drop_shadow(2px 4px 8px #585858)

}

</style>


</petclinic:layout>
