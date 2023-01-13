package org.springframework.samples.petclinic.jugador;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.user.Authorities;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.samples.petclinic.web.MyErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.samples.petclinic.dto.JugadorDTO;
import org.springframework.samples.petclinic.dto.ManualJugadorMapper;
import org.springframework.samples.petclinic.partida.GameWinner;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PlayerController {

	private static final String CREATE_OR_UPDATE_PLAYER_VIEW = "jugadores/createOrUpdateJugadorFormAdmin";
	private static final int RESULTS_LIMIT = 20;
	private static final int FRIEND_LIMIT = 150;
	private PlayerService playerService;
	private UserService userService;
	private MatchService matchService;
	private FriendRequestService friendRequestService;
	private ManualJugadorMapper m;

	@Autowired
	public PlayerController(PlayerService playerService, UserService userService, MatchService matchService,
			FriendRequestService friendRequestService) {
		this.playerService = playerService;
		this.userService = userService;
		this.matchService = matchService;
		this.friendRequestService = friendRequestService;
		m = new ManualJugadorMapper();
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	// --------------------------------------------------------------------------------------------------------------------------------------------- //

	@GetMapping(value = "/jugadores/list/{page}")
	public String showAllPlayers(Map<String, Object> model,@PathVariable("page") int page) {
		
		if(page<1) 
			return "redirect:/jugadores/list/1";
		
		Pageable pageable = PageRequest.of(page-1, 2);
		Page<Jugador> results = this.playerService.findAllJugadoresPageable(pageable);

		Integer numberOfPages = results.getTotalPages();
		Integer thisPage = page;

		if(thisPage > numberOfPages && numberOfPages != 0) 
			return "redirect:/jugadores/list/"+numberOfPages;
		
		model.put("numberOfPages", numberOfPages);
		model.put("thisPage", thisPage);
		
		model.put("selections", results.getContent());

		if (results.isEmpty()) {
			return "redirect:/jugadores/new";
		}
		return "jugadores/listJugador";
	}

	@GetMapping(value = "/perfil")
	public String showPerfil() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUser(auth.getName()).get();
		Integer id = playerService.findPlayerByUsername(user.getUsername()).getId();
		return "redirect:/jugadores/" + id;
	}

	@GetMapping(value = "/jugadores/{jugadorId}")
	public ModelAndView showPlayer(@PathVariable("jugadorId") int id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUser(auth.getName()).get();
		ModelAndView mav = new ModelAndView();
		for (Authorities authority : user.getAuthorities()) {
			if (authority.getAuthority().equals("admin")
					|| playerService.findPlayerByUsername(user.getUsername()).getId() == id) {
				mav = new ModelAndView("jugadores/showJugador");
				mav.addObject(this.playerService.findJugadorById(id));
			}

		}
		return mav;
	}

	@GetMapping(value = "/jugadores/{jugadorId}/delete")
	public String deletePlayer(@PathVariable("jugadorId") int id) throws Exception {
		try {
			playerService.deletePlayer(id);
			log.info("Player deleted succesfully");
		} catch (Exception e) {
			log.warn("Not existing player");
			throw new Exception("Player Delete Error");
		}
		return "redirect:/jugadores/list/1";
	}

	@GetMapping(value = "/jugadores/new")
	public String initCreationForm(Map<String, Object> model, Map<String, Object> model2) {
		model.put("jugador", new Jugador());
		Collection<Jugador> c = this.playerService.findAllJugadores();
		if (c.isEmpty()) {
			model2.put("sinJugadores", true);
		}
		return CREATE_OR_UPDATE_PLAYER_VIEW;
	}

	@PostMapping(value = "/jugadores/new")
	public ModelAndView processCreationForm(@Valid JugadorDTO jugadorDTO, BindingResult br, Map<String, Object> model) {
		Boolean correctPassword = false;
		ModelAndView resul;
		Jugador jugador = m.convertJugadorDTOToEntity(jugadorDTO);
		
		if (Boolean.TRUE.equals(br.hasErrors())) {
			log.error("Input error");
			resul = new ModelAndView(CREATE_OR_UPDATE_PLAYER_VIEW);
			model.put("jugador", jugador);
			resul.addObject("message", MyErrorController.getErrorMessage(br));
		} else {
			List<Jugador> lista = playerService.findAllJugadores();
			
			if(Boolean.TRUE.equals(PlayerValidation.isRegisteredEmail(jugador, model, lista)) || Boolean.FALSE.equals(PlayerValidation.isValidEmail(model, jugador)) 
					|| Boolean.FALSE.equals(PlayerValidation.isCorrectPassword(jugador, model, correctPassword)) || Boolean.TRUE.equals(PlayerValidation.firstNameOrLastNameAreEmpty(jugador, model))
					|| Boolean.TRUE.equals(PlayerValidation.usernameRegistered(jugador, model, lista))) {
				resul = new ModelAndView(CREATE_OR_UPDATE_PLAYER_VIEW);
				model.put("jugador", jugador);
			} else {
				jugador.setEstadoOnline(false);
				this.playerService.saveJugador(jugador);
				resul = new ModelAndView("redirect:/jugadores/" + jugador.getId());
			}
		}
		return resul;
	}

	@GetMapping(value = "/jugadores/{jugadorId}/edit")
	public ModelAndView initUpdateJugadorForm(@PathVariable("jugadorId") int jugadorId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUser(auth.getName()).get();
		ModelAndView mav = new ModelAndView();
		for (Authorities authority : user.getAuthorities()) {
			if (authority.getAuthority().equals("admin")) {
				mav = new ModelAndView(CREATE_OR_UPDATE_PLAYER_VIEW);
				mav.addObject(this.playerService.findJugadorById(jugadorId));
			}else if(playerService.findPlayerByUsername(auth.getName()).getId().equals(jugadorId)){
			    mav = new ModelAndView("jugadores/createOrUpdateJugadorForm");
                mav.addObject(this.playerService.findJugadorById(jugadorId));
			}
		}
		return mav;
	}

	@PostMapping(value = "/jugadores/{jugadorId}/edit")
	public ModelAndView processUpdateJugadorForm(@Valid JugadorDTO jugadorDto, BindingResult br,
			@PathVariable("jugadorId") int jugadorId, Map<String, Object> model) {
		Boolean correctPassword = false;
		ModelAndView resul;
		Jugador jugador = m.convertJugadorDTOToEntity(jugadorDto);
		Jugador jugadorActual= playerService.findJugadorById(jugadorId);
		if (Boolean.TRUE.equals(br.hasErrors())) {
			log.error("Input error");
			resul = new ModelAndView(CREATE_OR_UPDATE_PLAYER_VIEW);
			model.put("jugador", jugador);
			resul.addObject("message", MyErrorController.getErrorMessage(br));
		} else {
			List<Jugador> lista = playerService.findAllJugadores();
			
			if(Boolean.FALSE.equals(isYourEmail(jugador, jugadorId)) && Boolean.TRUE.equals(PlayerValidation.isRegisteredEmail(jugador, model, lista)) 
					|| Boolean.FALSE.equals(PlayerValidation.isValidEmail(model, jugador)) || Boolean.FALSE.equals(PlayerValidation.isCorrectPassword(jugador, model, correctPassword))
							|| Boolean.TRUE.equals(PlayerValidation.firstNameOrLastNameAreEmpty(jugador, model))
							|| (Boolean.TRUE.equals(PlayerValidation.usernameRegistered(jugador, model, lista))
									&& PlayerValidation.noEsTuUsername(jugadorActual,jugador))) {
				resul = new ModelAndView(CREATE_OR_UPDATE_PLAYER_VIEW);
				model.put("jugador", jugador);
			} else {
				jugador.setId(jugadorId);
				jugador.getUser().setId(playerService.findJugadorById(jugadorId).getUser().getId());
				this.playerService.saveJugador(jugador);
				resul = new ModelAndView("redirect:/jugadores/" + jugadorId);
			}
		}
		return resul;
	}

	private Boolean isYourEmail(Jugador jugador, int jugadorId) {
		return playerService.findJugadorById(jugadorId).getUser().getEmail().equals(jugador.getUser().getEmail());
	}

	@GetMapping(value = "/jugadores/{jugadorId}/playerMatches")
	public ModelAndView showMatchesOfAPlayer(@PathVariable("jugadorId") int jugadorId) {

		ModelAndView result = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUser(auth.getName()).get();
		String username = user.getUsername();
		Jugador player = playerService.findPlayerByUsername(username);

		if (matchService.getMatchesOfAPlayer(player.getId()).size() == 0) {
			result = new ModelAndView("welcome");
			result.addObject("message", "Aún no has disputado ninguna partida");
		} else {
			for (Authorities authority : user.getAuthorities()) {
				if (authority.getAuthority().equals("jugador")
						|| playerService.findPlayerByUsername(auth.getName()).getId() == jugadorId) {

					result = new ModelAndView("/jugadores/playerMatches");
					Collection<Match> m = matchService.getMatchesOfAPlayer(jugadorId);
					m.removeAll(matchService.getMatchesByGameWinner(GameWinner.UNDEFINED));
					result.addObject("playerMatches", m.stream().limit(RESULTS_LIMIT).collect(Collectors.toList()));
					result.addObject("gamesPlayed", m.size());
					result.addObject("gamesWon", player.getNumberOfGamesWon());
					result.addObject("totalPlayingGame", player.getTotalPlayingGame());
					result.addObject("durationOfTheLongestGame", player.getDurationOfTheLongestGame());
				}
			}
		}

		return result;
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------- //

	@GetMapping(value = "/jugadores/{jugadorId}/playerFriends")
	public ModelAndView showFriendsOfAPlayer(@PathVariable("jugadorId") int jugadorId) {
		ModelAndView result = new ModelAndView("/jugadores/playerFriends");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUser(auth.getName()).get();
		String username = user.getUsername();
		Jugador player = playerService.findPlayerByUsername(username);

		for (Authorities authority : user.getAuthorities()) {
			if (authority.getAuthority().equals("jugador")
					&& playerService.findPlayerByUsername(auth.getName()).getId() == jugadorId) {

				Collection<Jugador> f = player.playerFriends();
				result.addObject("jugadorId", jugadorId);
				result.addObject("playerFriends", f);
			}
		}

		return result;
	}

	@GetMapping(value = "/jugadores/friendRequests")
	public ModelAndView friendRequests(@AuthenticationPrincipal Authentication user) {
		ModelAndView result = new ModelAndView("/jugadores/friendRequests");
		List<Jugador> listPlayers = playerService.findPlayerByUsername(user.getName()).playersWhoHaveSentYouAFriendRequest();
		
		result.addObject("loggedPlayerId", playerService.findPlayerByUsername(user.getName()).getId());
		result.addObject("listPlayers", listPlayers);

		return result;
	}
	
	@GetMapping(value = "/jugadores/friendRequests/{player1Id}/{player2Id}/{result}")
	public ModelAndView friendRequests(@PathVariable("player1Id") int player1Id, @PathVariable("player2Id") int player2Id, 
			@PathVariable("result") boolean result, RedirectAttributes ra) {
		ModelAndView mv;
		String message = "";
		if(result && playerService.findJugadorById(player2Id).playerFriends().size() >= FRIEND_LIMIT) {
			message = "You have reached the limit number of friends";
		} else if(result && playerService.findJugadorById(player1Id).playerFriends().size() >= FRIEND_LIMIT) {
			message = "That player has reached the friend limit";
			System.out.println("prueba3");
		} else {
			System.out.println("prueba4");
			FriendRequest fr = friendRequestService.getNoReplyFriendRequestByPlayers(player1Id, player2Id);
			fr.setResultado(result);
			friendRequestService.saveFriendRequest(fr);
			message = result ? "Request successfully accepted" : "Request successfully declined";  
		}
		
		mv = new ModelAndView("/jugadores/friendRequests");
		mv.setViewName("redirect:/jugadores/friendRequests");
		ra.addFlashAttribute("message", message);
		
		return mv;
	}
	
	@GetMapping(value = "/jugadores/addFriends")
	public ModelAndView addFriends(Model model, @Param("keyword") String keyword, @AuthenticationPrincipal Authentication user) {
		ModelAndView result = new ModelAndView("/jugadores/addFriends");
		List<Jugador> listPlayers = playerService.findPlayerByKeyword(keyword);
		
		if(listPlayers.contains(playerService.findPlayerByUsername(user.getName()))) {
			listPlayers.remove(playerService.findPlayerByUsername(user.getName()));
		}

		result.addObject("loggedPlayerId", playerService.findPlayerByUsername(user.getName()).getId());
		result.addObject("listPlayers", listPlayers);
		model.addAttribute("keyword", keyword);

		return result;
	}
	
	@GetMapping(value = "/jugadores/addFriends/{player1Id}/{player2Id}")
	public ModelAndView addFriends(@PathVariable("player1Id") int player1Id, @PathVariable("player2Id") int player2Id, RedirectAttributes ra) {
		ModelAndView result;
		String message = "";
		
		if(friendRequestService.getNoReplyFriendRequestByPlayers(player1Id, player2Id) != null) { 
			message = "You have already sent a friend request to this player";
		} else if(friendRequestService.getNoReplyFriendRequestByPlayers(player2Id, player1Id) != null) {
			message = "You have a pending friend request from this player";
		} else if(friendRequestService.getFriendshipByPlayers(player1Id, player2Id) != null || 
				friendRequestService.getFriendshipByPlayers(player2Id, player1Id) != null) {
			message = "You are already friends with this player";
		} else {
			if(playerService.findJugadorById(player1Id).playerFriends().size() >= FRIEND_LIMIT) {
				message = "You have reached the limit number of friends";
			} else if(playerService.findJugadorById(player2Id).playerFriends().size() >= FRIEND_LIMIT) {
				message = "That player has reached the friend limit";
			} else {
				friendRequestService.saveFriendRequest(new FriendRequest(playerService.findJugadorById(player1Id), playerService.findJugadorById(player2Id)));
				message = "Friend request has been sent successfully";
			}
		}
		
		result = new ModelAndView("/jugadores/addFriends");
		result.setViewName("redirect:/jugadores/addFriends");
		ra.addFlashAttribute("message", message);
		
		return result;
	}

	@GetMapping("/jugadores/{jugadorId1}/playerFriends/{jugadorId2}/delete")
	public ModelAndView deleteFriend(@PathVariable("jugadorId1") int jugadorId1,
			@PathVariable("jugadorId2") int jugadorId2, RedirectAttributes ra) {
		ModelAndView result = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUser(auth.getName()).get();
		Boolean hasDeletedFriend = false;
		
		for (Authorities authority : user.getAuthorities()) {
			if (authority.getAuthority().equals("jugador") && playerService.findPlayerByUsername(auth.getName()).getId() == jugadorId1) {
				if(friendRequestService.getFriendRequestByPlayers(jugadorId1, jugadorId2) != null) {
					friendRequestService.deleteFriendRequest(friendRequestService.getFriendRequestByPlayers(jugadorId1, jugadorId2));
					hasDeletedFriend = true;
				} else if(friendRequestService.getFriendRequestByPlayers(jugadorId2, jugadorId1) != null) {
					friendRequestService.deleteFriendRequest(friendRequestService.getFriendRequestByPlayers(jugadorId2, jugadorId1));
					hasDeletedFriend = true;
				}
			}
		}
		
		result.setViewName("redirect:/jugadores/{jugadorId1}/playerFriends");
		
		if (Boolean.TRUE.equals(hasDeletedFriend)) {
			ra.addFlashAttribute("message", "Friend was deleted succesfully");
		}

		return result;
	}
	
	// --------------------------------------------------------------------------------------------------------------------------------------------- //

}
