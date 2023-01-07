package org.springframework.samples.petclinic.partida;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.comentario.Comentario;
import org.springframework.samples.petclinic.disco.Disco;
import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "matches")
@DynamicUpdate
public class Match extends NamedEntity{
	private static final int NUMBER_OF_TURNS = 4;
	private static final int NUMBER_OF_DISKS = 7;
	private static final int PRIMER_JUGADOR = 1;
	private static final int SEGUNDO_JUGADOR = 2;
	
	private static final Map<Integer, List<Integer>> map = new HashMap<>();
	
	
	//Valores representan el numero de bacterias a sumar a discoX
	@Transient
	private Integer disco1;
	@Transient
	private Integer disco2;
	@Transient
	private Integer disco3;
	@Transient
	private Integer disco4;
	@Transient
	private Integer disco5;
	@Transient
	private Integer disco6;
	@Transient
	private Integer disco7;
	
	//Representa disco origen de donde viene (tiene que ser array por como esta hecho)
	@Transient
	private Integer[] deDisco;

	@Transient
	private String movimiento;
		
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@Column(name = "inicio_de_partida")
	private LocalDateTime inicioPartida;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss.69")
    @Column(name = "fin_de_partida")
	private LocalDateTime finPartida;
    
    @Column(name = "contamination_number_of_player_1")
	private Integer contaminationNumberOfPlayer1;

    @Column(name = "contamination_number_of_player_2")
	private Integer contaminationNumberOfPlayer2;
    
	@Column(name = "number_of_bacteria_of_player_1")
	private Integer numberOfBacteriaOfPlayer1;

	@Column(name = "number_of_bacteria_of_player_2")
	private Integer numberOfBacteriaOfPlayer2;
	
	@Column(name = "number_of_sarcina_of_player_1")
	private Integer numberOfSarcinaOfPlayer1;
	
	@Column(name = "number_of_sarcina_of_player_2")
	private Integer numberOfSarcinaOfPlayer2;

	@Column(name = "es_privada")
	private Boolean esPrivada;
  
	@Column(name = "turn")
	private Integer turn;
	
	@Transient
	private List<String> turns;
	
	@OneToMany(mappedBy="match")
	private List<Disco> discos;

	@ManyToOne
	@JoinColumn(name = "id_jugador1")
	private Jugador jugador1;
	
	@ManyToOne
	@JoinColumn(name = "id_jugador2")
	private Jugador jugador2;

	@ManyToMany
	private Set<Jugador> espectadores;
	
	@OneToMany(mappedBy="match")
	private List<Invitacion> invitaciones;
	
	@Column(name = "ganador_de_partida")
	@Enumerated(EnumType.STRING)
	private GameWinner ganadorPartida;	
	
	@OneToMany(mappedBy="match")
	private List<Comentario> comentarios;
	
	@Column(name = "abandonada")
	private Boolean abandonada;
	
	
	public Match(Boolean esPrivada, Jugador jugadorAnfitrion) {
		this.esPrivada = esPrivada;
		this.jugador1 = jugadorAnfitrion;
		this.espectadores = new HashSet<Jugador>();
		this.invitaciones = new ArrayList<Invitacion>();
		this.comentarios = new ArrayList<Comentario>();
		this.abandonada = false;
		this.ganadorPartida = GameWinner.UNDEFINED;
		this.turn = 0;
		this.contaminationNumberOfPlayer1 = 0;
		this.contaminationNumberOfPlayer2 = 0;
		this.numberOfBacteriaOfPlayer1 = 20;
		this.numberOfBacteriaOfPlayer2 = 20;
		this.numberOfSarcinaOfPlayer1 = 4;
		this.numberOfSarcinaOfPlayer2 = 4;
		createDisks();
		createTurns();
		initializateMap();
	}
	
	public Match() {
		this.espectadores = new HashSet<Jugador>();
		this.invitaciones = new ArrayList<Invitacion>();
		this.comentarios = new ArrayList<Comentario>();
		this.abandonada = false;
		this.turn = 0;
		this.contaminationNumberOfPlayer1 = 0;
		this.contaminationNumberOfPlayer2 = 0;
		this.numberOfBacteriaOfPlayer1 = 20;
		this.numberOfBacteriaOfPlayer2 = 20;
		this.numberOfSarcinaOfPlayer1 = 4;
		this.numberOfSarcinaOfPlayer2 = 4;
		createDisks();
		createTurns();
		initializateMap();
	}
	
