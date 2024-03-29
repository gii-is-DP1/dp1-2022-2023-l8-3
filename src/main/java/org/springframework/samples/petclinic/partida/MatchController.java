 package org.springframework.samples.petclinic.partida;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.dto.ManualMatchMapper;
import org.springframework.samples.petclinic.dto.MatchDTO;
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
	
	private static final String INVITATION_TYPE_AS_PLAYER = "jugador";
	private static final int MINUTES_OF_AN_HOUR = 60;
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
	        if(Boolean.TRUE.equals(matchService.canIplay(player)) && Boolean.TRUE.equals(matchService.imPlaying(player))) {
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
		    	if(player.getTipoDeInvitacionPartidaEnviada().get(h).equals(INVITATION_TYPE_AS_PLAYER)) {
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
		        if(Boolean.FALSE.equals(matchService.canIplay(player))) {
		            result.addObject("mensaje", "hoy ya has jugado demasiado descansa la vista, sal a la calle un rato y disfruta de la naturaleza.");
		        }else if(Boolean.FALSE.equals(matchService.imPlaying(player))){
		            result.addObject("mensaje", "Ya estas jugando a otra partida en estos momentos. Termínala para poder comenzar otra.");
		        }
		        result.addObject(INVITATION_TYPE_AS_PLAYER, player);
		    }
		    return result;
	}
	
	@GetMapping(value ="/{idMatch}/waitForMatch")
	public ModelAndView showWait(@PathVariable("idMatch") int matchId, @AuthenticationPrincipal Authentication user, HttpServletResponse response) {
		if(matchService.getMatchById(matchId).getJugador2()!=null && matchService.getMatchById(matchId).getJugador1()!=playerService.findPlayerByUsername(user.getName())) {
	    	Jugador actualUser=playerService.findPlayerByUsername(user.getName());
	    	ModelAndView result = new ModelAndView(LIST_MATCHES);
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
	public ModelAndView post(@PathVariable("idMatch") Integer matchId, @AuthenticationPrincipal Authentication user) {
        ModelAndView result =  new ModelAndView();
        Jugador player = playerService.findPlayerByUsername(user.getName());
        if(Boolean.TRUE.equals(matchService.canIplay(player)) && Boolean.TRUE.equals(matchService.imPlaying(player))) {
	        Match match = matchService.getMatchById(matchId);
	        match.setJugador2(player);
	        this.matchService.saveMatch(match);
	        String id = String.valueOf(matchId);
	        result =new ModelAndView("redirect:/matches/"+id+"/currentMatch");
        }else {
            result = new ModelAndView("/matches/exception");
            if(Boolean.FALSE.equals(matchService.canIplay(player))) {
                result.addObject("mensaje", "hoy ya has jugado demasiado descansa la vista, sal a la calle un rato y disfruta de la naturaleza.");
            }else if(Boolean.FALSE.equals(matchService.imPlaying(player))){
                result.addObject("mensaje", "Ya estas jugando a otra partida en estos momentos. Termínala para poder comenzar otra.");
            }
            result.addObject(INVITATION_TYPE_AS_PLAYER, player);
        }
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
			if(Boolean.TRUE.equals(match.esFaseBinaria())) {
				binaryPhase(match, player1, player2);
			} else if(Boolean.TRUE.equals(match.esFaseContaminacion())) {
				pollutionPhase(match, player1, player2);
			} else if(Boolean.TRUE.equals(match.esFin())) {
				match.determineWinner();
				result = new ModelAndView(MATCH_STATISTICS_VIEW);
				finishMatch(match);
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
		match.getDisco(2).setNumMov1(match.getDisco(2).getNumMov1()-1);
		match.getDisco(4).setNumMov2(match.getDisco(4).getNumMov2()-1);
	}
	
	/**
	 * En las fases de fisión binaria y contaminación, la pantalla se recarga cada 3 segundos
	 * En la fase de propagación, para el jugador al que le toca, la pantalla se recarga cada 6 segundos
	 * Para el jugador al que no le toca, se recarga cada 3 segundos
	 * @param user
	 * @param match
	 * @param itIsPropagationPhase
	 * @param response
	 */
	public void refresh(Authentication user, Match match, Boolean itIsPropagationPhase, HttpServletResponse response) {
		Integer idLoggedPlayer = playerService.findPlayerByUsername(user.getName()).getId();
        Integer idCurrentPlayer = Boolean.TRUE.equals(match.turnoPrimerJugador()) ? match.getJugador1().getId() : match.getJugador2().getId();

        if (Boolean.TRUE.equals(itIsPropagationPhase)) {
			if(!idLoggedPlayer.equals(idCurrentPlayer)) {
	        	response.addHeader("Refresh", "3");
	        } else {
	        	response.addHeader("Refresh", "6");
	        }
		} else {
			response.addHeader("Refresh", "3");
		}
	}
	

	@PostMapping("/{idMatch}/currentMatch")
	public ModelAndView nextPhase(@PathVariable int idMatch, MatchDTO matchDTO, @AuthenticationPrincipal Authentication user, HttpServletResponse response) {
		ModelAndView result = new ModelAndView(CURRENT_MATCH_VIEW);
		Match match = matchService.getMatchById(idMatch);
		Jugador player1 = match.getJugador1();
		Jugador player2 = match.getJugador2();

		if(Boolean.TRUE.equals(match.esPropagacion())) {
			ManualMatchMapper m = new ManualMatchMapper();
			Match auxMatch = m.convertMatchDTOToEntity(matchDTO);
			match.copyTransientData(auxMatch);
			//Si es "" es correcto. Si tiene un mensaje es un msg de error
			String validacion = match.validateMove();
			result.addObject("error", validacion);

			if(validacion.length()==0) {
				if(Boolean.TRUE.equals(match.turnoPrimerJugador())) { // Realizar movimiento
					movingBacteria(0, player1, auxMatch, match);
				} else {
					movingBacteria(1, player2, auxMatch, match);
				}
				match.nextTurn();
				matchService.saveMatch(match);
			}
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
        Integer idCurrentPlayer = Boolean.TRUE.equals(match.turnoPrimerJugador()) ? match.getJugador1().getId() : match.getJugador2().getId();
        result.addObject("idLoggedPlayer", idLoggedPlayer);
		result.addObject("idCurrentPlayer", idCurrentPlayer);
		result.addObject("isYourTurn", idLoggedPlayer.equals(idCurrentPlayer));
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
	
	@GetMapping("/{idMatch}/abandonedMatch")
	public RedirectView abandonedMatch(@PathVariable int idMatch, Authentication user) {
		RedirectView result = new RedirectView("/matches/"+idMatch+"/currentMatch");
		Match match = matchService.getMatchById(idMatch);
		Jugador loggedPlayer = playerService.findPlayerByUsername(user.getName());
		match.setGanadorPartida(loggedPlayer == match.getJugador1() ? GameWinner.SECOND_PLAYER : GameWinner.FIRST_PLAYER);
		match.setAbandonada(true);
		matchService.saveMatch(match);
		return result;
	}
	
	@GetMapping("/{idMatch}/abandonedWaitMatch")
	public RedirectView abandonedWaitMatch(@PathVariable int idMatch, Authentication user) {
		RedirectView result = new RedirectView(CREATE_MATCH_VIEW);
		Match match = matchService.getMatchById(idMatch);
		for(Invitacion i:match.getInvitaciones()) {
			invitacionService.delete(i);
		}
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
	
	@GetMapping(value = "/InProgress/{page}")
	public String showMatchesInProgress(Map<String, Object> model,Map<String, Object> model2,
			@PathVariable("page") int page) {
		if(page<1) 
			return "redirect:/matches/InProgress/1";
		
		Pageable pageable = PageRequest.of(page-1, 10);
		Page<Match> results = this.matchService.getMatchesByGameWinnerPageable(GameWinner.UNDEFINED, pageable);

		Integer numberOfPages = results.getTotalPages();
		Integer thisPage = page;

		if(thisPage > numberOfPages && numberOfPages != 0) 
			return "redirect:/matches/InProgress/"+numberOfPages;

		model.put("numberOfPages", numberOfPages);
		model.put("selections", results.getContent());
		model.put("thisPage", thisPage);
		
		Boolean b=results.isEmpty();
		
		model2.put("sinPartidas", b);
		return "matches/listMatchesInProgress";
	}
	
	@GetMapping(value = "/Finished/{page}")
	public String showMatchesFinished(Map<String, Object> model,Map<String, Object> model2,Map<String, Object> model3,
			@PathVariable("page") int page) {
		if(page<1) 
			return "redirect:/matches/Finished/1";

		Pageable pageable = PageRequest.of(page-1, 10);
		Page<Match> results = this.matchService.getMatchesFinishedPageable(pageable);

		Integer numberOfPages = results.getTotalPages();
		Integer thisPage = page;
		
		if(thisPage > numberOfPages && numberOfPages != 0) 
			return "redirect:/matches/Finished/"+numberOfPages;
		
		model.put("numberOfPages", numberOfPages);
		model.put("selections", results.getContent());
		model.put("thisPage", thisPage);
		model.put("gamesPlayed", matchService.getPlayedMatches().size());
		model.put("totalPlayingGame", getTotalPlayingGame());
		model.put("durationOfTheLongestGame", getDurationOfTheLongestGame());

		Boolean b=results.isEmpty();

		model2.put("sinPartidas", b);
		model3.put("firstPlayer", GameWinner.FIRST_PLAYER);
		model3.put("secondPlayer", GameWinner.SECOND_PLAYER);
		return "matches/listMatchesFinished";
	}

	private String getTotalPlayingGame() {
		Long minutes = 0l;
		List<Match> matches = new ArrayList<Match>(matchService.getMatches());
		for (Match match : matches) {
			if(!match.getGanadorPartida().equals(GameWinner.UNDEFINED)) {
				minutes += match.durationInMinutes();
			}
		}
		Long hours = TimeUnit.MINUTES.toHours(minutes);
		return String.format("%2d horas y %1d minutos", hours, minutes-(hours*MINUTES_OF_AN_HOUR));
	}

	private Long getDurationOfTheLongestGame() {
		Long result = 0l;
		List<Match> matches = new ArrayList<Match>(matchService.getMatches());
		for (Match match : matches) {
			if(!match.getGanadorPartida().equals(GameWinner.UNDEFINED) && match.durationInMinutes() > result) {
				result = match.durationInMinutes();
			}
		}
		return result;
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
			result=new ModelAndView(LIST_MATCHES);
			result.addObject("match_list", matchService.getMatchesWithoutPlayer2());
	        List<Match> matches=new ArrayList<Match>();
	        for(Match m:matchService.getMatches()) {
	        	if(m.getJugador2()!=null && Boolean.FALSE.equals(m.getAbandonada()) && m.getFinPartida()==null && 
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