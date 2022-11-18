 package org.springframework.samples.petclinic.partida;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

        result.addObject("match_list", matchService.getMatchesWithoutPlayer2());
        result.addObject("matches", matchService.getMatches());
        return result;
    }
	
	
    
    
	
	
//Usar estas dos funciones cuando creeis el crear partida bien
	@GetMapping(value = "/createMatch")
	public ModelAndView createNewMatch() {
		ModelAndView result = new ModelAndView(CREATE_MATCH_VIEW);
		result.addObject("players", playerService.findAllJugadores());
		return result;
	}
	@PostMapping(value = "/createMatch")
	public RedirectView createMatch(@RequestParam String nombre,@RequestParam Boolean tipoPartida, @AuthenticationPrincipal Authentication user) {
		

		    String playerName = user.getName();
	        Jugador player = playerService.findPlayerByUsername(playerName);
	        Match match = new Match(false, player);
	        match.setName(nombre);
	        match.setEsPrivada(tipoPartida);
	        //Jugador jugador2 = playerService.findJugadorById(1);
	        match.setJugador1(player);
	        //match.setJugador2(jugador2);
		    this.matchService.saveMatch(match);
		    int id = match.getId();
		    String matchId=String.valueOf(id);
		    RedirectView result = new RedirectView("/matches/"+matchId+"/waitForMatch");
		    return result;
		    
		


	}
	@GetMapping(value ="/{idMatch}/waitForMatch")
	public ModelAndView showWait(@PathVariable int idMatch) {
	    ModelAndView resul = new ModelAndView(WAIT_MATCH_VIEW);
	    Match match = matchService.getMatchById(idMatch);
	    resul.addObject("match", match);
	    return resul;
	    
	}
	
	
	@PostMapping(value ="/{idMatch}/waitForMatch")
	public RedirectView post(@PathVariable("idMatch") Integer matchId, @AuthenticationPrincipal Authentication user) {
	    String playerName = user.getName();
        Jugador player = playerService.findPlayerByUsername(playerName);
        Match match = matchService.getMatchById(matchId);
        match.setJugador2(player);
        this.matchService.saveMatch(match);
        String id = String.valueOf(matchId);
        RedirectView result = new RedirectView("/matches/"+id+"/currentMatch");
        return result;
        
	}
	
	
	

	
	@GetMapping(value = "/{idMatch}/currentMatch")
	public ModelAndView showCurrentMatch(@PathVariable int idMatch) {
		ModelAndView result = new ModelAndView(CURRENT_MATCH_VIEW);
		Match match = matchService.getMatchById(idMatch);
		Jugador player1 = match.getJugador1();
		Jugador player2 = match.getJugador2();
				
		if(match.esFaseBinaria()) {
			System.out.println("FASE BINARIA");
			binaryPhase(match, player1, player2);
		} else if(match.esFaseContaminacion()) {
			System.out.println("FASE CONTAMINACION");
			pollutionPhase(match, player1, player2);
		} else if(match.esFin()){
			System.out.println("FIN");
			result = new ModelAndView(MATCH_STATISTICS_VIEW);
		}
		
		result.addObject("match", match);
		matchService.saveMatch(match);
		playerService.saveJugador(player1);
		playerService.saveJugador(player2);
		return result;
	}
	

	@RequestMapping("/{idMatch}/currentMatch")
	public ModelAndView nextPhase(@PathVariable int idMatch, Match auxMatch) {
		ModelAndView result = new ModelAndView(CURRENT_MATCH_VIEW);
		Match match = matchService.getMatchById(idMatch);
		Jugador player1 = match.getJugador1();
		Jugador player2 = match.getJugador2();
		
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
					movingBacteria(player1, auxMatch, match);
					playerService.saveJugador(player1);
				} else {
					movingBacteria(player2, auxMatch, match);
					playerService.saveJugador(player2);
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
	
	private void movingBacteria(Jugador player, Match auxMatch, Match match) {
		Integer initialDiskId = auxMatch.getDeDisco()[0]; // disco origen
		List<List<Integer>> targetDisksAndNumberOfBacteria = auxMatch.getTargetDiskAndNumberOfBacteria();
		List<Integer> targetDisks = targetDisksAndNumberOfBacteria.get(0); // discos destino
		List<Integer> numberOfBacteria = targetDisksAndNumberOfBacteria.get(1); // número de bacterias desplazadas a cada disco destino
		
		for(int i = 0; i < targetDisks.size(); i++) {
			match.movingBacteria(player, initialDiskId, targetDisks.get(i), numberOfBacteria.get(i));
		}
	}
	
	private void binaryPhase(Match match, Jugador player1, Jugador player2) {
		match.binaryPhase(player1, player2);
		match.nextTurn();
	}
	
	private void pollutionPhase(Match match, Jugador player1, Jugador player2) {
		match.pollutionPhase(player1, player2);
		match.nextTurn();
	}
	
	@RequestMapping("/{idMatch}/completedMatch")
	public RedirectView completedMatch(@PathVariable int idMatch) {
		RedirectView result = new RedirectView();
		Match match = matchService.getMatchById(idMatch);
		match.setFinPartida(LocalDateTime.now());
		matchService.saveMatch(match);
		result.setUrl("/matches/{idMatch}/statistics");
		return result;
	}
	
	@GetMapping("/{idMatch}/statistics")
	public ModelAndView matchStatistics(@PathVariable int idMatch) {
		ModelAndView result = new ModelAndView(MATCH_STATISTICS_VIEW); 
		result.addObject("match", matchService.getMatchById(idMatch));
		return result;
	}
	
	@GetMapping(value = "/InProgress")
	public String showMatchesInProgress(Map<String, Object> model,Map<String, Object> model2) {
		List<Match> results = this.matchService.getMatchesByGameWinner(GameWinner.UNDEFINED);
		Boolean b=false;
		model.put("selections", results);
		if(results.isEmpty()) {
			b=true;
		}
		model2.put("sinPartidas", b);
		return "matches/listMatchesInProgress";
	}
	
	@GetMapping(value = "/Finished")
	public String showMatchesFinished(Map<String, Object> model,Map<String, Object> model2,Map<String, Object> model3) {
		List<Match> results = this.matchService.getMatchesByGameWinner(GameWinner.FIRST_PLAYER);
		results.addAll(this.matchService.getMatchesByGameWinner(GameWinner.SECOND_PLAYER));
		Boolean b=false;
		model.put("selections", results);
		if(results.isEmpty()) {
			b=true;
		}
		model2.put("sinPartidas", b);
		model3.put("firstPlayer", GameWinner.FIRST_PLAYER);
		return "matches/listMatchesFinished";
	}
	

}