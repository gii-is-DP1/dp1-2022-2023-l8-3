package org.springframework.samples.petclinic.logro;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "logros")
public class Logro extends NamedEntity{
	
	
	@Column(name="metrica")
	@NotEmpty
	Metrica metrica;
	
	@Column(name="limite")
	@NotEmpty
	Integer limite;
	
	@Column(name="visibilidad")
	@NotEmpty
	Visibilidad visibilidad;
	
	@Column(name="dificultad")
	@NotEmpty
	DificultadLogro dificultad;
	
	@ManyToMany
	List<Jugador> jugadores;
	
	

}