	private void initializateMap() {		
		map.put(1, List.of(2,3,4));
		map.put(2, List.of(1,4,5));
		map.put(3, List.of(1,4,6));
		map.put(4, List.of(1,2,3,5,6,7));
		map.put(5, List.of(2,4,7));
		map.put(6, List.of(3,4,7));
		map.put(7, List.of(4,5,6));
	}
	
	private void createDisks() {
		discos = new ArrayList<Disco>();
		for(int i = 0; i<NUMBER_OF_DISKS; i++) {
			discos.add(new Disco(this));
		}
	}
	
	
	private void createTurns() {
		turns = new ArrayList<String>();
		for(int i=0; i<NUMBER_OF_TURNS; i++) {
			turns.add("PROPAGATION_RED_PLAYER");
			turns.add("PROPAGATION_BLUE_PLAYER");
			turns.add("BINARY");
			turns.add("PROPAGATION_RED_PLAYER");
			turns.add("PROPAGATION_BLUE_PLAYER");
			turns.add("BINARY");
			turns.add("PROPAGATION_RED_PLAYER");
			turns.add("PROPAGATION_BLUE_PLAYER");
			turns.add("BINARY");
			turns.add("POLLUTION");
		}
		turns.add("FIN");
	}
	
	// ----------------------------------------------------------------------------------------------- //
	
	public Integer getNumberOfBacteria(Integer playerId) {
		return playerId == 0 ? numberOfBacteriaOfPlayer1 : numberOfBacteriaOfPlayer2;
	}
	
	public Integer getNumberOfSarcina(Integer playerId) {
		return playerId == 0 ? numberOfSarcinaOfPlayer1 : numberOfSarcinaOfPlayer2;
	}
	
	public void addBacteria(Integer playerId, Integer numberOfBacteria) {
		if(playerId == 0) {
			numberOfBacteriaOfPlayer1 += numberOfBacteria;
		} else {
			numberOfBacteriaOfPlayer2 += numberOfBacteria;
		}
	}
	
	public Jugador getPlayer(Integer id) {
		return id == 0 ? jugador1 : jugador2;
	}

	public Disco getDisco(Integer diskId) {
		return discos.get(diskId);
	}
	
	public Integer getTurn() {
		return turn;
	}
	
	public List<String> getTurns() {
		return turns;
	}
	
	public Boolean itIsPropagationPhase() {
		return getTurns().get(getTurn()).contains("PROPAGATION");
	}
	
	public Boolean itIsFirstPlayerTurn() {
		return getTurns().get(getTurn()).equals("PROPAGATION_RED_PLAYER");
	}
	
	public void nextTurn() {
		turn++;
	}
	
	public String chooseTag(int i) {
		if(i==0) return "col23";
		else if(i==1) return "col45";
		else if(i==2) return "row2";
		else if(i==3) return "row2";
		else if(i==4) return "row2";
		else if(i==5) return "col23 row3";
		else if(i==6) return "col45 row3";
		else return "error";
	}
	private Integer[] getDiskMoves() {
		Integer[] res = {disco1, disco2, disco3, disco4, disco5, disco6, disco7};
		return res;
	}
	
	public List<List<Integer>> getTargetDiskAndNumberOfBacteria() {
		Integer[] disks = getDiskMoves();
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		List<Integer> targetDisks = new ArrayList<Integer>();
		List<Integer> numberOfBacteria = new ArrayList<Integer>();
		
		for(int i = 0; i < NUMBER_OF_DISKS; i++) {
			if(disks[i] > 0) {
				targetDisks.add(i+1);
				numberOfBacteria.add(disks[i]);
			}
		}
		result.add(targetDisks);
		result.add(numberOfBacteria);
		return result;
	}
	
	private Boolean diskINextToDiskJ(Integer i,Integer j){
		var ls = map.get(i);
		return ls.contains(j);
	}
	private void borraMovimientoDisco(Integer disco) {
		if(disco==1) {
			setDisco1(0);
		}else if(disco==2) {
			setDisco2(0);
		}else if(disco==3) {
			setDisco3(0);
		}else if(disco==4) {
			setDisco4(0);
		}else if(disco==5) {
			setDisco5(0);
		}else if(disco==6) {
			setDisco6(0);
		}else {
			setDisco7(0);
		}
	}
	
