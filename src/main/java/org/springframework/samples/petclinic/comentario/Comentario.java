package org.springframework.samples.petclinic.comentario;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.partida.Partida;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comentarios")
public class Comentario extends NamedEntity{
	
	//---------------------------------------------------------------------------------------------//
	@Column(name = "texto")
	@NotEmpty
	private String texto;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "fecha_de_publicacion")
	private LocalDateTime fechaDePublicacion;
	
	@ManyToOne
	@JoinColumn(name = "id_jugador")
	private Jugador jugador;
	
	@ManyToOne
	@JoinColumn(name = "id_partida")
	private Partida partida;
	
	//---------------------------------------------------------------------------------------------//
	
	public String getTexto() {
		return texto;
	}
	
	public LocalDateTime getFechaDePublicacion() {
		return fechaDePublicacion;
	}
	
	public Jugador getJugador() {
		return jugador;
	}
	
	public Partida getPartida() {
		return partida;
	}
	
	//---------------------------------------------------------------------------------------------//

	@Override
	public String toString() {
		return "Comentario [texto=" + texto + ", fechaDePublicacion=" + fechaDePublicacion + ", jugador=" + jugador.getUser().getUsername()+"]";
	}
	
	
}
