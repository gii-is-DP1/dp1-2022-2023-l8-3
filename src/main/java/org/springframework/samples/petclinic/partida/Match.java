package org.springframework.samples.petclinic.partida;

import java.time.LocalDateTime;
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
	
	// TODO
	private Disco[] discos;
	
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
	
	// ----------------------------------------------------------------------------------------------- //
	
	public Disco[] getDiscos() {
		return discos;
	}
	
	public Disco getDisco(Integer diskId) {
		return discos[diskId];
	}
	
	// ----------------------------------------------------------------------------------------------- //
	
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
