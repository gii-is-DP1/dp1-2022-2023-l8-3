package org.springframework.samples.petclinic.invitacion;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InvitationController {

	private InvitationService invitationService;
	private PlayerService playerService;

	@Autowired
	public InvitationController(InvitationService invitationService,  PlayerService playerService) {
		this.invitationService = invitationService;
		this.playerService=playerService;
	}
	
	@GetMapping(value="/invitarAmigo/{id}")
	public ModelAndView invitarAmigos(@PathVariable("id") int id,@AuthenticationPrincipal Authentication user) {
		ModelAndView result = new ModelAndView("redirect:/matches/createMatch");
		Jugador jugadorActual=playerService.findPlayerByUsername(user.getName());
		List<Jugador> listaAmigos=new ArrayList<>(jugadorActual.playerFriends());
		result.addObject("players",listaAmigos);
		List<Jugador> listaAmigosInvitados=new ArrayList<>();
		if(!jugadorActual.getAmigosInvitados().isEmpty()) {
			listaAmigosInvitados.addAll(jugadorActual.getAmigosInvitados());
		}

		if(!listaAmigosInvitados.contains(playerService.findJugadorById(id))) {
			listaAmigosInvitados.add(playerService.findJugadorById(id));
		}
		jugadorActual.setAmigosInvitados(listaAmigosInvitados);
		playerService.saveJugador(jugadorActual);
		result.addObject("actualPlayer", user.getName());
		result.addObject("amigosInvitados",listaAmigosInvitados);
		return result;
	}
}
