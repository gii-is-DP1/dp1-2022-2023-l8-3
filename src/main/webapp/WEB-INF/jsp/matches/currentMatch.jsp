<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="currentMatch">

	<form:form class="tablero" modelAttribute="match" onsubmit="return validate()">
		<h2>Partida en curso</h2>

		<div class="seccion1">

			<div class="jugador1">
				<c:set var="jugador" value="${match.jugador1}"/>
				<petclinic:seccionJugador usuario="${jugador.user.username}" numeroBacterias="${jugador.bacterias}"
					 numeroSarcinas="${jugador.sarcinas}" contaminacion="${jugador.numeroDeContaminacion}"/>
			</div>

			<div class="discos disable-select">
				<c:forEach var="i" begin="0" end="6" >
					<petclinic:disco indexDisco="${i}" clase="disco ${match.chooseTag(i)}"/>
				</c:forEach>
			</div>

			<div class="jugador2">
				<c:set var="jugador" value="${match.jugador2}"/>
				<petclinic:seccionJugador usuario="${jugador.user.username}" numeroBacterias="${jugador.bacterias}"
					 numeroSarcinas="${jugador.sarcinas}" contaminacion="${jugador.numeroDeContaminacion}"/>
			</div>

		</div>


		<div class="seccion2">
			<div class="chat">
				chat
			</div>

			<div class="informacion">
				<p>
					Informacion <br/>
					${match.turns[match.turn]} <br/>
				</p>
				<p class="error">
					${error}
				</p>

			</div>

			<div class="botones">

				<c:choose>
						<c:when test="${match.esPropagacion()}">
							<input type="submit" value="Siguiente fase"/>
						</c:when>
						<c:otherwise>
							<a class="button" href="<c:url value="/matches/${match.id}/currentMatch" />">Siguiente fase </a>
						</c:otherwise>
				</c:choose>
				<input  class="button" type="button" value="Abandonar partida"/>
			</div>

		</div>

	</form:form>

	<a class="idJugador noDisplay">
		${match.getIdJugadorTurnoActual()}
	</a>

</petclinic:layout>

<script type="text/javascript">
	const discoLabels = document.getElementsByClassName('discoLabel');
	const taLabels = document.getElementsByClassName('taLabel');
	const inputs = document.getElementsByClassName("inputs");
	const error = document.getElementsByClassName('error')[0];

	function toggleCheckbox(element){

		var checkedLabel = element.parentNode;

		for (var i = 0; i < discoLabels.length; i++) {
			if(element.checked){
				discoLabels[i].classList.add("noDisplay");
				var checkedDiskIndex = parseInt(element.id.substring(5));
				var diskIndex = parseInt(discoLabels[i].firstElementChild.id.substring(5));

				if(isNextTo(checkedDiskIndex, diskIndex)){
						taLabels[i].classList.remove("noDisplay");
				}

			}else{
				discoLabels[i].classList.remove("noDisplay");
				taLabels[i].classList.add("noDisplay");

				//Eliminamos los valores de cada input
				for (var j = 0; j < inputs.length; j++) {
					inputs[j].value="0";
				}

			}
		}
		checkedLabel.classList.remove("noDisplay");
	}

	const map = new Map();

	map.set(1, [2,3,4]);
	map.set(2, [1,4,5]);
	map.set(3, [1,4,6]);
	map.set(4, [1,2,3,5,6,7]);
	map.set(5, [2,4,7]);
	map.set(6, [3,4,7]);
	map.set(7, [4,5,6]);

	function isNextTo(i,j){
		var arr = map.get(i);
		return arr.includes(j);
	}

	function reglasComplejas(checkedDiskIndex, diskIndex){
		/*Disco dDestino = getDisco(discoDestino-1);
		Integer enemigo = jugador==PRIMER_JUGADOR ? SEGUNDO_JUGADOR : PRIMER_JUGADOR;

		//Si disco destino tiene sarcina tuya el mov es ilegal
		if(dDestino.getNumeroDeSarcinas(jugador)!=0){
			String msg = "Disco destino con sarcina aliada";
			System.out.println(msg);
			return msg;
		}
		//Si quedan mismo numero de bacterias enemigas que aliadas el mov es ilegal
		Integer i = dDestino.getNumeroDeBacterias(jugador)+valor;
		if(i != 0 && i == dDestino.getNumeroDeBacterias(enemigo)){
			String msg = "Mismo numero de bacterias enemigas que aliadas";
			System.out.println(msg);
			return msg;
		}

		//Si quedan mas de 5 bacterias en disco destino el mov es ilegal
		if((dDestino.getNumeroDeBacterias(jugador)+valor > 5)) {
			String msg = "Mas de 5 bacterias en disco destino";
			System.out.println(msg);
			return msg;
		}
		return "";
		*/
		/*
		jugador = parseInt(document.getElementsByClassName('idJugador')[0].innerText);
		var elChildren = discoLabels[checkedDiskIndex].parentNode.children;
		for(var c=0; c<elChildren.length - 1; c++){
			console.log(elChildren[c].className);
			var s = 'bs'+jugador;
			if(elChildren[c].className == s){
				console.log("PIPO");
			}
		}*/
		return '';
	}

