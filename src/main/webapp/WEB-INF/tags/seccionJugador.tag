<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="usuario" required="true" rtexprvalue="true"
              description="Usuario del jugador" %>
<%@ attribute name="numeroBacterias" required="true" rtexprvalue="true"
              description="Numero de bacterias del jugador" %>
<%@ attribute name="numeroSarcinas" required="true" rtexprvalue="true"
              description="Numero de sarcinas del jugador" %>
<%@ attribute name="contaminacion" required="true" rtexprvalue="true"
              description="Contaminacion del jugador" %>


<h4>${usuario}</h4>

<div class="bacterias">
  <h4>N. bacterias: ${numeroBacterias}</h4>
  <c:forEach var="i" begin="1" end="${numeroBacterias}" >
    <div class="smallbacteria"></div>
  </c:forEach>
</div>

<div class="sarcinas">
  <h4>N. sarcinas: ${numeroSarcinas}</h4>
  <c:forEach var="i" begin="1" end="${numeroSarcinas}" >
    <div class="smallsarcina"></div>
  </c:forEach>
</div>

<h4>Contaminacion: ${contaminacion}</h4>