	//Valida que un movimiento (con datos correctos) sea legal o no
	// TODO: te falta tener en cuenta que en el disco origen no haya el mismo número de bacterias de ambos jugadores
	private String legalMove(Integer discoDestino, Integer valor,Integer jugador, Integer enemigo) {
		Disco dDestino = getDisco(discoDestino-1);

		//Si disco destino tiene sarcina tuya el mov es ilegal
		if(dDestino.getNumeroDeSarcinas(jugador)!=0){ 
			String msg = "Disco destino con sarcina aliada";
			System.out.println(msg);
			return msg;
		}
		//Si quedan mismo numero de bacterias enemigas que aliadas el mov es ilegal 
		Integer i = dDestino.getNumeroDeBacterias(jugador)+valor;
		if(i != 0 && i == dDestino.getNumeroDeBacterias(enemigo)){ 
			String msg = "Mismo numero de bacterias enemigas que aliadas en disco: "+discoDestino;
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
	}
	
	//Valida movimiento y devuelve "" si todo correcto o un msg si ha habido un error.
	public String validateMove() {
		Integer[] disks = getDiskMoves();

		//Debe haber un unico disco origen
		if(getDeDisco()==null || getDeDisco().length != 1) 
			return "Más de un disco origen o ninguno";

		Integer jugador = getIdJugadorTurnoActual();
		Integer enemigo = jugador==PRIMER_JUGADOR ? SEGUNDO_JUGADOR : PRIMER_JUGADOR;

		Integer origen =  getDeDisco()[0];
//		Integer numDiscosOrigen = 0; //Numero de discos a donde hay movimiento posible
//		Integer numDiscosOrigenConCero = 0;//Numero de discos a donde hay movimiento posible con mov=0 (no es movimiento)
		Integer sumaValores = 0;//Numero total de bacterias a quitar de origen

		for(int destino=1; destino <= NUMBER_OF_DISKS; destino++) {
			if(diskINextToDiskJ(origen,destino)) {
				Integer valor = disks[destino-1];//valor = numero bacterias a sumar a disco destino

				//Valores permitidos [0,4]
				if(valor<0 || valor>=5) { 
					System.out.println("Valor distinto de [0,4]");
					return "Valor de bacterias no permitido";
				}

				if(valor != 0) { /*numDiscosOrigenConCero++;
				else {*/
					//Reglas mas complejas 

					String reglasComplejas = legalMove(destino,valor,jugador,enemigo);
					if(reglasComplejas.length() != 0) return reglasComplejas;
				}

				sumaValores+=valor;
			}
			else {
				//Borramos por defecto por si tiene algun valor. Seria movimiento ilegal
				borraMovimientoDisco(destino);
			}
		}

		//Se permite movimiento=0 (no mover a dicho disco), pero no todos pueden ser 0
		//Si todos los movimientos enviados son 0, ilegal
		if(sumaValores == 0) { 
			System.out.println("No hay ningun movimiento indicado");
			return "No se indicó ningun movimiento";
		}
		Disco dOrigen = getDisco(origen-1);

		//Si quedan bacterias negativas en origen el mov es ilegal
		if((dOrigen.getNumeroDeBacterias(jugador)-sumaValores)<0) { 
			String msg = "Bacterias negativas en origen:"+origen;
			System.out.println(msg);
			return msg;
		}
		
		//Si mismo n de bacterias enemigas y aliadas en origen es ilegal
		Integer bacteriasAlidasOrigen = dOrigen.getNumeroDeBacterias(jugador)-sumaValores;
		if(bacteriasAlidasOrigen != 0 &&
				bacteriasAlidasOrigen == dOrigen.getNumeroDeBacterias(enemigo)){ 
			String msg = "Mismo numero de bacterias enemigas que aliadas en disco: "+origen;
			System.out.println(msg);
			return msg;
		}
		
		return "";
	}
	
	public void movingBacteria(Integer idPlayerMatch, Jugador player, Integer initialDiskId, Integer targetDiskId, Integer numberOfBacteriaDisplaced) {
		getDisco(initialDiskId-1).eliminarBacterias(idPlayerMatch, numberOfBacteriaDisplaced);
		getDisco(targetDiskId-1).annadirBacterias(idPlayerMatch, numberOfBacteriaDisplaced);
		checkToAddSarcina(idPlayerMatch, player, targetDiskId-1);
	}
	
	private String checkToAddSarcina(Integer idPlayerMatch, Jugador player, Integer diskId) {
		String message = "";
		if(getDisco(diskId).getNumeroDeBacterias(idPlayerMatch+1) == 5) {
			Integer playerId = player.getUser().getUsername().equals(jugador1.getUser().getUsername()) ? 0 : 1;
			if(getNumberOfSarcina(playerId) > 0) {
				getDisco(diskId).eliminarBacterias(idPlayerMatch, 5);
				addBacteria(playerId, 5);
				getDisco(diskId).annadirSarcina(idPlayerMatch);
				if(playerId == 0) {
					numberOfSarcinaOfPlayer1--;
				} else {
					numberOfSarcinaOfPlayer2--;
				}
			} else {
				if(ganadorPartida == GameWinner.UNDEFINED) {
					ganadorPartida = player.getId() == jugador1.getId() ? GameWinner.SECOND_PLAYER : GameWinner.FIRST_PLAYER;
					message = "You have no sarcinas left";
				} else {
					ganadorPartida = GameWinner.DRAW;
					message = "Neither player has any sarcinas left";
				}
			}
		}
		return message;
	}
	
	
	public void binaryPhase(Jugador player1, Jugador player2) {
		for(int i=0; i<NUMBER_OF_DISKS; i++) {
			Integer numberOfBacteriaOfPlayer1 = getDiscos().get(i).getNumBact1();
			Integer numberOfBacteriaOfPlayer2 = getDiscos().get(i).getNumBact2();
			Integer numberOfSarcinaOfPlayer1 = getDiscos().get(i).getNumSarc1();
			Integer numberOfSarcinaOfPlayer2 = getDiscos().get(i).getNumSarc2();
			if(numberOfBacteriaOfPlayer1>0 && (numberOfBacteriaOfPlayer1-numberOfBacteriaOfPlayer2 == numberOfBacteriaOfPlayer1)
					&& numberOfSarcinaOfPlayer2 == 0) { // solo hay bacterias del jugador 1
				getDiscos().get(i).annadirBacterias(PRIMER_JUGADOR-1, 1);
				this.numberOfBacteriaOfPlayer1--;
				checkToAddSarcina(PRIMER_JUGADOR-1, player1, i);
			} else if(numberOfBacteriaOfPlayer2>0 && numberOfBacteriaOfPlayer2-numberOfBacteriaOfPlayer1 == numberOfBacteriaOfPlayer2
					&& numberOfSarcinaOfPlayer1 == 0) { // solo hay bacterias del jugador 2
				getDiscos().get(i).annadirBacterias(SEGUNDO_JUGADOR-1, 1);
				this.numberOfBacteriaOfPlayer2--;
				checkToAddSarcina(SEGUNDO_JUGADOR-1, player2, i);
			}
		}
	}
	
	public String pollutionPhase(Jugador player1, Jugador player2) {
		String message = "";
		Integer i = 0;
		while(i < NUMBER_OF_DISKS && ganadorPartida == GameWinner.UNDEFINED) {
			Integer numberOfBacteriaOfPlayer1 = getDiscos().get(i).getNumBact1();
			Integer numberOfBacteriaOfPlayer2 = getDiscos().get(i).getNumBact2();
			Integer numberOfSarcinaOfPlayer1 = getDiscos().get(i).getNumSarc1();
			Integer numberOfSarcinaOfPlayer2 = getDiscos().get(i).getNumSarc2();
			if((numberOfSarcinaOfPlayer1*5 + numberOfBacteriaOfPlayer1)>(numberOfSarcinaOfPlayer2*5 + numberOfBacteriaOfPlayer2)) {
				if(contaminationNumberOfPlayer1 < 9) {
					contaminationNumberOfPlayer1++;
				}
				
			} else if((numberOfSarcinaOfPlayer1*5 + numberOfBacteriaOfPlayer1)<(numberOfSarcinaOfPlayer2*5 + numberOfBacteriaOfPlayer2)) {
				if(contaminationNumberOfPlayer2 < 9) {
					contaminationNumberOfPlayer2++;
				}
			}
			i++;
		}
		if(contaminationNumberOfPlayer1 == 9 || contaminationNumberOfPlayer2 == 9) {
			if(contaminationNumberOfPlayer1 > contaminationNumberOfPlayer2) {
				ganadorPartida = GameWinner.SECOND_PLAYER;
				message = "The number of contamination has determined the winner";
			} else if(contaminationNumberOfPlayer2 > contaminationNumberOfPlayer1) {
				ganadorPartida = GameWinner.FIRST_PLAYER;
				message = "The number of contamination has determined the winner";
			} else {
				message = determineWinner();
			}
		}
		return message;
	}
	
	public String determineWinner() {
		String message = "";
		if(contaminationNumberOfPlayer1 > contaminationNumberOfPlayer2) {
			ganadorPartida = GameWinner.SECOND_PLAYER;
			message = "The number of contamination has determined the winner";
		} else if (contaminationNumberOfPlayer2 > contaminationNumberOfPlayer1) {
			ganadorPartida = GameWinner.FIRST_PLAYER;
			message = "The number of contamination has determined the winner";
		} else {
			if(totalNumberOfTokens()[0] > totalNumberOfTokens()[1]) {
				ganadorPartida = GameWinner.SECOND_PLAYER;
				message = "The total number of tokens has determined the winner.";
			} else if(totalNumberOfTokens()[1] > totalNumberOfTokens()[0]) {
				ganadorPartida = GameWinner.FIRST_PLAYER;
				message = "The total number of tokens has determined the winner.";
			} else {
				if(totalNumberOfSarcines()[0] > totalNumberOfSarcines()[1]) {
					ganadorPartida = GameWinner.SECOND_PLAYER;
					message = "The total number of sarcinas has determined the winner.";
				} else if(totalNumberOfSarcines()[1] > totalNumberOfSarcines()[0]) {
					ganadorPartida = GameWinner.FIRST_PLAYER;
					message = "The total number of sarcinas has determined the winner.";
				} else {
					ganadorPartida = GameWinner.DRAW;
					message = "No winner can be determined";
				}
			}
		}
		return message;
	}
	
	public Integer[] totalNumberOfTokens() {
		Integer player1Fiches = 0;
		Integer player2Fiches = 0;
		for(int i = 0; i<NUMBER_OF_DISKS; i++) {
			player1Fiches += getDiscos().get(i).getNumBact1() + getDiscos().get(i).getNumSarc1();
			player2Fiches += getDiscos().get(i).getNumBact2() + getDiscos().get(i).getNumSarc2();
		}
		return new Integer[] {player1Fiches, player2Fiches};
	}
	
	public Integer[] totalNumberOfSarcines() {
		Integer player1Sarcines = 0;
		Integer player2Sarcines = 0;
		for(int i = 0; i<NUMBER_OF_DISKS; i++) {
			player1Sarcines += getDiscos().get(i).getNumSarc1();
			player2Sarcines += getDiscos().get(i).getNumSarc2();
		}
		return new Integer[] {player1Sarcines, player2Sarcines};
	}
	
	public Boolean turnoPrimerJugador() {
		return getTurns().get(getTurn()).equals("PROPAGATION_RED_PLAYER");
	}
	
	public Integer getIdJugadorTurnoActual(){
		return turnoPrimerJugador() ? PRIMER_JUGADOR : SEGUNDO_JUGADOR;
	}
	
	public void copyTransientData(Match aux) {
		this.deDisco = aux.getDeDisco();
		this.disco1 = aux.getDisco1();
		this.disco2 = aux.getDisco2();
		this.disco3 = aux.getDisco3();
		this.disco4 = aux.getDisco4();
		this.disco5 = aux.getDisco5();
		this.disco6 = aux.getDisco6();
		this.disco7 = aux.getDisco7();
	}
	
	public Boolean esPropagacion() {
		return getTurns().get(this.getTurn()).startsWith("PROPAGATION");
	}
	public Boolean esFin() {
		return getTurns().get(this.getTurn()).equals("FIN");
	}
	public Boolean esFaseBinaria() {
		return getTurns().get(this.getTurn()).equals("BINARY");
	}
	public Boolean esFaseContaminacion() {
		return getTurns().get(this.getTurn()).equals("POLLUTION");
	}
	
	public Integer totalMoves() {
		Integer result = 0;
		for(int i = 0; i < discos.size(); i++) {
			result += discos.get(i).getNumMov1() + discos.get(i).getNumMov2();
		}
		return result;
	}
	
	// Devuelve el disco con más movimientos y cuántos movimientos se han realizado a él
	public Integer[] dishWithMoreMovements() {
		Integer[] result = new Integer[] {0, 0};
		for (int i = 0; i < discos.size(); i++) {
			if(discos.get(i).getNumMov1() + discos.get(i).getNumMov2() > result[1]) {
				result[0] = i;
				result[1] = discos.get(i).getNumMov1() + discos.get(i).getNumMov2();
			}
		}
		return result;
	}
	
	public long durationInMinutes() {
		return inicioPartida.until(finPartida, ChronoUnit.MINUTES);
	}
	
	public long getMatchTime() {
		return inicioPartida.until(LocalDateTime.now(), ChronoUnit.MINUTES);
	}
	
}