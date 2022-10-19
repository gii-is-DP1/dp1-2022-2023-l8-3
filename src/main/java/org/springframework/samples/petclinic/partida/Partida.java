package org.springframework.samples.petclinic.partida;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
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
public class Partida extends BaseEntity{
	
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "inicio_de_partida")
	private LocalDateTime inicioPartida;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "fin_de_partida")
	private LocalDateTime finPartida;

	@Column(name = "es_privada")
	private Boolean esPrivada;
	
    @OneToMany(mappedBy="discos")
	private Disco[] discos;
	
	@Column(name = "primer_jugador")
	@ManyToOne()
	private Jugador jugador1;
	
	@Column(name = "segundo_jugador")
	@ManyToOne()
	private Jugador jugador2;

	@ManyToMany(mappedBy="espectador")
	private List<Jugador> espectadores;
	
	@OneToMany(mappedBy="invitacion")
	private List<Invitacion> invitaciones;
	
	@Column(name = "ganador_de_partida")
	@OneToOne
	private GanadorPartida ganadorPartida;	
	
	/*
	//Falta comentario
	 * 
	@OneToMany(mappedBy="comentario")
	private List<Comentario> comentarios;
	
	*/
}
