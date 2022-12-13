<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="achievements">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#birthDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>
            <c:if test="${achievement['new']}">New </c:if> Achievement
        </h2>
        <form:form modelAttribute="achievement"
                   class="form-horizontal">
            <input type="hidden" name="id" value="${achievement.id}"/>
            <div class="form-group has-feedback">                
                <petclinic:inputField label="Name" name="name"/>
                <petclinic:inputField label="Description" name="description"/>
        		<petclinic:selectField name="metrics" label="Metrics" names="${metrics}" size="4"/>
                <petclinic:inputField label="Threshold" name="threshold"/>
                <petclinic:selectField name="difficulty" label="Difficulty" names="${difficulty}" size="3"/>
                 <petclinic:selectField name="visibility" label="Visibility" names="${visibility}" size="3"/>
                
              
                
				
                <!-- como se haria el visibility con un checkboxs -->                
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${achievement['new']}">
                            <button class="btn btn-default" type="submit">Add Achievement</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Update Achievement</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>        
    </jsp:body>

</petclinic:layout>

<style type="text/css">

#labelBox{
text-align: right;

}




</style>