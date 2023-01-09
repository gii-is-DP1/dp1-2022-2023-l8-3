package org.springframework.samples.petclinic.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.samples.petclinic.comentario.Comentario;
import org.springframework.samples.petclinic.disco.Disco;
import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.partida.GameWinner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private Integer id;
	private String name;
	private Integer disco1, disco2, disco3, disco4, disco5, disco6, disco7;
	private Integer[] deDisco;
	private String movimiento;
	private LocalDateTime inicioPartida;
	private LocalDateTime finPartida;
	private Integer contaminationNumberOfPlayer1;
	private Integer contaminationNumberOfPlayer2;
	private Integer numberOfBacteriaOfPlayer1;
	private Integer numberOfBacteriaOfPlayer2;
	private Integer numberOfSarcinaOfPlayer1;
	private Integer numberOfSarcinaOfPlayer2;
	private Boolean esPrivada;
	private Integer turn;
	private List<String> turns;
	private List<Disco> discos;
	private Jugador jugador1;
	private Jugador jugador2;
	private Set<Jugador> espectadores;
	private List<Invitacion> invitaciones;
	private GameWinner ganadorPartida;
	private List<Comentario> comentarios;
	private Boolean abandonada;
	
}
