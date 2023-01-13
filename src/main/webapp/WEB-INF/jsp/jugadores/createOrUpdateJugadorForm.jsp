<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<style>
@import url('https://fonts.googleapis.com/css2?family=Press+Start+2P&display=swap');</style>

<petclinic:layout pageName="jugadorForm">
	
	<c:choose>
		<c:when test="${jugador['new']}">
			<div class="WelcomeNuevoUsu">
				<h1>Bienvenido<p class="letras">JUGADOR</p>que tal si nos registramos</h1>
			</div>
		</c:when>
		<c:otherwise>
			<div class="WelcomeNuevoUsu">
				<p class="letras">ACTUALIZA TU PERFIL</p>
			</div>
		</c:otherwise>
	</c:choose>
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
	<c:if test="${usernameRegistered}">
		<div class="alert alert-info">
    	 Username incorrecto, ya esta registrado a nombre de otro jugador
		</div>
	</c:if>
    <c:if test="${sinJugadores}"><div class="alert alert-info">No hay jugadores registrados actualmente</div></c:if>
	<form:form modelAttribute="jugador" class="form-horizontal"
		id="add-owner-form">
		<div class="form-group has-feedback">
			<table class="table table-borderless">
				<tr>
					<th>Nombre de usuario</th>
					<td><input class="form-control" value="${jugador.user.username}" type="text" name="user.username"/></td>
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
					<td><input class="form-control" value="${jugador.user.email}" type="text" name="user.email"/></td>
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
						<button class="btn btn-default btn-block" type="submit">&Uacute;nete a nosotros</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-default btn-block" type="submit">Actualizar informaci&oacute;n</button>
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

.WelcomeNuevoUsu{

text-align: center;

}

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

.letras{
position: relative;
	text-align: center;
	font-size: xx-large;
	font-family: 'Press Start 2P', cursive;
	animation-name: anim;
	animation-duration: 5s;
	animation-iteration-count: infinite;
}
@keyframes anim{
    0% {color: blue;} /*Amarillo*/
   16.5% {color: purple;} /*Naranja*/
   33% {color: red;} /*Negro*/
   50% {color:#FFCD00;}
   66% {color: #DEF400;} 
   82.5% {color: GREEN;} /*Otra vez naranja*/
   100% {color:#00A2FF;} /*Otra vez amarillo*/
  
}

</style>

