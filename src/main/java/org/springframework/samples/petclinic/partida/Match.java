package org.springframework.samples.petclinic.partida;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
	private static final Integer PRIMER_JUGADOR = 0;
	private static final int SEGUNDO_JUGADOR = 1;
	
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "inicio_de_partida")
	private LocalDateTime inicioPartida;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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
	}
	
	// Constructor para cuando se crea una partida en el script SQL
	public Match() {
		this.espectadores = new ArrayList<Jugador>();
		this.invitaciones = new ArrayList<Invitacion>();
		this.comentarios = new ArrayList<Comentario>();
		createDisks();
		createTurns();
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
	
	public List<Disco> getDiscos() {
		return discos;
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
	
	// ----------------------------------------------------------------------------------------------- //
	
	public void nextTurn() {
		turn++;
	}
	
	private Boolean movingBacteria(Integer playerId, Integer initialDiskId, Integer targetDiskId, Integer numberOfBacteriaDisplaced) {
		Boolean correctMovement = true;	// TODO: mensaje para que el usuario sepa por qué su movimiento no es correcto
		
		if(initialDiskId == targetDiskId-1 || initialDiskId == targetDiskId+1) {
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
		} else if (initialDiskId == targetDiskId) {
			correctMovement = false; // las bacterias ya están en ese disco
		} else {
			correctMovement = false; // el movimiento debe ser a discos adyacentes
		}
		return correctMovement;
	}
	
	private void checkToAddSarcina(Integer playerId, Integer diskId) {
		if(getDisco(diskId).getNumeroDeBacterias(playerId) == 5) {
			getDisco(diskId).eliminarBacterias(playerId, 5);
			getDisco(diskId).annadirSarcina(playerId);
		}
	}
	
	// ----------------------------------------------------------------------------------------------- //
	
}
