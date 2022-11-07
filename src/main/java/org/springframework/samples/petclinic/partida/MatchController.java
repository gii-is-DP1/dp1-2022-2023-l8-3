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
		Match match = new Match(false, null); //Hay que poner el jugador aqui!!! (creo, no entiendo los constructores en spring)
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
			// EN waitForMatch.jsp se redirige al match con id=1, no al match creado (pq no esta implementado eso)
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
		
//		Si es propagacion no se hace nada pq las propagaciones van al PostMapping()
		
		if(match.esFaseBinaria()) {
			System.out.println("FASE BINARIA");
			match.nextTurn();

		} else if(match.esFaseContaminacion()) {
			System.out.println("FASE CONTAMINACION");

			match.nextTurn();

		} else if(match.esFin()){ //El fin se controla aqui no en el PostMapping()
			System.out.println("FIN");
		}
		
		return result;
	}
	

	@PostMapping("/{idMatch}/currentMatch")
	public ModelAndView nextPhase(@PathVariable int idMatch, Match auxMatch) {
		ModelAndView result = new ModelAndView(CURRENT_MATCH_VIEW);
		Match match = matchService.getMatchById(idMatch);
				
		if(match.esPropagacion()) {
			System.out.println("FASE PROPAGACION");
			System.out.println("Validando");

			match.copyTransientData(auxMatch);
			//Si es "" es correcto. Si tiene un mensaje es un msg de error
			String validacion = "NO";//match.validateMove();
			result.addObject("error", validacion);

			if(validacion.length()==0) {
				//Hacer movimiento (haz metodo auxiliar)
				Integer targetDiskId = auxMatch.getTargetDiskAndNumberOfBacteria()[0];
				Integer numberOfBacteria = auxMatch.getTargetDiskAndNumberOfBacteria()[1];
				Integer initialDiskId = auxMatch.getDeDisco()[0]-1;
				
				if(auxMatch.turnoPrimerJugador())
					match.movingBacteria(1, initialDiskId, targetDiskId, numberOfBacteria);
				else {
					match.movingBacteria(2, initialDiskId, targetDiskId, numberOfBacteria);
				}

				match.getDiscos().forEach(a->System.out.println(a.getNumBact1()+" "+a.getNumBact2()));

				//Pasa turno
				match.nextTurn();
				matchService.saveMatch(match);
			}
			else {
				System.out.println("Validación falló");
			}
		}
		else {
			System.out.println("NO ES PROPAGACIÓN PERO HUBO POST");
		}
		
		result.addObject("match", match);
			
			
		return result;
	}
	

	@GetMapping(value = "/{idMatch}/completedMatch")
	public ModelAndView completedMatch(@PathVariable int idMatch) {
		ModelAndView result = new ModelAndView("/matches/completedMatch"); // aún no está hecha la vista
		result.addObject("match", matchService.getMatchById(idMatch));
		return result;
	}
	

}