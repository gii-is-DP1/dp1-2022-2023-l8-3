 package org.springframework.samples.petclinic.partida;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.disco.Disco;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.statistics.Achievement;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	private static final String MATCH_STATISTICS_VIEW = "/matches/matchStatistics";
	private static final String COMPLETED_MATCH_VIEW = "/matches/completedMatch";
	private static final String CURRENT_MATCH_VIEW = "/matches/currentMatch";
	private static final String CREATE_MATCH_VIEW = "/matches/createMatch";
	private static final String WAIT_MATCH_VIEW = "/matches/waitForMatch";
	private static final String LIST_MATCHES = "/matches/matchesList";

	private MatchService matchService;
	private PlayerService playerService;
	
	@Autowired
	public MatchController(MatchService matchService, PlayerService playerService) {
		this.matchService = matchService;
		this.playerService = playerService;
	}
	
	@GetMapping(value = "/matchesList")
    public ModelAndView listingMatch() {
        ModelAndView result = new ModelAndView(LIST_MATCHES);   

        result.addObject("match_list", matchService.getMatchWithotP2());
        result.addObject("matches", matchService.getMatches());
        return result;
    }
	
	@PostMapping(value = "/matchesList")
    public ModelAndView listingMatch(@Valid Match match, BindingResult br, @AuthenticationPrincipal Authentication user) {
        ModelAndView result;
        if(br.hasErrors()) {
            result = new ModelAndView(LIST_MATCHES, br.getModel());
            result.addObject("match", match);
        } else {
            String playerName = user.getName();
            Jugador player = playerService.findJugadorByUserName(playerName);
            match.setJugador2(player);
            this.matchService.saveMatch(match);
            result = new ModelAndView(WAIT_MATCH_VIEW, br.getModel());
            result.addObject("match", match);
            result.addObject("player", player);
        }
        return result;
    }
	
	
//Usar estas dos funciones cuando creeis el crear partida bien
	@GetMapping(value = "/createMatch")
	public ModelAndView createNewMatch(@AuthenticationPrincipal Authentication user) {
		ModelAndView result = new ModelAndView(CREATE_MATCH_VIEW);
		String playerName = user.getName();
		System.out.println(playerName + "pepepe");
		System.out.println("pepe");
		Jugador player = playerService.findJugadorByUserName(playerName);
		Match match = new Match(false, player); //Hay que poner el jugador aqui!!! (creo, no entiendo los constructores en spring)
		result.addObject("match", match);
		result.addObject("player", player);
		result.addObject("players", playerService.findAllJugadores());
		return result;
	}
	@PostMapping(value = "/createMatch")
	public ModelAndView createMatch(@Valid Match match, BindingResult br, @AuthenticationPrincipal Authentication user) {
		ModelAndView result;
		if(br.hasErrors()) {
			result = new ModelAndView(CREATE_MATCH_VIEW, br.getModel());
			result.addObject("match", match);
		} else {
		    System.out.println("pepe1");
		    String playerName = user.getName();
	        Jugador player = playerService.findJugadorByUserName(playerName);
	        Match match2 = new Match(false, player);//Hay que poner el jugador aqui!!! (creo, no entiendo los constructores en spring)
	        System.out.println("pepe2");
	        Jugador jugador2 = playerService.findJugadorById(1);
	        match2.setJugador2(jugador2);
	        System.out.println(playerName);
		    this.matchService.saveMatch(match2);
			result = new ModelAndView(WAIT_MATCH_VIEW, br.getModel());
			result.addObject("match", match2);
			result.addObject("player", player);
		}
		return result;
	}

	
	@GetMapping(value = "/{idMatch}/currentMatch")
	public ModelAndView showCurrentMatch(@PathVariable int idMatch) {
		ModelAndView result = new ModelAndView(CURRENT_MATCH_VIEW);
		Match match = matchService.getMatchById(idMatch);
				
		if(match.esFaseBinaria()) {
			System.out.println("FASE BINARIA");
			binaryPhase(match);
		} else if(match.esFaseContaminacion()) {
			System.out.println("FASE CONTAMINACION");
			pollutionPhase(match);
		} else if(match.esFin()){
			System.out.println("FIN");
			result = new ModelAndView(COMPLETED_MATCH_VIEW);
		}
		
		result.addObject("match", match);
		matchService.saveMatch(match);
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
			String validacion = match.validateMove();
			result.addObject("error", validacion);

			if(validacion.length()==0) {
				// Realizar movimiento
				if(match.turnoPrimerJugador()) {
					movingBacteria(0, auxMatch, match);
				} else {
					movingBacteria(1, auxMatch, match);
				}
				//Pasa turno
				match.nextTurn();
				matchService.saveMatch(match);
			} else {
				System.out.println("Validación falló");
			}
		} else {
			System.out.println("NO ES PROPAGACIÓN PERO HUBO POST");
		}
		result.addObject("match", match);
		return result;
	}
	
	// TODO: por algún motivo, falla cuando se añade una sarcina y se mueven bacterias a otro disco en el mismo momento.
	private void movingBacteria(Integer playerId, Match auxMatch, Match match) {
		Integer initialDiskId = auxMatch.getDeDisco()[0]; // disco origen
		List<List<Integer>> targetDisksAndNumberOfBacteria = auxMatch.getTargetDiskAndNumberOfBacteria();
		List<Integer> targetDisks = targetDisksAndNumberOfBacteria.get(0); // discos destino
		List<Integer> numberOfBacteria = targetDisksAndNumberOfBacteria.get(1); // número de bacterias desplazadas a cada disco destino
		
		for(int i = 0; i < targetDisks.size(); i++) {
			match.movingBacteria(playerId, initialDiskId, targetDisks.get(i), numberOfBacteria.get(i));
		}
	}
	
	private void binaryPhase(Match match) {
		// TODO
		match.nextTurn();
	}
	
	private void pollutionPhase(Match match) {
		// TODO
		match.nextTurn();
	}

	@GetMapping(value = "/{idMatch}/completedMatch")
	public ModelAndView completedMatch(@PathVariable int idMatch) {
		ModelAndView result = new ModelAndView(COMPLETED_MATCH_VIEW); // aún no está hecha la vista
		result.addObject("match", matchService.getMatchById(idMatch));
		return result;
	}
	
	@GetMapping("/{idMatch}/statistics")
	public ModelAndView matchStatistics(@PathVariable int idMatch) {
		ModelAndView result = new ModelAndView(MATCH_STATISTICS_VIEW); 
		result.addObject("match", matchService.getMatchById(idMatch));
		return result;
	}
	

}