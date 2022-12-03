package org.springframework.samples.petclinic.comentario;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.partida.Match;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comentarios")
public class Comentario extends BaseEntity {
	
	//---------------------------------------------------------------------------------------------//
	@Column(name = "texto")
	@NotEmpty
	@Length(min = 1, max = 200)
	private String texto;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "fecha_de_publicacion")
	private LocalDateTime fechaDePublicacion;
	
	@ManyToOne
	@JoinColumn(name = "id_jugador")
	private Jugador jugador;
	
	@ManyToOne
	@JoinColumn(name = "match_id")
	private Match match;
	
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
	
	public Match getPartida() {
		return match;
	}
	
	//---------------------------------------------------------------------------------------------//

	@Override
	public String toString() {
		return "Comentario [texto=" + texto + ", fechaDePublicacion=" + fechaDePublicacion + ", jugador=" + jugador.getUser().getUsername()+"]";
	}
	
	public String fechaComentario() {
		LocalDateTime fecha =  this.getFechaDePublicacion();
		return fecha.getHour()+":"+fecha.getMinute()+":"+fecha.getSecond();
	}
}
