<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<petclinic:layout pageName="currentMatch">
	
	<form:form class="tablero" modelAttribute="movimiento">
		<h2>Partida en curso</h2>

		<div class="seccion1">
			<div class="jugador1">
				<h4>${match.jugador1.user.username}</h4>

				<div class="bacterias">
					<h4>N. bacterias: ${match.jugador2.bacterias}</h4>
					<c:forEach var="i" begin="1" end="${match.jugador1.bacterias}" >
						<div class="smallbacteria"></div>
					</c:forEach>
				</div>

				<div class="sarcinas">
					<h4>N. sarcinas: ${match.jugador2.sarcinas}</h4>
					<c:forEach var="i" begin="1" end="${match.jugador1.sarcinas}" >
						<div class="smallsarcina"></div>
					</c:forEach>
				</div>
				
				<h4>Contaminacion: ${match.jugador1.numeroDeContaminacion}</h4>
				
			</div>
			<div class="discos">
				
				<div class="disco">
					<c:forEach var="i" begin="0" end="1" >
						<div class="circulo">
							<div class="j1">
								<c:forEach var="j" begin="1" end="${match.discos[i].numBact1}" >
									<div class="smallbacteria"></div>
								</c:forEach>
							</div>
							<div class="j1">
								<c:forEach var="j" begin="1" end="${match.discos[i].numSarc1}" >
									<div class="smallsarcina"></div>
								</c:forEach>
							</div>
							<div class="j2">
								<c:forEach var="j" begin="1" end="${match.discos[i].numBact2}" >
									<div class="smallbacteria"></div>
								</c:forEach>
							</div>
							<div class="j2">
								<c:forEach var="j" begin="1" end="${match.discos[i].numSarc2}" >
									<div class="smallsarcina"></div>
								</c:forEach>
							</div>												
						</div>
					</c:forEach>
				</div>
				<div class="disco">
					<c:forEach var="i" begin="2" end="4" >
						<div class="circulo">
							<div class="j1">
								<c:forEach var="j" begin="1" end="${match.discos[i].numBact1}" >
									<div class="smallbacteria"></div>
								</c:forEach>
							</div>
							<div class="j1">
								<c:forEach var="j" begin="1" end="${match.discos[i].numSarc1}" >
									<div class="smallsarcina"></div>
								</c:forEach>
							</div>
							<div class="j2">
								<c:forEach var="j" begin="1" end="${match.discos[i].numBact2}" >
									<div class="smallbacteria"></div>
								</c:forEach>
							</div>
							<div class="j2">
								<c:forEach var="j" begin="1" end="${match.discos[i].numSarc2}" >
									<div class="smallsarcina"></div>
								</c:forEach>
							</div>										
						</div>
					</c:forEach>
				</div>
				<div class="disco">
					<c:forEach var="i" begin="5" end="6" >
						<div class="circulo">
							<div class="j1">
								<c:forEach var="j" begin="1" end="${match.discos[i].numBact1}" >
									<div class="smallbacteria"></div>
								</c:forEach>
							</div>
							<div class="j1">
								<c:forEach var="j" begin="1" end="${match.discos[i].numSarc1}" >
									<div class="smallsarcina"></div>
								</c:forEach>
							</div>
							<div class="j2">
								<c:forEach var="j" begin="1" end="${match.discos[i].numBact2}" >
									<div class="smallbacteria"></div>
								</c:forEach>
							</div>
							<div class="j2">
								<c:forEach var="j" begin="1" end="${match.discos[i].numSarc2}" >
									<div class="smallsarcina"></div>
								</c:forEach>
							</div>												
						</div>
					</c:forEach>
				</div>

			</div>


			<div class="jugador2">
				<h4>${match.jugador2.user.username}</h4>

				<div class="bacterias">
					<h4>N. bacterias: ${match.jugador2.bacterias}</h4>
					<c:forEach var="i" begin="1" end="${match.jugador2.bacterias}" >
						<div class="smallbacteria"></div>
					</c:forEach>
				</div>

				<div class="sarcinas">
					<h4>N. sarcinas: ${match.jugador2.sarcinas}</h4>
					<c:forEach var="i" begin="1" end="${match.jugador2.sarcinas}" >
						<div class="smallsarcina"></div>
					</c:forEach>
				</div>

				<h4>Contaminacion: ${match.jugador2.numeroDeContaminacion}</h4>

			</div>

		</div>


		<div class="seccion2">
			<div class="chat">
				chat
			</div>

			<div class="informacion">
				<p>
					Informacion <br/>
					${match.turns[match.turn]}
				</p>
			</div>

			<div class="botones">
				<a href="/matches/${match.id}/nextPhase"><input type="submit" value="Siguiente fase"/></a>
				<a href="/matches/${match.id}/completedMatch"><input type="submit" value="Abandonar partida"/></a>
			</div>
		</div>

	</form:form>



</petclinic:layout>

<script type="text/javascript">
/*
//No necesario creo
	const discos = document.getElementsByClassName("disco");

	for (let i = 0; i < discos.length; i++) {
		discos[i].onclick = (e) => {
			console.log("a");
			const input = e.target.querySelector('input');
			input.checked = !input.checked;
		}
	}
*/

//Comprobar
	function validate(){
		const c1 = document.getElementsByName("bacteria");
		const c2 = document.getElementsByName("disco");

		let cb = [];
		let cd = [];
		for (var i = 0; i < c1.length; i++) {
			if (c1[i].checked) {
				cb.push(c1[i].value);
			}
		}
		for (var i = 0; i < c2.length; i++) {
			if (c2[i].checked) {
				cd.push(c2[i].value);
			}
		}

		if(cd.length != 1 || cb.length == 0) return false;

		let disco = cb[0].id.substring(11);
		for (var i = 1; i < cb.length; i++) {
			let disco2 = db[i].id.substring(11);
			if(disco != disco2) return false;
		}

	}



