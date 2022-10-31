<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<petclinic:layout pageName="currentMatch">

	<form:form class="tablero" modelAttribute="">>
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
				<c:forEach var="i" begin="0" end="6" >
					<div class="disco ${match.chooseTag(i)}">
						<input type="checkbox" name="disco${i}" id="disco${i}" value="a disco ${i}">
						<label for="disco${i}" class="pointer">a</label>

						<div class="bs1">
							<c:forEach var="b1" begin="1" end="${match.discos[i].numBact1}">
								<input type="checkbox" name="j1bacteria${b1}disco${i}" id="j1bacteria${b1}disco${i}" value="bacteria jug1 de disco ${i}">
								<label for="j1bacteria${b1}disco${i}" class="bacteria pointer">a</label>
							</c:forEach>

							<c:forEach var="s1" begin="1" end="${match.discos[i].numSarc1}">
								<div class="sarcina pointer"></div>
							</c:forEach>
						</div>

						<div class="bs2">
							<c:forEach var="b2" begin="1" end="${match.discos[i].numBact2}">
								<input type="checkbox" name="j2bacteria${b2}disco${i}" id="j2bacteria${b2}disco${i}" value="bacteria jug2 de disco ${i}">
								<label for="j2bacteria${b2}disco${i}" class="bacteria pointer">a</label>
							</c:forEach>

							<c:forEach var="s2" begin="1" end="${match.discos[i].numSarc2}">
								<div class="sarcina pointer"></div>
							</c:forEach>
						</div>


					</div>

				</c:forEach>
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
				informacion
			</div>

			<div class="botones">
				<input type="submit" value="Siguiente fase"/>
				<button type="button" name="button">Abandonar partida</button>
			</div>
		</div>

	</form:form>



</petclinic:layout>

<script type="text/javascript">
	const discos = document.getElementsByClassName("disco");

	for (let i = 0; i < discos.length; i++) {
		discos[i].onclick = (e) => {
			const input = e.target.querySelector('input');
			input.checked = !input.checked;
		}
	}


</script>

<style type="text/css">
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

	.discos {
		width: min(var(--discos-vw),var(--discos-vh));
		display: grid;
		grid-template-columns: repeat(6, 1fr);
		grid-template-rows: repeat(3, 1fr);
		column-gap: min(calc(var(--discos-vw)/50),calc(var(--discos-vh)/50));
		justify-content: center;
		padding: 1em;
		border-radius: 10px;
		background-color: var(--color-background-divs);
	}

	.disco {
		width: min(calc(var(--discos-vw)/3.2),calc(var(--discos-vh)/3.2));
		height: min(calc(var(--discos-vw)/3.2),calc(var(--discos-vh)/3.2));

		background-color: var(--color-disco);
		border: 1px solid black;
		border-radius: 50%;
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
	}

	.bs1 .bacteria, .bs1 .sarcina, .jugador1 .bacterias .smallbacteria, .jugador1 .sarcinas .smallsarcina{
		background-color: var(--color-j1);
		color: var(--color-j1);
	}

	.bs2 .bacteria, .bs2 .sarcina, .jugador2 .bacterias .smallbacteria, .jugador2 .sarcinas .smallsarcina{
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




	input{

	}






</style>
