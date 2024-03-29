<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">
				<petclinic:menuItem active="${name eq 'home'}" url="/"
					title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Home</span>
				</petclinic:menuItem>
				
				<sec:authorize access="hasAuthority('jugador')">
				<petclinic:menuItem active="${name eq 'createMatch'}" url="/matches/createMatch"
					title="crear partida">
					<span class="glyphicon glyphicon-certificate" aria-hidden="true"></span>
					<span>Crear partida</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAuthority('jugador')">
				<petclinic:menuItem active="${name eq 'matchesList'}" url="/matches/matchesList"
					title="buscar partidas">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Buscar partida</span>
				</petclinic:menuItem>


				<petclinic:menuItem active="${name eq 'achievements'}" url="/statistics/achievements/" title="achievements" dropdown="${true}">
					<ul class="dropdown-menu">
						<li>
							<a href="<c:url value="/statistics/achievements/1" />">Achievements listing</a>
						</li>
						<li class="divider"></li>
						<li>
							<a href="<c:url value="/statistics/achievements/currentPlayer/1" />">My Achievements <span class="glyphicon glyphicon-certificate" aria-hidden="true"></span></a>
						</li>
					</ul>
				</petclinic:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAuthority('admin')">
				<petclinic:menuItem active="${name eq 'jugadores'}" url="/jugadores/list/1"
					title="jugadores">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Jugadores</span>
				</petclinic:menuItem>
				
				<petclinic:menuItem active="${name eq 'partidas'}" url="/matches" title="Partidas" dropdown="${true}">
					<ul class="dropdown-menu">
						<li>
							<a href="<c:url value="/matches/InProgress/1" />">Partidas en curso</a>
						</li>
						<li class="divider"></li>
						<li>
							<a href="<c:url value="/matches/Finished/1" />">Partidas jugadas </a>
						</li>
					</ul>
				</petclinic:menuItem>
				
				<petclinic:menuItem active="${name eq 'achievements'}" url="/statistics/achievements/admin/1"	title="achievements">
					<span class="glyphicon glyphicon-certificate" aria-hidden="true"></span>
					<span>Achievements</span>
				</petclinic:menuItem>
				</sec:authorize>
				

			</ul>

			<ul class="nav navbar-nav navbar-right">
				
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Inicia sesi&oacute;n</a></li>
					<li><a href="<c:url value="/registerNewJugador" />">Reg&iacute;strate</a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<sec:authorize access="hasAuthority('jugador')">
							<li>
									<a href="/perfil" class="text-center"> Perfil</a>
							</li>
							<li class="divider"></li>
							</sec:authorize>
							<li> <a href="<c:url value="/cambiarEstadoOffline" />" class="text-center">Cerrar Sesi&oacute;n</a>
							</li>

 
						</ul></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<c:if test="${tengoInvitaciones}">
						<a href="<c:url value="/invitacionesPendientes"/>" class="btn btn-light" style="margin-top: 5%;">
							<span class="glyphicon glyphicon-bell" aria-hidden="true" style="color: orange;font-size: 2.1em">
							</span>
						</a>
					</c:if>
					<c:choose>
						<c:when test="${partidaPendiente}">
							<a href="<c:url value="/matches/${matchId}/currentMatch"/>" class="btn btn-light" style="margin-top: 5%;">
								<span class="glyphicon glyphicon-alert" aria-hidden="true" style="color: red;font-size: 2.1em"></span>
							</a>
						</c:when>
						<c:when test="${jugador2NoUnido}">
							<a href="<c:url value="/matches/${matchId}/waitForMatch"/>" class="btn btn-light" style="margin-top: 5%;">
							<span class="glyphicon glyphicon-alert" aria-hidden="true" style="color: red;font-size: 2.1em"></span>
							</a>
						</c:when>
					</c:choose>
					
				</sec:authorize>
			</ul>
		</div>

		

	</div>
	
</nav>
				<sec:authorize access="isAuthenticated()">
					<c:if test="${tengoInvitaciones}">
						<div class="alert alert-info">${mensaje}</div>
					</c:if>
				</sec:authorize>