</script>

<style type="text/css">

	.circulo {
		width: 10vw; 
		height: 20vh; 
		background-color: #aaa; 
		border-radius: 50%; 
		text-align: center;
		display: flex;
	}
  
	.seccion1{
		height: min(var(--discos-vw),var(--discos-vh));
	}

	.seccion1 div{
		margin:0 auto 0 auto;
	}

	.jugador1, .jugador2{
		width: var(--jugadores);
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		padding-top:100px;
		padding-bottom: 100px;
		padding-left: 10px;
		padding-right: 10px;
		border-radius: 10px;
		background-color: var(--color-background-divs);
	}

	.bacterias, .sarcinas{
		display: inline-flex;
    flex-wrap: wrap;
	}
  
	.discos{
		display: flex;
		flex-direction: column;
		border-radius: 10px;
		background-color: var(--color-background-divs);
		justify-content: space-between;
	}
  
	.disco {
		display: flex;
		align-items: center;
		grid-column-end: span 2;
		justify-content: space-evenly;
		padding: auto;
		position:relative;
		color:var(--color-disco);
	}

	.col23{
		grid-column-start: 2;
	}
	.col45{
		grid-column-start: 4;
	}

	.row3{
		grid-row-start: 3;
	}
	.row2{
		grid-row-start: 2;
	}

	.bacteria {
		width: min(calc(var(--discos-vw)/15),calc(var(--discos-vh)/15));
		height: min(calc(var(--discos-vw)/15),calc(var(--discos-vh)/15));
		border-radius: 50%;
		border: none;
		position: relative;
		z-index: 4;
	}

	.sarcina {
		width: min(calc(var(--discos-vw)/10),calc(var(--discos-vh)/10));
		height: min(calc(var(--discos-vw)/10),calc(var(--discos-vh)/10));
		border-radius: 20%;
		border: none;
	}

	.disco div{
		display: flex;
		flex-wrap: wrap;
		justify-content: center;
		word-break:break-all;
	}

	:root{
		--discos-vw:40vw;
		--discos-vh:60vh;
		--color-disco:#878787;
		--color-background-divs:rgba(22, 22, 26,0.2);
		--jugadores:min(calc(100vw - var(--discos-vw))/2, calc(100vh - var(--discos-vh))/2);
		--color-j1:#B00B13;
		--color-j2:SlateBlue;
	}

	input[type=checkbox] {
	 display: none;
	}

	input:checked + label {
	  border: solid 2px purple;
	  background-color: purple;
		color: purple;
		opacity: 20%;
	}
	label{
		position: absolute ;
	  top: 0;
	  bottom: 0;
	  left: 0;
	  right: 0;
		height: 100%;
  	width: 100%;
		border-radius: 50%;
	}
	.pointer{
		cursor: pointer;
	}

	.tablero{
		display: block;
	}

	.seccion1, .seccion2{
		text-align: center;
		display: flex;
	}
	.seccion1{
		height: min(var(--discos-vw),var(--discos-vh));
	}

	.seccion1 div{
		margin:0 auto 0 auto;
	}

	.jugador1, .jugador2{
		width: var(--jugadores);
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		padding-top:100px;
		padding-bottom: 100px;
		padding-left: 10px;
		padding-right: 10px;
		border-radius: 10px;
		background-color: var(--color-background-divs);
	}
	

	.bacterias, .sarcinas{
		display: inline-flex;
    flex-wrap: wrap;
	}



	.bacteria {
		width: min(calc(var(--discos-vw)/15),calc(var(--discos-vh)/15));
		height: min(calc(var(--discos-vw)/15),calc(var(--discos-vh)/15));
		border-radius: 50%;
		border: none;
		position: relative;
		z-index: 4;
	}

	.sarcina {
		width: min(calc(var(--discos-vw)/10),calc(var(--discos-vh)/10));
		height: min(calc(var(--discos-vw)/10),calc(var(--discos-vh)/10));
		border-radius: 20%;
		border: none;
	}

	.bs1 .bacteria, .bs1 .sarcina, .jugador1 .bacterias .smallbacteria, .jugador1 .sarcinas .smallsarcina, .circulo .j1 .smallbacteria, .circulo .j1 .smallsarcina{
		background-color: var(--color-j1);
		color: var(--color-j1);
	}

	.bs2 .bacteria, .bs2 .sarcina, .jugador2 .bacterias .smallbacteria, .jugador2 .sarcinas .smallsarcina, .circulo .j2 .smallbacteria, .circulo .j2 .smallsarcina{
		background-color: var(--color-j2);
		color: var(--color-j2);
	}


	.seccion2{
		height: min(var(--discos-vw)/2,var(--discos-vh)/2);
		display: grid;
		grid-template-columns: repeat(3, 1fr);
		grid-template-rows: repeat(1, 1fr);
	}
	.smallbacteria{
		width: min(calc(var(--discos-vw)/20),calc(var(--discos-vh)/20));
		height: min(calc(var(--discos-vw)/20),calc(var(--discos-vh)/20));
		border-radius: 50%;
		border: none;
	}
	.smallsarcina{
		width: min(calc(var(--discos-vw)/15),calc(var(--discos-vh)/15));
		height: min(calc(var(--discos-vw)/15),calc(var(--discos-vh)/15));
		border-radius: 20%;
		border: none;
	}
	.seccion2 div{
		border: 1px solid black;
	{

</style>
