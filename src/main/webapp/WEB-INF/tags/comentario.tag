<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<%@ attribute name="msg" required="true" rtexprvalue="true"
              description="Texto del mensaje" %>
<%@ attribute name="fecha" required="true" rtexprvalue="true"
              description="Fecha del mensaje" %>
<%@ attribute name="jugador" required="true" rtexprvalue="true"
              description="Nombre usuario del jugador" %>

<div class="comentario">
  <div style="width: 20%;">
    <span>${fecha}</span>
    <span>${jugador}:</span>
  </div>
  <textarea  onclick='this.style.height = "";this.style.height = this.scrollHeight + "px"'
   class='textarea' style="width: 80%;" readonly> ${msg}</textarea>
</div>
