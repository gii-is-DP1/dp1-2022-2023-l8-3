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
	
	@GetMapping(value = "/{idMatch}/completedMatch")
	public ModelAndView completedMatch(@PathVariable int idMatch) {
		ModelAndView result = new ModelAndView("/matches/completedMatch"); // aún no está hecha la vista
		result.addObject("match", matchService.getMatchById(idMatch));
		return result;
	}
	
	@RequestMapping("/{idMatch}/nextPhase")
	public RedirectView nextPhase(@PathVariable int idMatch) {
		RedirectView result;
		Match match = matchService.getMatchById(idMatch);
		if(match.getTurns().get(match.getTurn()).equals("FIN")) {
			result = new RedirectView("/matches/{idMatch}/completedMatch");
		} else {
			match.nextTurn();
			matchService.saveMatch(match);
			result = new RedirectView("/matches/{idMatch}/currentMatch");
		}
		return result;
	}
	
	//Temporales
	@GetMapping(value = "/currentMatch")
	public ModelAndView showCurrentMatch() {
		ModelAndView result = new ModelAndView(CURRENT_MATCH_VIEW);
		result.addObject("match", matchService.getMatchById(1));
		return result;
	}
	
	@PostMapping(value = "/currentMatch")
	public ModelAndView next(String movimiento) {
		ModelAndView result = new ModelAndView(CURRENT_MATCH_VIEW);
		result.addObject("match", matchService.getMatchById(1));
		System.out.println("PITO"+movimiento);
		return result;
	}
	
}
