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
import javax.persistence.OrderColumn;
import javax.persistence.Table;

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
@Table(name = "partidas")
public class Match extends BaseEntity{
	private static final int NUMBER_OF_DISKS = 7;
	private static final Integer PRIMER_JUGADOR = 0;
	private static final int SEGUNDO_JUGADOR = 1;
	
	/*@Column(name = "siguiente_movimiento")
	private String[] sigMov; //La idea es poner aqui el movimiento de una manera pero no consigo que me funcione, puede que use otra forma al final
	*/
	
	@Column(name = "turno_primer_jugador")
	private Boolean turnoJugador1;  //Creo que esto es necesario
	
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	@Column(name = "inicio_de_partida")
	private LocalDateTime inicioPartida;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss.69")
    @Column(name = "fin_de_partida")
	private LocalDateTime finPartida;

	@Column(name = "es_privada")
	private Boolean esPrivada;
	
	@OneToMany(mappedBy="id")
	private List<Disco> discos;
	
	@ManyToOne
	@JoinColumn(name = "id_jugador1")
	private Jugador jugador1;
	
	@ManyToOne
	@JoinColumn(name = "id_jugador2")
	private Jugador jugador2;

	@ManyToMany
	private List<Jugador> espectadores;
	
	@OneToMany(mappedBy="id")
	private List<Invitacion> invitaciones;
	
	@Column(name = "ganador_de_partida")
	@Enumerated(EnumType.STRING)
	private GameWinner ganadorPartida;	
	
	@OneToMany(mappedBy="id")
	private List<Comentario> comentarios;

	
	public Match(Boolean esPrivada, Jugador jugadorAnfitrion) {
		this.inicioPartida = LocalDateTime.now();
		this.esPrivada = esPrivada;
		this.jugador1 = jugadorAnfitrion;
		this.espectadores = new ArrayList<Jugador>();
		this.invitaciones = new ArrayList<Invitacion>();
		this.comentarios = new ArrayList<Comentario>();
		this.ganadorPartida = GameWinner.UNDEFINED;
		createDisks();
	}
	
	private void createDisks() {
		discos = new ArrayList<Disco>();
		for(int i = 0; i<NUMBER_OF_DISKS; i++) {
			discos.add(new Disco(this));
		}
	}
	
	// ----------------------------------------------------------------------------------------------- //
	
	public List<Disco> getDiscos() {
		return discos;
	}
	
	public Disco getDisco(Integer diskId) {
		return discos.get(diskId);
	}
	
	// ----------------------------------------------------------------------------------------------- //
	
	public String chooseTag(int i){
		if(i==0) return "col23";
		else if(i==1) return "col45";
		else if(i==2) return "row2";
		else if(i==3) return "row2";
		else if(i==4) return "row2";
		else if(i==5) return "col23 row3";
		else return "col45 row3";
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
