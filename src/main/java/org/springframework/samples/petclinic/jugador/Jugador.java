package org.springframework.samples.petclinic.jugador;

import java.util.ArrayList;
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

	@Column(name="estado_Online")
	private Boolean estadoOnline;
	
	@Transient
	private Integer numeroDeContaminacion=0;
	
	@Transient
	private Integer bacterias=20;

	@Transient
	private Integer sarcinas=4;
	
	
	
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
	
	
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "lista_amigos", joinColumns = @JoinColumn(name = "id_jugador1"),
	inverseJoinColumns = @JoinColumn(name = "id_jugador2"))
	private List<Jugador> listaAmigos;
	
	
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "achievements_players", joinColumns = @JoinColumn(name = "players_id"),
	inverseJoinColumns = @JoinColumn(name = "achievement_id"))
	private List<Achievement> logros;
	
	public Jugador() {
		
	}
	
	public Jugador(String firstName, String lastName, User user) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.user = user;
		this.invitacionesPartidaRecibidas = new ArrayList<Match>();
		this.listaAmigos = new ArrayList<Jugador>();
		this.logros = new ArrayList<Achievement>();
	}
		
}
