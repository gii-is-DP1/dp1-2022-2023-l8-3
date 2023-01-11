package org.springframework.samples.petclinic.jugador;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.samples.petclinic.statistics.Achievement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.partida.GameWinner;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Audited
@Table(name = "jugadores")
public class Jugador extends Person {

	private static final int MINUTES_OF_AN_HOUR = 60;

	private static final int MAXIMUM_OF_SARCINAS = 4;

	private Boolean estadoOnline;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username")
	private User user;

	@NotAudited
	@ManyToMany(cascade = CascadeType.ALL,mappedBy = "jugador")
	private List<Invitacion> invitacionesPartidaRecibidas;

	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "amigosInvitados", joinColumns = @JoinColumn(name = "idJugador1"), inverseJoinColumns = @JoinColumn(name = "idJugador2"))
	private List<Jugador> amigosInvitados;
	
	@ElementCollection
	private List<String> tipoDeInvitacionPartidaEnviada; //el indice de cada elemento se corresponde con el indice del jugador invitado en la lista amigos invitados
	
	@NotAudited
	@OneToMany(mappedBy = "jugador1")
	private List<FriendRequest> sentFriendRequests;

	@NotAudited
	@OneToMany(mappedBy = "jugador2")
	private List<FriendRequest> receivedFriendRequests;
	
	@NotAudited
	@OneToMany(mappedBy = "jugador1")
	private List<Match> gamesAsHost;
	
	@NotAudited
	@OneToMany(mappedBy = "jugador2")
	private List<Match> gamesAsGuest;
	
	@NotAudited
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
	
	public Integer getNumberOfGames() {
		Integer result = 0;
		for (Match match : gamesAsHost) {
			if(!match.getGanadorPartida().equals(GameWinner.UNDEFINED)) {
				result++;
			}
		}
		for (Match match : gamesAsGuest) {
			if(!match.getGanadorPartida().equals(GameWinner.UNDEFINED)) {
				result++;
			}
		}
		return result;
	}
	
	public Integer getNumberOfGamesWon() {
		Integer result = 0;
		for (Match match : gamesAsHost) {
			if(match.getGanadorPartida().equals(GameWinner.FIRST_PLAYER)) {
				result++;
			}
		}
		for (Match match : gamesAsGuest) {
			if(match.getGanadorPartida().equals(GameWinner.SECOND_PLAYER)) {
				result++;
			}
		}
		return result;
	}
	
	public Integer getNumberOfSarcinasPlaced() {
		Integer result = 0;
		for (Match match : gamesAsHost) {
			result += MAXIMUM_OF_SARCINAS-match.getContaminationNumberOfPlayer1();
		}
		for (Match match : gamesAsGuest) {
			result += MAXIMUM_OF_SARCINAS-match.getNumberOfSarcinaOfPlayer2();
		}
		return result;
	}
	
	public Integer getNumberOfFriends() {
		return playerFriends().size();
	}
	
	public String getTotalPlayingGame() {
		Long minutes = 0l;
		for (Match match : gamesAsHost) {
			if(!match.getGanadorPartida().equals(GameWinner.UNDEFINED)) {
				minutes +=  match.durationInMinutes();
			}
		}
		for (Match match : gamesAsGuest) {
			if(!match.getGanadorPartida().equals(GameWinner.UNDEFINED)) {
				minutes +=  match.durationInMinutes();
			}
		}
		Long hours = TimeUnit.MINUTES.toHours(minutes);
		return String.format("%2d horas y %1d minutos", hours, minutes-(hours*MINUTES_OF_AN_HOUR));
	}
	
	public Long getDurationOfTheLongestGame() {
		Long result = 0l;
		for (Match match : gamesAsHost) {
			if(!match.getGanadorPartida().equals(GameWinner.UNDEFINED)) {
				if(match.durationInMinutes() > result) {
					result = match.durationInMinutes();
				}
			}
		}
		for (Match match : gamesAsGuest) {
			if(!match.getGanadorPartida().equals(GameWinner.UNDEFINED)) {
				if(match.durationInMinutes() > result) {
					result = match.durationInMinutes();
				}
			}
		}
		return result;
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
   


}
