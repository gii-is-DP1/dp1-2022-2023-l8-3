<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="jugadorForm">
	
	<h2>
		<c:choose>
			<c:when test="${jugador['new']}">Nuevo </c:when>
			<c:otherwise>Actualizar </c:otherwise>
		</c:choose>
		Jugador
	</h2>
	<br>
	<c:if test="${contraseñaIncorrecta}">
		<div class="alert alert-info">
    	 Contrase&ntilde;a incorrecta, debe tener entre 10 y 50 caracteres y al menos un numero
		</div>
	</c:if>
	<c:if test="${emailIncorrecto1}">
		<div class="alert alert-info">
    	 Email incorrecto, ya esta registrado a nombre de otro jugador
		</div>
	</c:if>
	<c:if test="${emailIncorrecto2}">
		<div class="alert alert-info">
    	 El Email no cumple el formato correcto
		</div>
	</c:if>
	<c:if test="${firstNameOrLastNameAreEmpty}">
		<div class="alert alert-info">
    	 El nombre y los apellidos no pueden ser vacíos
		</div>
	</c:if>
    <c:if test="${sinJugadores}"><div class="alert alert-info">No hay jugadores registrados actualmente</div></c:if>
	<form:form modelAttribute="jugador" class="form-horizontal"
		id="add-owner-form">
		<div class="form-group has-feedback">
			<table class="table table-borderless">
				<caption>Player data</caption>
				<tr>
					<th>Nombre de usuario</th>
					<c:choose>
						<c:when test="${jugador['new']}">
							<td><input class="form-control" type="text" name="user.username"/></td>
						</c:when>
						<c:otherwise>
							<td><input class="form-control" value="${jugador.user.username}" type="text" name="user.username"/></td>
						</c:otherwise>
					</c:choose>
				</tr>
				<tr>
					<th>Nombre</th>
					<td><input class="form-control" value="${jugador.firstName}" type="text" name="firstName"/></td>
				</tr>
				<tr>
					<th>Apellido</th>
					<td><input class="form-control" value="${jugador.lastName}" type="text" name="lastName"/></td>
				</tr>
				<tr>
					<th>Email</th>
					<td><input class="form-control" value="${jugador.user.email}" type="email" name="user.email"/></td>
				</tr>
				<tr>
					<th>Contrase&ntilde;a</th>
					<c:choose>
						<c:when test="${jugador['new']}">
							<td><input class="form-control" type="text" name="user.password" id="password"/></td>
						</c:when>
						<c:otherwise>
							<td><input class="form-control" value="${jugador.user.password}" type="password" name="user.password" id="password"/></td>
						</c:otherwise>
					</c:choose>
				</tr>
				<tr>
					<th/>
					<td>
						<c:choose>
							<c:when test="${jugador['new']}">
								<input id="mostrarOcultar" class="btn btn-primary" type="button" onclick="mostrarContrasena()"value="Ocultar Contraseña"></input>
							</c:when>
							<c:otherwise>
								<input id="mostrarOcultar" class="btn btn-primary" type="button" onclick="mostrarContrasena()"value="Mostrar Contraseña"></input>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<c:choose>
					<c:when test="${jugador['new']}">
						<button class="btn btn-default" type="submit">A&ntilde;adir
							jugador</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-default" type="submit">Actualizar
							Jugador</button>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</form:form>
</petclinic:layout>
<script>
function mostrarContrasena(){
    var tipo = document.getElementById("password");
    var button = document.getElementById("mostrarOcultar");
    if(tipo.type == "password"){
        tipo.type = "text";
    }else{
        tipo.type = "password";
    }
    if(button.value=="Mostrar Contraseña"){
  	  button.value="Ocultar Contraseña";
    }
    else{
  	  button.value="Mostrar Contraseña";
    }
}
</script>
<style type="text/css">

.table-borderless > tbody > tr > td,
.table-borderless > tbody > tr > th,
.table-borderless > tfoot > tr > td,
.table-borderless > tfoot > tr > th,
.table-borderless > thead > tr > td,
.table-borderless > thead > tr > th {
    border: none;
}

.table-borderless td{
	width: 84%;
}
</style>
