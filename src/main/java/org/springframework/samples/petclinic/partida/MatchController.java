package org.springframework.samples.petclinic.partida;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/matches")
public class MatchController {
	
	private static final String CURRENT_MATCH_VIEW = "/matches/currentMatch";
	private MatchService matchService;
	
	@Autowired
	public MatchController(MatchService matchService) {
		this.matchService = matchService;
	}
	
	@GetMapping(value = "/{idMatch}/currentMatch")
	public ModelAndView showCurrentMatch(@PathVariable int idMatch) {
		ModelAndView result = new ModelAndView(CURRENT_MATCH_VIEW);
		Match match = matchService.getMatchById(idMatch);
		result.addObject("match", match);
		if(match.getTurn() == 0 || match.getTurns().get(match.getTurn()).startsWith("PROPAGATION")) {
			// redireccionar a "/{idMatch}/currentMatch/{idPlayer}/propagationPhase", siendo idPlayer el jugador al que le toque
		} else if(match.getTurns().get(match.getTurn()).equals("BINARY")) {
			// redireccionar a "/{idMatch}/currentMatch/binaryPhase"
		} else if(match.getTurns().get(match.getTurn()).equals("POLLUTION")) {
			// redireccionar a "/{idMatch}/currentMatch/pollutionPhase"
		} else {
			// final del juego
		}
		return result;
	}
	// NOTA: Crear un controlador POST para cuando el usuario pulse "MOVE BACTERIA" y otro Request para cuando
	// el usuario pase a la siguiente fase. El POST llamará al método moveBacteria.
	@PostMapping("/{idMatch}/currentMatch")
	public RedirectView nextPhase(@PathVariable int idMatch, Match auxMatch) {
		RedirectView result = new RedirectView();
		Match match = matchService.getMatchById(idMatch);
		
		if(match.getTurns().get(match.getTurn()).equals("FIN")) {
			result = new RedirectView("/matches/{idMatch}/completedMatch");
		} else {
			if(match.getTurns().get(match.getTurn()).startsWith("PROPAGATION")) {
				result.addStaticAttribute("bacterias", auxMatch.getBacteriasAmover());
				result.addStaticAttribute("discos", auxMatch.getADisco());
			}
			match.nextTurn();
			matchService.saveMatch(match);
			result.setUrl("/matches/{idMatch}/currentMatch");
		}
		return result;
	}
	
	@GetMapping(value = "/{idMatch}/completedMatch")
	public ModelAndView completedMatch(@PathVariable int idMatch) {
		ModelAndView result = new ModelAndView("/matches/completedMatch"); // aún no está hecha la vista
		result.addObject("match", matchService.getMatchById(idMatch));
		return result;
	}
	
}
