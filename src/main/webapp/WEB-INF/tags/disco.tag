<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<%@ attribute name="clase" required="true" rtexprvalue="true"
              description="Clase de div disco" %>
<%@ attribute name="indexDisco" required="true" rtexprvalue="true"
              description="Numero del disco" %>
<%@ attribute name="idLoggedPlayer" required="true" rtexprvalue="true"
              description="Jugador logueado" %>
<%@ attribute name="idCurrentPlayer" required="true" rtexprvalue="true"
              description="Jugador al que le toca mover bacterias" %>
<%@ attribute name="itIsPropagationPhase" required="true" rtexprvalue="true"
              description="Devuelve si es la fase de propagación o no" %>

<c:set var="esteDisco" value="${match.getDisco(indexDisco)}"/>
<c:set var="numeroDisco" value="${indexDisco + 1}"/>

<div class="${clase}">

  <label for="disco${numeroDisco}" class="pointer discoLabel">
    ${numeroDisco}
    <c:if test="${(idLoggedPlayer == idCurrentPlayer && itIsPropagationPhase)}">
    		<form:checkbox class="checkbox" path="deDisco" id="disco${numeroDisco}" value="${numeroDisco}" onchange="toggleCheckbox(this)"/>
    </c:if>
    
  </label>

  <div class="bs1">
    <c:forEach var="b1" begin="1" end="${esteDisco.numBact1}">
      <div class="bacteria"></div>
    </c:forEach>

    <c:forEach var="s1" begin="1" end="${esteDisco.numSarc1}">
      <div class="sarcina"></div>
    </c:forEach>
  </div>

  <div class="bs2">
    <c:forEach var="b2" begin="1" end="${esteDisco.numBact2}">
      <div class="bacteria"></div>
    </c:forEach>

    <c:forEach var="s2" begin="1" end="${esteDisco.numSarc2}">
      <div class="sarcina"></div>
    </c:forEach>
  </div>

  <div class="">
    <label for="TAdisco${numeroDisco}" class="taLabel noDisplay">
      <input class="inputs" type="number" min="0" max="4" value="0" name="disco${numeroDisco}" id="TAdisco${numeroDisco}" />
    </label>
  </div>

</div>

