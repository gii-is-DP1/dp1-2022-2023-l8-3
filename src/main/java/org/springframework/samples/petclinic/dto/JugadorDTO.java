package org.springframework.samples.petclinic.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.jugador.FriendRequest;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.statistics.Achievement;
import org.springframework.samples.petclinic.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JugadorDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private Integer id;
	private String firstName;
	private String lastName;
	private Boolean estadoOnline;
	private User user;
	private List<Invitacion> invitacionesPartidaRecibidas;
	private List<Jugador> amigosInvitados;
	private List<String> tipoDeInvitacionPartidaEnviada;
	private List<FriendRequest> sentFriendRequests;
	private List<FriendRequest> receivedFriendRequests;
	private List<Match> gamesAsHost;
	private List<Match> gamesAsGuest;
	private List<Achievement> logros;
	
}
