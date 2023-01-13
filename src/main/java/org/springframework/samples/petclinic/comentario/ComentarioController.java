package org.springframework.samples.petclinic.comentario;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/chat")
public class ComentarioController {


	private static final int MAXIMUM_COMMENT_LENGTH = 200;
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
	public String postMsg(@PathVariable int idMatch, @AuthenticationPrincipal Authentication user,
			@RequestParam String msg){
		Integer idJugador = ps.findPlayerByUsername(user.getName()).getId();

	    Match match = ms.getMatchById(idMatch);
	    
	    boolean usuarioCorrecto = idJugador == match.getJugador1().getId() || 
	    		idJugador == match.getJugador2().getId();
	    
	    if(usuarioCorrecto && !msg.trim().equals("") && msg.trim().length() <= MAXIMUM_COMMENT_LENGTH) {
		    Jugador j = ps.findJugadorById(idJugador);
			Comentario c = new Comentario();

		    c.setMatch(match);
		    c.setFechaDePublicacion(LocalDateTime.now());
		    c.setTexto(msg);
		    c.setJugador(j);
		    
		    cs.saveComentario(c);
	    }
	    return "redirect:/matches/{idMatch}/currentMatch";
	}
}
