<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<%@ attribute name="clase" required="true" rtexprvalue="true"
              description="Clase de div disco" %>
<%@ attribute name="indexDisco" required="true" rtexprvalue="true"
              description="Numero del disco" %>


<c:set var="arrayDiscos" value="${match.discos[indexDisco]}"/>
<c:set var="numeroDisco" value="${indexDisco + 1}"/>

<div class="${clase}">
  <form:checkbox path="aDisco"  id="disco${numeroDisco}" value="D${numeroDisco}"/>
  <label for="disco${numeroDisco}" class="pointer">a</label>

  <div class="bs1">
    <c:forEach var="b1" begin="1" end="${arrayDiscos.numBact1}">

      <petclinic:bacterias name="bacteriasAmover" divJugador1="${true}" turnoJugador1="${match.turnoJugador1}"
        numeroDisco="${numeroDisco}" indexBacteria="${b1}"/>

    </c:forEach>
    <c:forEach var="s1" begin="1" end="${arrayDiscos.numSarc1}">
      <div class="sarcina"></div>
    </c:forEach>
  </div>

  <div class="bs2">
    <c:forEach var="b2" begin="1" end="${arrayDiscos.numBact2}">

      <petclinic:bacterias name="bacteriasAmover" divJugador1="${false}" turnoJugador1="${match.turnoJugador1}"
        numeroDisco="${numeroDisco}" indexBacteria="${b1}"/>

    </c:forEach>
    <c:forEach var="s2" begin="1" end="${arrayDiscos.numSarc2}">
      <div class="sarcina"></div>
    </c:forEach>
  </div>
</div>
