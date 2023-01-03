 package org.springframework.samples.petclinic.partida;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.invitacion.InvitationService;
import org.springframework.samples.petclinic.invitacion.resultadoInvitacion;
import org.springframework.samples.petclinic.invitacion.tipoInvitacion;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
	private static final String ABANDONED = "/matches/abandoned";


	private MatchService matchService;
	private PlayerService playerService;
	private InvitationService invitacionService;
	
	@Autowired
	public MatchController(MatchService matchService, PlayerService playerService,InvitationService invitacionService) {
		this.matchService = matchService;
		this.playerService = playerService;
		this.invitacionService=invitacionService;
	}
	
	
	
	@GetMapping(value = "/matchesList")
    public ModelAndView listingMatch(@AuthenticationPrincipal Authentication user) {
		Jugador actualUser=playerService.findPlayerByUsername(user.getName());
        ModelAndView result = new ModelAndView(LIST_MATCHES);   
        result.addObject("match_list", matchService.getMatchesWithoutPlayer2());
        List<Match> matches=new ArrayList<Match>();
        for(Match m:matchService.getMatches()) {
        	if(m.getJugador2()!=null && m.getAbandonada()==false && m.getFinPartida()==null && 
        			m.getEspectadores().size()<4 && m.getGanadorPartida()==GameWinner.UNDEFINED && 
        			m.getJugador1()!=actualUser && m.getJugador2()!=actualUser) {
        		matches.add(m);
        	}
        }
        result.addObject("matches", matches);
        result.addObject("loggedUser", actualUser);
        return result;
    }
	
	@GetMapping(value = "/createMatch")
	public ModelAndView createNewMatch(@AuthenticationPrincipal Authentication user) {
		ModelAndView result = new ModelAndView(CREATE_MATCH_VIEW);
		Jugador jugadorActual=playerService.findPlayerByUsername(user.getName());
		List<Jugador> listaAmigos=new ArrayList<>(jugadorActual.playerFriends());
		List<Jugador> listaAmigosInvitados=new ArrayList<>();
		listaAmigosInvitados.addAll(jugadorActual.getAmigosInvitados());
		result.addObject("actualPlayer", playerService.findPlayerByUsername(user.getName()));
		result.addObject("amigosInvitados",listaAmigosInvitados);
		result.addObject("players",listaAmigos);
		return result;
	}
	@PostMapping(value = "/createMatch")
	public ModelAndView createMatch(@RequestParam String nombre,@RequestParam Boolean tipoPartida, @AuthenticationPrincipal Authentication user) {
	        ModelAndView result =  new ModelAndView();
	        Jugador player = playerService.findPlayerByUsername(user.getName());
	        if(matchService.canIplay(player)) {
	        Match match = new Match(false, player);
	        match.setName(nombre);
	        match.setEsPrivada(tipoPartida);
	        match.getDisco(2).annadirBacterias(0, 1);
	        match.getDisco(4).annadirBacterias(1, 1);
	        match.setJugador1(player);
		    this.matchService.saveMatch(match);
		    int id = match.getId();
		    String matchId=String.valueOf(id);

		    for(Integer h=0;h<player.getAmigosInvitados().size();h++) {
		    	Jugador j=player.getAmigosInvitados().get(h);
		    	Invitacion i=new Invitacion();
		    	i.setFechaHora(LocalDate.now());
		    	i.setJugador(j);
		    	i.setMatch(match);
		    	i.setResultado(resultadoInvitacion.SIN_RESPONDER);
		    	if(player.getTipoDeInvitacionPartidaEnviada().get(h).equals("jugador")) {
		    		i.setTipo(tipoInvitacion.JUGADOR);
		    	}
		    	else {
		    		i.setTipo(tipoInvitacion.ESPECTADOR);
		    	}
		    	invitacionService.save(i);
		    }
		    List<Jugador>copia=player.getAmigosInvitados();
		    copia.clear();
		    player.setAmigosInvitados(copia);
		    playerService.saveJugador(player);
		    
		    result = new ModelAndView("redirect:/matches/"+matchId+"/waitForMatch");
		    }else {
		        result = new ModelAndView("/matches/exception");
		        result.addObject("jugador", player);
		    }
		    return result;
	}
	
	@GetMapping(value ="/{idMatch}/waitForMatch")
	public ModelAndView showWait(@PathVariable("idMatch") int matchId, @AuthenticationPrincipal Authentication user, HttpServletResponse response) {
	    if(matchService.getMatchById(matchId).getJugador2()!=null && matchService.getMatchById(matchId).getJugador1()!=playerService.findPlayerByUsername(user.getName())) {
	    	Jugador actualUser=playerService.findPlayerByUsername(user.getName());
	    	ModelAndView result = new ModelAndView("/matches/matchesList");
	    	result.addObject("partidaLlena",true);
	    	result.addObject("match_list", matchService.getMatchesWithoutPlayer2());
	    	List<Match> matches=new ArrayList<Match>();
	        for(Match m:matchService.getMatches()) {
	        	if(m.getJugador2()!=null && m.getAbandonada()==false && m.getFinPartida()==null && 
	        			m.getEspectadores().size()<4 && m.getGanadorPartida()==GameWinner.UNDEFINED && 
	        			m.getJugador1()!=actualUser && m.getJugador2()!=actualUser) {
	        		matches.add(m);
	        	}
	        }
	        result.addObject("matches", matches);
	    	return result;
	    }
		response.addHeader("Refresh", "1.85");
	    ModelAndView resul = new ModelAndView(WAIT_MATCH_VIEW);
	    Match match = matchService.getMatchById(matchId);
	    String id = String.valueOf(matchId);
	    String playerName = user.getName();
        Jugador player = playerService.findPlayerByUsername(playerName);
	    resul.addObject("match", match);
	    if(match.getJugador1()==player) {
	    resul.addObject("EresJugador1", true);
	    }
	    if(match.getJugador2()!=null) {
	    	match.setInicioPartida(LocalDateTime.now());
	    	matchService.saveMatch(match);
	        resul = new ModelAndView("redirect:/matches/"+id+"/currentMatch");
	    }
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
	public ModelAndView showCurrentMatch(@PathVariable int idMatch, @AuthenticationPrincipal Authentication user, HttpServletResponse response) {
		ModelAndView result;
		Match match = matchService.getMatchById(idMatch);
		Jugador player1 = match.getJugador1();
		Jugador player2 = match.getJugador2();
		if(match.getGanadorPartida() == GameWinner.UNDEFINED) {
			result = new ModelAndView(CURRENT_MATCH_VIEW);
			if(match.esFaseBinaria()) {
				binaryPhase(match, player1, player2);
			} else if(match.esFaseContaminacion()) {
				pollutionPhase(match, player1, player2);
			}
		} else {
			result = new ModelAndView(MATCH_STATISTICS_VIEW);
			if(match.getFinPartida()==null) {
				finishMatch(match);
			}
		}
		refresh(user, match, match.itIsPropagationPhase(), response);
        addDataToTheView(user, result, match);
		matchService.saveMatch(match);
		playerService.saveJugador(player1);
		playerService.saveJugador(player2);
		return result;
	}

	private void finishMatch(Match match) {
		match.setFinPartida(LocalDateTime.now());
		match.getJugador1().setNumeroDeContaminacion(0);
		match.getJugador1().setBacterias(20);
		match.getJugador1().setSarcinas(4);
		match.getJugador2().setNumeroDeContaminacion(0);
		match.getJugador2().setBacterias(20);
		match.getJugador2().setSarcinas(4);
	}
	
	/**
	 * Dependiendo de diferentes factores, refresca o no la pantalla.
	 * @param user
	 * @param match
	 * @param itIsPropagationPhase
	 * @param response
	 */
	public void refresh(Authentication user, Match match, Boolean itIsPropagationPhase, HttpServletResponse response) {
		Integer idLoggedPlayer = playerService.findPlayerByUsername(user.getName()).getId();
        Integer idCurrentPlayer = match.turnoPrimerJugador() ? match.getJugador1().getId() : match.getJugador2().getId();
		if (itIsPropagationPhase) {
			if(idLoggedPlayer != idCurrentPlayer) {
	        	response.addHeader("Refresh", "5");
	        }
		} else {
			response.addHeader("Refresh", "1");
		}
	}
	

	@RequestMapping("/{idMatch}/currentMatch")
	public ModelAndView nextPhase(@PathVariable int idMatch, Match auxMatch, @AuthenticationPrincipal Authentication user, HttpServletResponse response) {
		ModelAndView result = new ModelAndView(CURRENT_MATCH_VIEW);
		Match match = matchService.getMatchById(idMatch);
		Jugador player1 = match.getJugador1();
		Jugador player2 = match.getJugador2();
		
		if(match.esPropagacion()) {
			match.copyTransientData(auxMatch);
			//Si es "" es correcto. Si tiene un mensaje es un msg de error
			String validacion = match.validateMove();
			result.addObject("error", validacion);

			if(validacion.length()==0) {
				if(match.turnoPrimerJugador()) { // Realizar movimiento
					movingBacteria(0, player1, auxMatch, match);
					playerService.saveJugador(player1);
				} else {
					movingBacteria(1, player2, auxMatch, match);
					playerService.saveJugador(player2);
				}
				match.nextTurn();
				matchService.saveMatch(match);
			}
		} else if(match.esFin()) {
			match.determineWinner();
			result = new ModelAndView(MATCH_STATISTICS_VIEW);
			finishMatch(match);
		} else {
			match.nextTurn();
			matchService.saveMatch(match);
		}
		refresh(user, match, match.itIsPropagationPhase(), response);
		addDataToTheView(user, result, match);
		
		return result;
	}

	private void addDataToTheView(Authentication user, ModelAndView result, Match match) {
		Integer idLoggedPlayer = playerService.findPlayerByUsername(user.getName()).getId();
        Integer idCurrentPlayer = match.turnoPrimerJugador() ? match.getJugador1().getId() : match.getJugador2().getId();
        result.addObject("idLoggedPlayer", idLoggedPlayer);
		result.addObject("idCurrentPlayer", idCurrentPlayer);
		result.addObject("isYourTurn", idLoggedPlayer == idCurrentPlayer);
		result.addObject("match", match);
	}
	
	private void movingBacteria(Integer idPlayerMatch, Jugador player, Match auxMatch, Match match) {
		Integer initialDiskId = auxMatch.getDeDisco()[0]; // disco origen
		List<List<Integer>> targetDisksAndNumberOfBacteria = auxMatch.getTargetDiskAndNumberOfBacteria();
		List<Integer> targetDisks = targetDisksAndNumberOfBacteria.get(0); // discos destino
		List<Integer> numberOfBacteria = targetDisksAndNumberOfBacteria.get(1); // número de bacterias desplazadas a cada disco destino
		
		for(int i = 0; i < targetDisks.size(); i++) {
			match.movingBacteria(idPlayerMatch, player, initialDiskId, targetDisks.get(i), numberOfBacteria.get(i));
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
	
	@RequestMapping("/{idMatch}/abandonedMatch")
	public RedirectView abandonedMatch(@PathVariable int idMatch, Authentication user) {
		RedirectView result = new RedirectView("/matches/"+idMatch+"/currentMatch");
		Match match = matchService.getMatchById(idMatch);
		Jugador loggedPlayer = playerService.findPlayerByUsername(user.getName());
		match.setGanadorPartida(loggedPlayer == match.getJugador1() ? GameWinner.SECOND_PLAYER : GameWinner.FIRST_PLAYER);
		match.setAbandonada(true);
		matchService.saveMatch(match);
		return result;
	}
	
	@RequestMapping("/{idMatch}/abandonedWaitMatch")
	public RedirectView abandonedWaitMatch(@PathVariable int idMatch, Authentication user) {
		RedirectView result = new RedirectView("/matches/createMatch");
		Match match = matchService.getMatchById(idMatch);
		matchService.deleteMatch(match);
		return result;
	}
	
	@GetMapping("/{idMatch}/abandoned")
	public ModelAndView abandonedMatchView(@PathVariable int idMatch) {
	    ModelAndView result = new ModelAndView(ABANDONED);
        Match match = matchService.getMatchById(idMatch);
        Jugador winner = new Jugador();
        if(match.getGanadorPartida()==GameWinner.SECOND_PLAYER){
            winner = match.getJugador2();
        }else if(match.getGanadorPartida()==GameWinner.FIRST_PLAYER) {
            winner = match.getJugador1();
        }
        result.addObject("winner", winner);
	    result.addObject("match", match);
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
	
	
	@GetMapping(value = "/{idMatch}/currentMatchSpectated")
	public ModelAndView showCurrentMatchSpectated(@PathVariable int idMatch, @AuthenticationPrincipal Authentication user,HttpServletResponse response) {
		ModelAndView result;
		Jugador actualUser=playerService.findPlayerByUsername(user.getName());
		Match match = matchService.getMatchById(idMatch);
		if(match.getEspectadores().size()<4) {
			if(match.getGanadorPartida() == GameWinner.UNDEFINED) {
				result = new ModelAndView(CURRENT_MATCH_VIEW);
			} else {
				result = new ModelAndView(MATCH_STATISTICS_VIEW);
			}
			refresh(user, match, match.itIsPropagationPhase(), response);
	        addDataToTheView(user, result, match);
	        Set<Jugador> espectadores=match.getEspectadores();
	        espectadores.add(playerService.findPlayerByUsername(user.getName()));
	        match.setEspectadores(espectadores);
	        matchService.saveMatch(match);
		}
		else {
			result=new ModelAndView("/matches/matchesList");
			result.addObject("match_list", matchService.getMatchesWithoutPlayer2());
	        List<Match> matches=new ArrayList<Match>();
	        for(Match m:matchService.getMatches()) {
	        	if(m.getJugador2()!=null && m.getAbandonada()==false && m.getFinPartida()==null && 
	        			m.getEspectadores().size()<4 && m.getGanadorPartida()==GameWinner.UNDEFINED && 
	        			m.getJugador1()!=actualUser && m.getJugador2()!=actualUser) {
	        		matches.add(m);
	        	}
	        }
	        result.addObject("matches", matches);
	        result.addObject("noCabenMasEspectadores",true);
		}
		return result;
	}
	
	@GetMapping("/{matchId}/abandonedMatchSpectated")
	public ModelAndView abandonedMatchSpectated(@PathVariable int matchId,@AuthenticationPrincipal Authentication user) {
	    ModelAndView result = new ModelAndView("redirect:/matches/matchesList");
        Match match = matchService.getMatchById(matchId);
        Set<Jugador> espectadores=match.getEspectadores();
        espectadores.remove(playerService.findPlayerByUsername(user.getName()));
        match.setEspectadores(espectadores);
        matchService.saveMatch(match);
	    return result;
	}
	
	@GetMapping("/cancelarCreacionPartida")
	public ModelAndView cancelarPartida(@AuthenticationPrincipal Authentication user) {
	    ModelAndView result = new ModelAndView("redirect:/");
	    Jugador actualPlayer=playerService.findPlayerByUsername(user.getName());
	    List<Jugador>copia=actualPlayer.getAmigosInvitados();
	    copia.clear();
	    actualPlayer.setAmigosInvitados(copia);
	    List<String>copia2=actualPlayer.getTipoDeInvitacionPartidaEnviada();
	    copia2.clear();
	    actualPlayer.setTipoDeInvitacionPartidaEnviada(copia2);
	    playerService.saveJugador(actualPlayer);
	    return result;
	}
	

}