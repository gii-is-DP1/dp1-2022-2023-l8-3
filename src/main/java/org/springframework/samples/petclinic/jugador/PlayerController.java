package org.springframework.samples.petclinic.jugador;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.springframework.web.bind.annotation.RequestMapping;
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

	@Autowired
	public PlayerController(PlayerService playerService, UserService userService, MatchService matchService,
			FriendRequestService friendRequestService) {
		this.playerService = playerService;
		this.userService = userService;
		this.matchService = matchService;
		this.friendRequestService = friendRequestService;
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
		Jugador jugador = new Jugador();
		model.put("jugador", jugador);
		Collection<Jugador> c = this.playerService.findAllJugadores();
		if (c.isEmpty()) {
			model2.put("sinJugadores", true);
		}
		return CREATE_OR_UPDATE_PLAYER_VIEW;
	}

	@PostMapping(value = "/jugadores/new")
	public ModelAndView processCreationForm(@Valid JugadorDTO jugadorDto, BindingResult br, Map<String, Object> model) {
		Boolean correctPassword = false;
		ModelAndView resul;
		if (Boolean.TRUE.equals(br.hasErrors())) {
			log.error("Input error");
			resul = new ModelAndView(CREATE_OR_UPDATE_PLAYER_VIEW, br.getModel());
		} else {
			List<Jugador> lista = playerService.findAllJugadores();
			ManualJugadorMapper m = new ManualJugadorMapper();
			Jugador jugador = m.convertJugadorDTOToEntity(jugadorDto);
			
			if(isRegisteredEmail(jugador, model, lista) || !isValidEmail(model, jugador) || !isCorrectPassword(jugador, model, correctPassword)) {
				resul = new ModelAndView(CREATE_OR_UPDATE_PLAYER_VIEW);
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
		
		if (Boolean.TRUE.equals(br.hasErrors())) {
			log.error("Input error");
			resul = new ModelAndView(CREATE_OR_UPDATE_PLAYER_VIEW, br.getModel());
		} else {
			List<Jugador> lista = playerService.findAllJugadores();
			ManualJugadorMapper m = new ManualJugadorMapper();
			Jugador jugador = m.convertJugadorDTOToEntity(jugadorDto);
			
			if((Boolean.FALSE.equals(isYourEmail(jugador, jugadorId)) && Boolean.TRUE.equals(isRegisteredEmail(jugador, model, lista))) 
					|| Boolean.FALSE.equals(isValidEmail(model, jugador)) || Boolean.FALSE.equals(isCorrectPassword(jugador, model, correctPassword))) {
				resul = new ModelAndView(CREATE_OR_UPDATE_PLAYER_VIEW);
				resul.addObject(this.playerService.findJugadorById(jugadorId));
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

	private Boolean isRegisteredEmail(Jugador jugador, Map<String, Object> model, List<Jugador> lista) {
		Boolean result = false;
		Integer i = 0;
		while(!result && i < lista.size()) {
			if (lista.get(i).getUser().getEmail().equals(jugador.getUser().getEmail())) {
				model.put("emailIncorrecto1", true);
				result = true;
			}
			i++;
		}
		return result;
	}
	
	private Boolean isValidEmail(Map<String, Object> model, Jugador player) {
		Boolean result = true;
		String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" +"[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
		Pattern pattern = Pattern.compile(emailPattern);
		Matcher matcher = pattern.matcher(player.getUser().getEmail());
		
		if (Boolean.FALSE.equals(matcher.matches())) {
			model.put("emailIncorrecto2", true);
			result = false;
		}
		
		return result;
	}

	private Boolean isCorrectPassword(Jugador player, Map<String, Object> model, Boolean correctPassword) {
		Integer i = 0;
		
		if(player.getUser().getPassword().length() >= 10 && player.getUser().getPassword().length() <= 50) {
			while(Boolean.FALSE.equals(correctPassword) && i < 10) {
				if (player.getUser().getPassword().contains(i.toString())) {
					correctPassword = true;
				}
				i++;
			}
		}
		if(Boolean.FALSE.equals(correctPassword)) {
			model.put("contraseñaIncorrecta", true);
		}
		
		return correctPassword;
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
	
	@RequestMapping(value = "/jugadores/friendRequests/{player1Id}/{player2Id}/{result}")
	public ModelAndView friendRequests(@PathVariable("player1Id") int player1Id, @PathVariable("player2Id") int player2Id, 
			@PathVariable("result") boolean result, RedirectAttributes ra) {
		ModelAndView mv;
		String message = "";
		System.out.println("prueba1");
		if(result && playerService.findJugadorById(player2Id).playerFriends().size() >= FRIEND_LIMIT) {
			message = "You have reached the limit number of friends";
			System.out.println("prueba2");
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
		
		System.out.println("prueba5");
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
	
	@RequestMapping(value = "/jugadores/addFriends/{player1Id}/{player2Id}")
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
