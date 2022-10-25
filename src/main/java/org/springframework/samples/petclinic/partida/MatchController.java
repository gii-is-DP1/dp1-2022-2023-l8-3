package org.springframework.samples.petclinic.partida;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
}
