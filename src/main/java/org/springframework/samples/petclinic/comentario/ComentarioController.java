package org.springframework.samples.petclinic.comentario;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchRepository;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/chat")
public class ComentarioController {


	private ComentarioService cs;
	private MatchService ms;
	private PlayerService ps;

	@Autowired
	public ComentarioController(ComentarioService cs, MatchService ms, PlayerService ps) {
		this.cs = cs;
		this.ms = ms;
		this.ps = ps;
	}
	
	@PostMapping(value = "/{idMatch}/postMsg")
	public RedirectView postMsg(@PathVariable int idMatch, @AuthenticationPrincipal Authentication user,
			@RequestParam String msg, @RequestParam int idJugador){
		System.out.println("PENEEES");
		Boolean msgCorrecto = true; //Comprobamos lenguaje vulgar
		
		if(msgCorrecto) { 
			Comentario c = new Comentario();
		    Match match = ms.getMatchById(idMatch);
		    
		    c.setMatch(match);
		    c.setFechaDePublicacion(LocalDateTime.now());
		    c.setTexto(msg);
		    
		    Jugador j = ps.findJugadorById(idJugador);
		    c.setJugador(j);
		    
		    cs.saveComentario(c);
		}
		System.out.println("pitoooooss");

	    return new RedirectView("/matches/"+idMatch+"/currentMatch");
	}
}
