package org.springframework.samples.petclinic.jugador;

import java.util.Collection;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
<<<<<<< HEAD
=======
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
>>>>>>> davdancab
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PlayerController {
	
	private PlayerService playerService;
	
	@Autowired
	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	
	@GetMapping(value = "/jugadores")
	public String showAllPlayers(Map<String, Object> model,Jugador jugador) {
		Collection<Jugador> results = this.playerService.findAllJugadores();
		model.put("selections", results);
		return "jugadores/listJugador";
	}
	
	@GetMapping(value = "/jugadores/{jugadorId}")
	public ModelAndView showPlayer(@PathVariable("jugadorId") int id) {
		ModelAndView mav = new ModelAndView("jugadores/showJugador");
		mav.addObject(this.playerService.findJugadorById(id));
		return mav;
	}
	
	
	@GetMapping(value = "/jugadores/{jugadorId}/delete")
	public String deletePlayer(@PathVariable("jugadorId") int id)throws Exception {
		try {
			playerService.deletePlayer(id);
		}
		catch (Exception e) {
			throw new Exception("Player Delete Error");
		}
		return "redirect:/jugadores";
	}
	
	
	@GetMapping(value = "/jugadores/new")
	public String initCreationForm(Map<String, Object> model) {
		Jugador jugador = new Jugador();
		model.put("jugador", jugador);
		return "jugadores/createOrUpdateJugadorForm";
	}

	@PostMapping(value = "/jugadores/new")
	public String processCreationForm(@Valid Jugador jugador, BindingResult result) {
		if (result.hasErrors()) {
			return "jugadores/createOrUpdateJugadorForm";
		}
		else {
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
		}
		else {
			jugador.setId(jugadorId);
			this.playerService.saveJugador(jugador);
			return "redirect:/jugadores/{jugadorId}";
		}
	}

	

}
