package org.springframework.samples.petclinic.partida;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
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
import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "matches")
@DynamicUpdate
public class Match extends BaseEntity{
	private static final int NUMBER_OF_TURNS = 4;
	private static final int NUMBER_OF_DISKS = 7;
	private static final Integer PRIMER_JUGADOR = 1;
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
	private List<Jugador> espectadores;
	
	@OneToMany(mappedBy="match")
	private List<Invitacion> invitaciones;
	
	@Column(name = "ganador_de_partida")
	@Enumerated(EnumType.STRING)
	private GameWinner ganadorPartida;	
	
	@OneToMany(mappedBy="match")
	private List<Comentario> comentarios;


	// Constructor para cuando se crea una partida desde la aplicación
	public Match(Boolean esPrivada, Jugador jugadorAnfitrion) {
		this.inicioPartida = LocalDateTime.now();
		this.esPrivada = esPrivada;
		this.jugador1 = jugadorAnfitrion;
		this.espectadores = new ArrayList<Jugador>();
		this.invitaciones = new ArrayList<Invitacion>();
		this.comentarios = new ArrayList<Comentario>();
		this.ganadorPartida = GameWinner.UNDEFINED;
		this.turn = 0;
		createDisks();
		createTurns();
		initializateMap();
	}
	
	// Constructor para cuando se crea una partida en el script SQL
	public Match() {
		this.espectadores = new ArrayList<Jugador>();
		this.invitaciones = new ArrayList<Invitacion>();
		this.comentarios = new ArrayList<Comentario>();
		this.turn = 0;
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
	
	// ----------------------------------------------------------------------------------------------- //
	
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
	
	public Integer[] getTargetDiskAndNumberOfBacteria() {
		Integer[] disks = getDiskMoves();
		Integer i = 0;
		Integer targetDiskId = -1;
		Integer numberOfBacteria = 0;
		while(i < NUMBER_OF_DISKS && targetDiskId <0) {
			if(disks[i] > 0) {
				targetDiskId = i;
				numberOfBacteria = disks[i];
			}
			i++;
		}
		return new Integer[] {targetDiskId, numberOfBacteria};
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
	private String legalMove(Integer discoOrigen, Integer discoDestino, Integer valor,Integer jugador) {
		Disco dDestino = getDisco(discoDestino-1);
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
	}
	
	//Valida movimiento y devuelve "" si todo correcto o un msg si ha habido un error.
	public String validateMove() {
		Integer[] disks = getDiskMoves();

		//Debe haber un unico disco origen
		if(getDeDisco().length != 1) return "Más de un disco origen o ningu";

		Integer jugador = getIdJugadorTurnoActual();

		Integer origen =  getDeDisco()[0];
		Integer numDiscosOrigen = 0; //Numero de discos a donde hay movimiento posible
		Integer numDiscosOrigenConCero = 0;//Numero de discos a donde hay movimiento posible con mov=0 (no es movimiento)
		Integer sumaValores = 0;//Numero total de bacterias a quitar de origen

		for(int destino=1; destino <= NUMBER_OF_DISKS; destino++) {
			if(diskINextToDiskJ(origen,destino)) {
				Integer valor = disks[destino-1];//valor = numero bacterias a sumar a disco destino

				//Valores permitidos [0,4]
				if(valor<0 || valor>=5) { 
					System.out.println("Valor distinto de [0,4]");
					return "Valor de bacterias no permitido";
				}

				if(valor == 0) numDiscosOrigenConCero++;
				//Reglas mas complejas 
				else {
					String reglasComplejas = legalMove(origen,destino,valor,jugador);
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
		if(numDiscosOrigen == numDiscosOrigenConCero) { 
			System.out.println("No hay ningun movimiento indicado");
			return "No se indicó ningun movimiento";
		}
		//Si quedan bacterias negativas en origen el mov es ilegal
		if((getDisco(origen-1).getNumeroDeBacterias(jugador)-sumaValores)<0) { 
			String msg = "Bacterias negativas en origen:"+origen;
			System.out.println(msg);
			return msg;
		}
		
		return "";
	}
	
	public void movingBacteria(Integer playerId, Integer initialDiskId, Integer targetDiskId, 
			Integer numberOfBacteriaDisplaced) {
		//TU SIMPLEMENTE HAZ MOVIMIENTO NO VALIDES. Also tienes que modificar sarcinas cuando se necesite
		
		/*Boolean correctMovement = true;	// TODO: mensaje para que el usuario sepa por qué su movimiento no es correcto
		if(!(getDisco(targetDiskId).getNumeroDeBacterias(playerId) + numberOfBacteriaDisplaced > 5)) {

			getDisco(initialDiskId).eliminarBacterias(playerId, numberOfBacteriaDisplaced);
			getDisco(targetDiskId).annadirBacterias(playerId, numberOfBacteriaDisplaced);

			if(getDisco(initialDiskId).getNumeroDeBacterias(PRIMER_JUGADOR) == getDisco(initialDiskId).getNumeroDeBacterias(SEGUNDO_JUGADOR) ||
					getDisco(targetDiskId).getNumeroDeBacterias(PRIMER_JUGADOR) == getDisco(targetDiskId).getNumeroDeBacterias(SEGUNDO_JUGADOR)) {
				correctMovement = false; // no puede haber el mismo número de bacterias de cada jugador en ningún disco
			} else {
				checkToAddSarcina(playerId, targetDiskId);
			}
		} else {
			correctMovement = false; // no puede haber más de 5 bacterias en un mismo disco
		}
		return correctMovement;*/
		
		System.out.println("haciendo movimiento: Origen: "+initialDiskId+"; Destino: "+
				targetDiskId+ "Numero de bacterias a mover: "+numberOfBacteriaDisplaced);
	}
	
	private void checkToAddSarcina(Integer playerId, Integer diskId) {
		if(getDisco(diskId).getNumeroDeBacterias(playerId) == 5) {
			getDisco(diskId).eliminarBacterias(playerId, 5);
			getDisco(diskId).annadirSarcina(playerId);
		}
	}
	
	public Boolean turnoPrimerJugador() {
		return getTurns().get(this.getTurn()).equals("PROPAGATION_RED_PLAYER");
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
}