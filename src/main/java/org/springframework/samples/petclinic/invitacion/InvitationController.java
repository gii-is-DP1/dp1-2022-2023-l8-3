package org.springframework.samples.petclinic.invitacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.partida.GameWinner;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class InvitationController {

	private InvitationService invitationService;
	private PlayerService playerService;
	private MatchService matchService;

	@Autowired
	public InvitationController(InvitationService invitationService,  PlayerService playerService,MatchService matchService) {
		this.invitationService = invitationService;
		this.playerService=playerService;
		this.matchService=matchService;
	}
	
	public Boolean estaLibre(Jugador jugador) {
		Boolean res=true;
		for(Match m:matchService.getMatches())
			if(m.getGanadorPartida()==GameWinner.UNDEFINED && m.getFinPartida()==null && !m.getAbandonada()) {
				if(m.getJugador1()==jugador ||m.getJugador2()==jugador) {
					res=false;
				}
			}
		return res;
	}
	
	
	@GetMapping(value="/invitarAmigo/{id}")
	public ModelAndView invitarAmigos(@PathVariable("id") int id,@AuthenticationPrincipal Authentication user) {
		ModelAndView result = new ModelAndView("/matches/createMatch");
		Jugador jugadorActual=playerService.findPlayerByUsername(user.getName());
		Jugador jugadorInvitado=playerService.findJugadorById(id);
		List<Jugador> listaAmigos=new ArrayList<>(jugadorActual.playerFriends());
		result.addObject("players",listaAmigos);
		List<Jugador> listaAmigosInvitados=new ArrayList<>();
		if(!jugadorActual.getAmigosInvitados().isEmpty()) {
			listaAmigosInvitados.addAll(jugadorActual.getAmigosInvitados());
		}

		if(!listaAmigosInvitados.contains(jugadorInvitado)) {
			if(estaLibre(jugadorInvitado)) {
				listaAmigosInvitados.add(jugadorInvitado);
				result.addObject("amigoEnPartida", false);
			}
			else {
				result.addObject("amigoEnPartida", true);
				result.addObject("nombreAmigo", jugadorInvitado.getUser().getUsername());
			}
		}
		jugadorActual.setAmigosInvitados(listaAmigosInvitados);
		playerService.saveJugador(jugadorActual);
		result.addObject("actualPlayer", user.getName());
		result.addObject("amigosInvitados",listaAmigosInvitados);
		return result;
	}
	
	@PostMapping(value = "/invitarAmigo/{id}")
	public RedirectView createMatch(@RequestParam String nombre,@RequestParam Boolean tipoPartida, @AuthenticationPrincipal Authentication user) {
		    String playerName = user.getName();
	        Jugador player = playerService.findPlayerByUsername(playerName);
	        Match match = new Match(false, player);
	        match.setName(nombre);
	        match.setEsPrivada(tipoPartida);
	        match.getDisco(2).annadirBacterias(0, 1);
	        match.getDisco(4).annadirBacterias(1, 1);
	        match.setJugador1(player);
		    this.matchService.saveMatch(match);
		    int id = match.getId();
		    String matchId=String.valueOf(id);
		    RedirectView result = new RedirectView("/matches/"+matchId+"/waitForMatch");
		    
		    Jugador actualPlayer=playerService.findPlayerByUsername(playerName);
		    for(Jugador j:actualPlayer.getAmigosInvitados()) {
		    	Invitacion i=new Invitacion();
		    	i.setFechaHora(LocalDate.now());
		    	i.setJugador(j);
		    	i.setMatch(match);
		    	i.setResultado(resultadoInvitacion.SIN_RESPONDER);
		    	i.setTipo(tipoInvitacion.JUGADOR);
		    	invitationService.save(i);
		    }
		    List<Jugador>copia=actualPlayer.getAmigosInvitados();
		    copia.clear();
		    actualPlayer.setAmigosInvitados(copia);
		    playerService.saveJugador(actualPlayer);
		    return result;
	}
	
	
	@GetMapping(value="/invitacionesPendientes")
	public ModelAndView invitarAmigos(@AuthenticationPrincipal Authentication user) {
		ModelAndView result=new ModelAndView("/invitaciones/invitacionesPendientes");
		List<Invitacion> lista=invitationService.getInvitacionByInvitadoId(playerService.findPlayerByUsername(user.getName()).getId());
		result.addObject("invitaciones",lista);
		return result;
	}
	
	
	@GetMapping(value="/rechazarInvitacion/{id}")
	public String rechazarPartida(@AuthenticationPrincipal Authentication user,@PathVariable("id") Integer invitacionId) {
		Invitacion invitacion=invitationService.getInvitacionById(invitacionId);
		Jugador jugadorActual=playerService.findPlayerByUsername(user.getName());
		if(invitacion.getJugador()==jugadorActual) {
			invitationService.delete(invitacion);
		}
		return "redirect:/";
	}
	
	@GetMapping(value="/aceptarInvitacion/{id}")
	public ModelAndView aceptarPartida(@AuthenticationPrincipal Authentication user,@PathVariable("id") Integer invitacionId) {
		ModelAndView result=new ModelAndView("redirect:/");
		Invitacion invitacion=invitationService.getInvitacionById(invitacionId);
		Jugador jugadorActual=playerService.findPlayerByUsername(user.getName());
		if(invitacion.getJugador()==jugadorActual) {
			Match match=invitacion.getMatch();
			if(match.getJugador2()!=null) {
				result.setViewName("/invitaciones/invitacionesPendientes");
				result.addObject("yaHayJugador",true);
				invitationService.delete(invitacion);
				
			}
			else {
				match.setJugador2(jugadorActual);
		        matchService.saveMatch(match);
		        result.setViewName("redirect:/matches/"+match.getId()+"/currentMatch");
				invitationService.delete(invitacion);
			}
		}
		return result;
	}
	
	
	
	
	
	
	
}
