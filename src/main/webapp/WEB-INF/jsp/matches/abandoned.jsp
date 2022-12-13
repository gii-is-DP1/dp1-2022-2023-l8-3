<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="abandono">

  <h1>LA PARTIDA NO PUDO TERMINAR SATISFACTORIAMENTE, EL GANADOR ES: <b>${winner.user.username}</b>, YA QUE EL ORTRO JUGADOR ABANDONÓ</h1>
   

</petclinic:layout>