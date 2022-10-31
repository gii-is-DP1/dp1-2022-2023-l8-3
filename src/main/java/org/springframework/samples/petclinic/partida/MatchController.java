package org.springframework.samples.petclinic.partida;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
	public ModelAndView showCurrentMatch(@PathVariable int id) {
		ModelAndView result = new ModelAndView(CURRENT_MATCH_VIEW);
		result.addObject("match", matchService.getMatchById(id));
		return result;
	}
	
	//Temporales
	@GetMapping(value = "/currentMatch")
	public ModelAndView showCurrentMatch() {
		ModelAndView result = new ModelAndView(CURRENT_MATCH_VIEW);
		result.addObject("match", matchService.getMatchById(1));
		return result;
	}
	
	@PostMapping(value = "/{id}/currentMatch")
	public ModelAndView next(@PathVariable int id) {
		ModelAndView result = new ModelAndView(CURRENT_MATCH_VIEW);
		result.addObject("match", matchService.getMatchById(id));
		System.out.println("Movimiento");
		return result;
	}
	
}