//Comprobar
	const checkboxes = document.getElementsByClassName("checkbox");

	function validate(){
		console.log("Validating submited values");
		var n = 0;
		var checkedBox = null;
		for (var i = 0; i < checkboxes.length; i++) {
			if (checkboxes[i].checked) {
				n++;
				checkedBox = checkboxes[i];
			}
		}

		//Solo puede haber 1 checkbox checkeado
		if(n!=1){
			error.innerText = "Mas de 1 checkbox checkeado o ninguno checkeado";
			return false;
		}

		//checkedBox no puede ser null a partir de aqui

		var discoLabels = document.getElementsByClassName('discoLabel');
		var atLeastAnInputWithValue = false;
		for (var i = 0; i < discoLabels.length; i++) {//Por cada discoLabel, comprobamos cuales estan al lado del checkbox checkeado
			var checkedDiskIndex = parseInt(checkedBox.id.substring(5));
			var diskIndex = parseInt(discoLabels[i].firstElementChild.id.substring(5));

			var input = parseInt(discoLabels[i].parentNode.childNodes[7].firstElementChild.firstElementChild.value);
			if(isNaN(input)){
				error.innerText = "Un input: "+ input +" , tiene un valor que no es un numero";
				return false;
			}

			if(isNextTo(checkedDiskIndex, diskIndex)){
				//Validamos el contenido de los inputs de cada disco. Valores validos: [0,4]
				if(!(input>=0 && input<5)) {
					error.innerText = "Valor ilegal en un input: "+ input +" ,  a enviar";
					return false;
				}else if (input!=0) {
					atLeastAnInputWithValue = true;
				}

				var err = reglasComplejas(checkedDiskIndex, diskIndex);
				if(err != '') {
					error.innerText = err;
					return false;
				}

			}else{
				//No puede haber contenido en estos inputs
				if(input!=0){
					error.innerText = "Hay un input: "+ input +" ,  ilegal con contenido";
					return false;
				}
			}
		}

		if (!atLeastAnInputWithValue) {
				error.innerText = "All values to send were 0. Illegal";

				return false;
		}
		return true;
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
	.button:hover {
		text-decoration: none;
		background-color: rgba(25, 25, 30, 0.05);
		color: green;
	}

	.button {
		text-decoration: none;
		background-color: #EEEEEE;
		color: #333333;
		padding: 2px 6px 2px 6px;
		border-top: 1px solid #CCCCCC;
		border-right: 1px solid #333333;
		border-bottom: 1px solid #333333;
		border-left: 1px solid #CCCCCC;
		border-radius: 5px;
		padding: 5px;
		margin-left: auto;
		margin-right: auto;
	}

	.noDisplay{
		display:none;
	}
	p.error{
		color: red;
	}
	input[type=checkbox] {
	 display: initial;
	}

	input:checked + label {
	  border: solid 2px purple;
	  background-color: purple;
		color: purple;
		opacity: 20%;
	}
	.discoLabel{
		position: absolute ;
	  top: 0;
	  bottom: 0;
	  left: 0;
	  right: 0;
		height: 100%;
  	width: 100%;
		border-radius: 50%;
	}
	.taLabel{
		z-index: 4;
	}
	.disable-select {
	  -webkit-user-select: none;
	  -moz-user-select: none;
	  -ms-user-select: none;
	  user-select: none;
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

	}
	select{
		z-index: 4;
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
	}

</style>
