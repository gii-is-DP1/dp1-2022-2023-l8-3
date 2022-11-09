package org.springframework.samples.petclinic.jugador;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.springframework.lang.Nullable;
import org.springframework.samples.petclinic.logro.Logro;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "jugadores")
public class Jugador extends Person{

	@Transient
	private Boolean estadoOnline;
	
	@Transient
	private Integer numeroDeContaminacion;
	
	@Transient
	private Integer bacterias;

	@Transient
	private Integer sarcinas;
	
	
	
	@Override
	public String toString() {
		return "Jugador [estadoOnline=" + estadoOnline + ", numeroDeContaminacion=" + numeroDeContaminacion
				+ ", bacterias=" + bacterias + ", sarcinas=" + sarcinas + ", user=" + user + "]";
	}



	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username",referencedColumnName = "username")
	private User user;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Match> invitacionesPartidaRecibidas;
	

	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "lista_amigos", joinColumns = @JoinColumn(name = "id_jugador1"),
	inverseJoinColumns = @JoinColumn(name = "id_jugador2"))
	private List<Jugador> listaAmigos;
	
	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy="jugador")
//	private List<Comentario> comentario;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "lista_logros", joinColumns = @JoinColumn(name = "id_jugador"),
	inverseJoinColumns = @JoinColumn(name = "id_logro"))
	private List<Logro> logros;
	
	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy="jugador")
//	private List<Invitacion> invitacionesPartidaEnviadas;
//	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy="anfitrion")
//	private List<Partida> partidasSiendoAnfitrion;
//	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy="invitado")
//	private List<Partida> partidasSiendoinvitado;
	
	
	
		
}
