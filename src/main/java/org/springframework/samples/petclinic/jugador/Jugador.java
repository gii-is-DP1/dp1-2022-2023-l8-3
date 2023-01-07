package org.springframework.samples.petclinic.jugador;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.samples.petclinic.statistics.Achievement;
import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "jugadores")
public class Jugador extends Person {
	@Column(name = "estado_Online")
	private Boolean estadoOnline;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username")
	private User user;

	@ManyToMany(cascade = CascadeType.ALL,mappedBy = "jugador")
	private List<Invitacion> invitacionesPartidaRecibidas;

	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "amigosInvitados", joinColumns = @JoinColumn(name = "idJugador1"), inverseJoinColumns = @JoinColumn(name = "idJugador2"))
	private List<Jugador> amigosInvitados;
	
	@ElementCollection
	private List<String> tipoDeInvitacionPartidaEnviada; //el indice de cada elemento se corresponde con el indice del jugador invitado en la lista amigos invitados

	@OneToMany(mappedBy = "jugador1")
	private List<FriendRequest> sentFriendRequests;

	@OneToMany(mappedBy = "jugador2")
	private List<FriendRequest> receivedFriendRequests;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "achievements_players", joinColumns = @JoinColumn(name = "players_id"), inverseJoinColumns = @JoinColumn(name = "achievement_id"))
	private List<Achievement> logros;

	public Jugador() {

	}

	public Jugador(String firstName, String lastName, User user, Boolean estadoOnline) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.user = user;
		this.estadoOnline = estadoOnline;
		this.invitacionesPartidaRecibidas = new ArrayList<Invitacion>();
		this.sentFriendRequests = new ArrayList<FriendRequest>();
		this.receivedFriendRequests = new ArrayList<FriendRequest>();
		this.logros = new ArrayList<Achievement>();
	}

	public List<Jugador> playerFriends() {
		List<Jugador> res = new ArrayList<>();
		System.out.println("pene1");
		for (FriendRequest r : sentFriendRequests) {
			if (r.getResultado() != null && r.getResultado()) {
				res.add(r.getJugador2());
			}

		}
		System.out.println("pene2");

		for (FriendRequest r : receivedFriendRequests) {
			if (r.getResultado() != null && r.getResultado()) {
				res.add(r.getJugador1());
			}

		}
		System.out.println("pene3");

		return res;
	}
	
	public List<Jugador> playersWhoHaveSentYouAFriendRequest() {
		List<Jugador> res = new ArrayList<Jugador>();
		for(FriendRequest r : receivedFriendRequests) {
			if(r.getResultado() == null) {
				res.add(r.getJugador1());
			}
		}
		return res;
	}
   


}
