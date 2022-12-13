package org.springframework.samples.petclinic.jugador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.samples.petclinic.statistics.Achievement;
import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.user.User;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "jugadores")
public class Jugador extends Person {
	@Column(name = "estado_Online")
	private Boolean estadoOnline;

	@Column(name = "contamination_number")
	private Integer numeroDeContaminacion;

	@Column(name = "number_of_bacteria")
	private Integer bacterias;

	@Column(name = "number_of_sarcina")
	private Integer sarcinas;
	

	@Override
	public String toString() {
		return "Jugador [estadoOnline=" + estadoOnline + ", numeroDeContaminacion=" + numeroDeContaminacion
				+ ", bacterias=" + bacterias + ", sarcinas=" + sarcinas + ", user=" + user + "]";
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username")
	private User user;

	@ManyToMany(cascade = CascadeType.ALL,mappedBy = "jugador")
	private List<Invitacion> invitacionesPartidaRecibidas;

	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "amigosInvitados", joinColumns = @JoinColumn(name = "idJugador1"), inverseJoinColumns = @JoinColumn(name = "idJugador2"))
	private List<Jugador> amigosInvitados;

//	@ManyToMany(cascade = CascadeType.REMOVE)
//	@JoinTable(name = "lista_amigos", joinColumns = @JoinColumn(name = "friend_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
//	private List<Jugador> amigoDe;

	@OneToMany(mappedBy = "jugador1", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FriendRequest> sentFriendRequests;

	@OneToMany(mappedBy = "jugador2", cascade = CascadeType.ALL, orphanRemoval = true)
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
		this.numeroDeContaminacion = 0;
		this.bacterias = 20;
		this.sarcinas = 4;
		this.amigosInvitados=new ArrayList<Jugador>();
		this.invitacionesPartidaRecibidas = new ArrayList<Invitacion>();
		this.sentFriendRequests = new ArrayList<FriendRequest>();
		this.receivedFriendRequests = new ArrayList<FriendRequest>();
		this.logros = new ArrayList<Achievement>();
	}

	public void addBacteria(Integer numberOfBacteria) {
		bacterias += numberOfBacteria;
	}

	public void decreaseBacteria() {
		bacterias--;
	}

	public void decreaseSarcinas() {
		sarcinas--;
	}

	public void increseContaminationNumber() {
		numeroDeContaminacion++;
	}

	public List<Jugador> playerFriends() {
		List<Jugador> res = new ArrayList<>();
		for (FriendRequest r : sentFriendRequests) {
			if (r.getResultado() != null && r.getResultado()) {
				res.add(r.getJugador2());
			}

		}
		for (FriendRequest r : receivedFriendRequests) {
			if (r.getResultado() != null && r.getResultado()) {
				res.add(r.getJugador1());
			}

		}

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
    //inicio del contgador que sea un map fecha/numero
//	Boolean play = true;
//	LocalDate fecha;
//    public Boolean canPlayPlayer() {
//        
//    }


}
