package org.springframework.samples.petclinic.jugador;

import java.util.Collection;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.user.Authorities;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

	private static final String LIST_PLAYER_MATCHES = "/jugadores/playerMatches";


	

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/jugadores")
	public String showAllPlayers(Map<String, Object> model) {
		Collection<Jugador> results = this.playerService.findAllJugadores();
		model.put("selections", results);
		return "jugadores/listJugador";
	}
	
	@GetMapping(value = "/perfil")
	public String showPerfil() {
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		User user=userService.findUser(auth.getName()).get();
		Integer id=playerService.findJugadorByUserName(user.getUsername()).getId();
		return "redirect:/jugadores/"+id;
	}
  
	@GetMapping(value = "/jugadores/{jugadorId}")
	public ModelAndView showPlayer(@PathVariable("jugadorId") int id) {
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		User user=userService.findUser(auth.getName()).get();
		ModelAndView mav=new ModelAndView();
		for(Authorities authority:user.getAuthorities()) {
			if(authority.getAuthority().equals("admin") || playerService.findJugadorByUserName(auth.getName()).getId()==id) {
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
	public String initCreationForm(Map<String, Object> model,Model model2) {
		Jugador jugador = new Jugador();
		model.put("jugador", jugador);
		return "jugadores/createOrUpdateJugadorForm";
	}

	@PostMapping(value = "/jugadores/new")
	public String processCreationForm(@Valid Jugador jugador, BindingResult result) {
		if (result.hasErrors()) {
			return "jugadores/createOrUpdateJugadorForm";
		} else {
			this.playerService.saveJugador(jugador);

			return "redirect:/jugadores/" + jugador.getId();
		}
	}

	@GetMapping(value = "/jugadores/{jugadorId}/edit")
	public String initUpdateOwnerForm(@PathVariable("jugadorId") int jugadorId, Model model) {
		Jugador jugador = this.playerService.findJugadorById(jugadorId);
		model.addAttribute(jugador);
		return "jugadores/createOrUpdateJugadorForm";
	}

	@PostMapping(value = "/jugadores/{jugadorId}/edit")
	public String processUpdateOwnerForm(@Valid Jugador jugador, BindingResult result,
			@PathVariable("jugadorId") int jugadorId) {
		if (result.hasErrors()) {
			return "jugadores/createOrUpdateJugadorForm";
		} else {
			jugador.setId(jugadorId);
			jugador.getUser().setUsername(playerService.findJugadorById(jugadorId).getUser().getUsername());
			this.playerService.saveJugador(jugador);
			return "redirect:/jugadores/{jugadorId}";
		}
	}

	@GetMapping(value = "/jugadores/{jugadorId}/matches")
	public ModelAndView showMatchesOfAPlayer(@PathVariable int jugadorId) {
		ModelAndView result = new ModelAndView(LIST_PLAYER_MATCHES);
		result.addObject("playerMatches", matchService.getMatchesOfAPlayer(jugadorId));
		return result;
	}

}
