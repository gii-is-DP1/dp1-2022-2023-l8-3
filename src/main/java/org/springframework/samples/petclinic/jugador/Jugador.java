package org.springframework.samples.petclinic.jugador;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.petclinic.comentario.Comentario;
import org.springframework.samples.petclinic.invitacion.Invitation;
import org.springframework.samples.petclinic.statistics.Achievement;
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

	@Column(name = "estado_Online")
	@NotEmpty
	private boolean estadoOnline;

	@Column(name = "numero_de_Contaminacion")
	@NotEmpty
	private Integer numeroDeContaminacion;

	@Column(name = "numero_de_bacterias")
	@NotEmpty
	private Integer bacterias;
	
	@Column(name = "numero_de_sarcinas")
	@NotEmpty
	private Integer sarcinas;
	
	
	
	@Override
	public String toString() {
		return "Jugador [estadoOnline=" + estadoOnline + ", numeroDeContaminacion=" + numeroDeContaminacion
				+ ", bacterias=" + bacterias + ", sarcinas=" + sarcinas + ", user=" + user + "]";
	}



	@ManyToOne
	@JoinColumn(name = "username",referencedColumnName = "username")
	private User user;
	
	@OneToMany
	private List<Match> invitacionesPartidaRecibidas;
	

	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "lista_amigos", joinColumns = @JoinColumn(name = "id_jugador1"),
	inverseJoinColumns = @JoinColumn(name = "id_jugador2"))
	private List<Jugador> listaAmigos;
	
	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy="jugador")
//	private List<Comentario> comentario;
	
//	@ManyToMany(cascade = CascadeType.ALL, mappedBy="jugadores")
//	private List<Logro> logro;
//	
//	@ManyToMany(cascade = CascadeType.ALL, mappedBy="espectador")
//	private List<Partida> partidasComoEspectador;
	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy="jugador")
//	private List<Invitacion> invitacionesPartidaEnviadas;
//	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy="anfitrion")
//	private List<Partida> partidasSiendoAnfitrion;
//	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy="invitado")
//	private List<Partida> partidasSiendoinvitado;
	
	
	
		
}
