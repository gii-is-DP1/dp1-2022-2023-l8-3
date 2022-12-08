package org.springframework.samples.petclinic.jugador;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.user.Authorities;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.samples.petclinic.partida.GameWinner;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PlayerController {

	private PlayerService playerService;
	private UserService userService;
	private MatchService matchService;

	@Autowired
	public PlayerController(PlayerService playerService, UserService userService, MatchService matchService) {
		this.playerService = playerService;
		this.userService = userService;
		this.matchService = matchService;

	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/jugadores")
	public String showAllPlayers(Map<String, Object> model) {
		Collection<Jugador> results = this.playerService.findAllJugadores();
		model.put("selections", results);
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
					|| playerService.findPlayerByUsername(auth.getName()).getId() == id) {
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
		} catch (Exception e) {
			throw new Exception("Player Delete Error");
		}
		return "redirect:/jugadores";
	}

	@GetMapping(value = "/jugadores/new")
	public String initCreationForm(Map<String, Object> model, Map<String, Object> model2) {
		System.out.println("pepepe");
		Jugador jugador = new Jugador();
		model.put("jugador", jugador);
		Collection<Jugador> c = this.playerService.findAllJugadores();
		if (c.isEmpty()) {
			model2.put("sinJugadores", true);
		}
		return "jugadores/createOrUpdateJugadorForm";
	}

	@PostMapping(value = "/jugadores/new")
	public String processCreationForm(@Valid Jugador jugador, BindingResult result, Map<String, Object> model) {
		if (result.hasErrors()) {
			return "jugadores/createOrUpdateJugadorForm";
		} else {
			Boolean contraseñaCorrecta = false;
			List<Jugador> lista = playerService.findAllJugadores();
			// Comprobar si el correo ya esta registrado con otro jugador
			for (Jugador j : lista) {
				if (j.getUser().getEmail().equals(jugador.getUser().getEmail())) {
					model.put("emailIncorrecto1", true);
					return "jugadores/createOrUpdateJugadorForm";
				}
			}
			// Comprobar si el correo acaba en @gmail.com
			if (!(jugador.getUser().getEmail().endsWith("@gmail.com"))) {
				model.put("emailIncorrecto2", true);
				return "jugadores/createOrUpdateJugadorForm";
			}
			// Comprobar si la contraseña esta entre 10 y 50 y contiene al menos un numero
			for (Integer i = 0; i < 10; i++) {
				if (jugador.getUser().getPassword().length() > 10 && jugador.getUser().getPassword().length() < 50
						&& jugador.getUser().getPassword().contains(i.toString())) {
					contraseñaCorrecta = true;
				}
			}
			if (contraseñaCorrecta == false) {
				model.put("contraseñaIncorrecta", true);
				return "jugadores/createOrUpdateJugadorForm";
			}
			this.playerService.saveJugador(jugador);

			return "redirect:/jugadores/" + jugador.getId();
		}
	}

	@GetMapping(value = "/jugadores/{jugadorId}/edit")
	public ModelAndView initUpdateOwnerForm(@PathVariable("jugadorId") int jugadorId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUser(auth.getName()).get();
		ModelAndView mav = new ModelAndView();
		for (Authorities authority : user.getAuthorities()) {
			if (authority.getAuthority().equals("admin")
					|| playerService.findPlayerByUsername(auth.getName()).getId() == jugadorId) {
				mav = new ModelAndView("jugadores/createOrUpdateJugadorForm");
				mav.addObject(this.playerService.findJugadorById(jugadorId));
			}
		}
		return mav;
	}

	@PostMapping(value = "/jugadores/{jugadorId}/edit")
	public String processUpdateOwnerForm(@Valid Jugador jugador, BindingResult result,
			@PathVariable("jugadorId") int jugadorId, Map<String, Object> model) {
		if (result.hasErrors()) {
			return "jugadores/createOrUpdateJugadorForm";
		} else {
			Boolean contraseñaCorrecta = false;
			List<Jugador> lista = playerService.findAllJugadores();
			// Comprobar si el correo ya esta registrado con otro jugador que no seas tu
			// mismo
			for (Jugador j : lista) {
				if (j.getUser().getEmail().equals(jugador.getUser().getEmail())
						&& !(playerService.findJugadorById(jugadorId) == j)) {
					model.put("emailIncorrecto1", true);
					return "jugadores/createOrUpdateJugadorForm";
				}
			}
			// Comprobar si el correo acaba en @gmail.com
			if (!(jugador.getUser().getEmail().endsWith("@gmail.com"))) {
				model.put("emailIncorrecto2", true);
				return "jugadores/createOrUpdateJugadorForm";
			}
			// Comprobar si la contraseña esta entre 10 y 50 y contiene al menos un numero
			for (Integer i = 0; i < 10; i++) {
				if (jugador.getUser().getPassword().length() > 10 && jugador.getUser().getPassword().length() > 10
						&& jugador.getUser().getPassword().contains(i.toString())) {
					contraseñaCorrecta = true;
				}
			}
			if (contraseñaCorrecta == false) {
				model.put("contraseñaIncorrecta", true);
				return "jugadores/createOrUpdateJugadorForm";
			}
			jugador.setId(jugadorId);
			jugador.getUser().setId(playerService.findJugadorById(jugadorId).getUser().getId());
			this.playerService.saveJugador(jugador);
			return "redirect:/jugadores/{jugadorId}";
		}
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
					result.addObject("playerMatches", m);
				}
			}
		}

		return result;
	}

	@GetMapping(value = "/jugadores/{jugadorId}/playerFriends")
	public ModelAndView showFriendsOfAPlayer(@PathVariable("jugadorId") int jugadorId) {
		ModelAndView result = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUser(auth.getName()).get();
		String username = user.getUsername();
		Jugador player = playerService.findPlayerByUsername(username);

		for (Authorities authority : user.getAuthorities()) {
			if (authority.getAuthority().equals("jugador")
					|| playerService.findPlayerByUsername(auth.getName()).getId() == jugadorId) {

				result = new ModelAndView("/jugadores/playerFriends");
				Collection<Jugador> f = player.playerFriends();
				result.addObject("playerFriends", f);
			}
		}

		return result;
	}

}
