<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <h2>
        <c:if test="${owner['new']}"><fmt:message key="newM"/></c:if><fmt:message key="owner"/>
    </h2>
    <form:form modelAttribute="owner" class="form-horizontal" id="add-owner-form">
        <div class="form-group has-feedback">
            <fmt:message key="firstName" var="firstNameLabel"/>
            <petclinic:inputField label="${firstNameLabel}" name="firstName"/>
            <fmt:message key="lastName" var="lastNameLabel"/>
            <petclinic:inputField label="${lastNameLabel}" name="lastName"/>
            <fmt:message key="address" var="addressLabel"/>
            <petclinic:inputField label="${addressLabel}" name="address"/>
            <fmt:message key="city" var="cityLabel"/>
            <petclinic:inputField label="${cityLabel}" name="city"/>
            <fmt:message key="telephone" var="telephoneLabel"/>
            <petclinic:inputField label="${telephoneLabel}" name="telephone"/>
            <fmt:message key="username" var="usernameLabel"/>
            <petclinic:inputField label="${usernameLabel}" name="user.username"/>
            <fmt:message key="password" var="passwordLabel"/>
            <petclinic:inputField label="${passwordLabel}" name="user.password"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${owner['new']}">
                        <button class="btn btn-default" type="submit"><fmt:message key="addOwner"/></button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit"><fmt:message key="updateOwner"/></button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
