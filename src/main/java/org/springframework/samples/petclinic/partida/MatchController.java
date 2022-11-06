package org.springframework.samples.petclinic.partida;



import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.disco.Disco;
import org.springframework.samples.petclinic.statistics.Achievement;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/matches")
public class MatchController {
	
	private static final String CURRENT_MATCH_VIEW = "/matches/currentMatch";
	private static final String CREATE_MATCH_VIEW = "/matches/createMatch";
	private static final String WAIT_MATCH_VIEW = "/matches/waitForMatch";

	private MatchService matchService;
	
	@Autowired
	public MatchController(MatchService matchService) {
		this.matchService = matchService;
	}
	
//Usar estas dos funciones cuando creeis el crear partida bien
	@GetMapping(value = "/createMatch")
	public ModelAndView createNewMatch() {
		ModelAndView result = new ModelAndView(CREATE_MATCH_VIEW);
		Match match = new Match(false, null); //Hay que poner el jugador aqui!!!
		result.addObject("match", match);
		return result;
	}
	@PostMapping(value = "/createMatch")
	public ModelAndView createMatch(@Valid Match match, BindingResult br ) {
		ModelAndView result;
		if(br.hasErrors()) {
			result = new ModelAndView(CREATE_MATCH_VIEW, br.getModel());
			result.addObject("match", match);
		} else {
//			guardar match
			result = new ModelAndView(WAIT_MATCH_VIEW, br.getModel());
			result.addObject("match", match);
		}
		return result;
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

	// NOTA: Crear un controlador POST para cuando el usuario pulse "MOVE BACTERIA" y otro para cuando
	// el usuario pase a la siguiente fase. El POST llamará al método moveBacteria.
	@PostMapping("/{idMatch}/currentMatch")
	public RedirectView nextPhase(@PathVariable int idMatch, Match auxMatch) {
		RedirectView result = new RedirectView();
		Match match = matchService.getMatchById(idMatch);
		
		//Hay que programar otras formas de terminar partida
		if(match.getTurns().get(match.getTurn()).equals("FIN")) {
			result = new RedirectView("/matches/{idMatch}/completedMatch");
		} else {
			if(match.getTurns().get(match.getTurn()).startsWith("PROPAGATION")) {
				System.out.println("Validando");
				//Validar movimiento
				auxMatch.setDiscos(match.getDiscos());
				Boolean movimientoValido = auxMatch.validateMove();

				if(movimientoValido) {
					//Hacer movimiento (haz metodo auxiliar)
					Integer targetDiskId = auxMatch.getTargetDiskAndNumberOfBacteria()[0];
					Integer numberOfBacteria = auxMatch.getTargetDiskAndNumberOfBacteria()[1];
					Integer initialDiskId = auxMatch.getDeDisco()[0]-1;
//					if(match.getTurns().get(match.getTurn()).endsWith("RED_PLAYER")) {
					if(auxMatch.turnoPrimerJugador())
						match.movingBacteria(1, initialDiskId, targetDiskId, numberOfBacteria);
//					} else if(match.getTurns().get(match.getTurn()).endsWith("BLUE_PLAYER")) {
					else {
						match.movingBacteria(2, initialDiskId, targetDiskId, numberOfBacteria);
					}

					match.getDiscos().forEach(a->System.out.println(a.getNumBact1()+" "+a.getNumBact2()));

					//Pasa turno
					match.nextTurn();
					matchService.saveMatch(match);
				}
			}else {
				System.out.println("Validación falló");
			}
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