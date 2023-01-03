<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<%@ attribute name="thisPage" required="true" rtexprvalue="true"
              description="Numero de pagina actual" %>
<%@ attribute name="numberOfPages" required="true" rtexprvalue="true"
              description="Numero de paginas totales" %>
<%@ attribute name="url" required="true" rtexprvalue="true"
              description="url destino" %> 

<div class="paginatin">

  <c:choose>
    <c:when test="${thisPage > 1}">
      <a href="${url}${thisPage - 1}">&laquo;</a>
    </c:when>
    <c:otherwise>
      <a href="#">&laquo;</a>
    </c:otherwise>
  </c:choose>
  
  <c:forEach var="page" begin="1" end="${numberOfPages}">

    <c:choose>
      <c:when test="${page == thisPage}">
        <a href="#"><c:out value="${page}"/></a>
      </c:when>
      <c:otherwise>
        <a href="${url}${page}"><c:out value="${page}"/></a>
      </c:otherwise>
    </c:choose>

  </c:forEach>

  <c:choose>
    <c:when test="${thisPage < numberOfPages}">
      <a href="${url}${thisPage + 1}">&raquo;</a>
    </c:when>
    <c:otherwise>
      <a href="#">&raquo;</a>
    </c:otherwise>
  </c:choose>

</div>				  

