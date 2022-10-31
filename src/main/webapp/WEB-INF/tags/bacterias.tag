<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of corresponding property in bean object" %>
<%@ attribute name="divJugador1" required="true" type="java.lang.Boolean"
              description="Indica si el div pertenece a las bacterias del jugador 1 (true) o al jugador 2 (false)" %>
<%@ attribute name="numeroDisco" required="true" rtexprvalue="true"
              description="Numero del disco" %>
<%@ attribute name="indexBacteria" required="true" rtexprvalue="true"
              description="Numero del disco" %>
<%@ attribute name="turnoJugador1" required="true" rtexprvalue="true" type="java.lang.Boolean"
              description="Indica si es el turno del jugador 1" %>

<spring:bind path="${name}">

  <c:set var="jugador" value="${divJugador1 ? 'J1' : 'J2'}"/>
  <c:set var="idCheckbox" value="${jugador}-bacteria${indexBacteria}disco${numeroDisco}"/>
  <c:set var="valueCheckbox" value="${jugador}-D${numeroDisco}-B${indexBacteria}"/>

  <c:choose>
    <c:when test="${turnoJugador1 && jugador == 'J1'}">
      <div>
        <form:checkbox path="${name}" id="${idCheckbox}" value="${valueCheckbox}"/>
        <label for="${idCheckbox}" class="bacteria pointer">${indexBacteria}</label>
      </div>
    </c:when>

    <c:when test="${!turnoJugador1 && jugador == 'J2'}">
      <div>
        <form:checkbox path="${name}" id="${idCheckbox}" value="${valueCheckbox}"/>
        <label for="${idCheckbox}" class="bacteria pointer">${indexBacteria}</label>
      </div>
    </c:when>

    <c:otherwise>
      <div class="bacteria"></div>
    </c:otherwise>
  </c:choose>

</spring:bind>
