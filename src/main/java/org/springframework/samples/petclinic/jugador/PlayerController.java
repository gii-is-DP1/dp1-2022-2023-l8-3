package org.springframework.samples.petclinic.jugador;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PlayerController {
	
	@Autowired
	private PlayerService playerService;
	
//	@GetMapping(value = "/jugadores")
//	public String processFindForm(Jugador jugador, BindingResult result, Map<String, Object> model) {
//
//		// allow parameterless GET request for /owners to return all records
//		if (jugador.getLastName() == null) {
//			jugador.setLastName(""); // empty string signifies broadest possible search
//		}
//
//		// find owners by last name
//		Collection<Jugador> results = this.playerService.findOwnerByLastName(owner.getLastName());
//		if (results.isEmpty()) {
//			// no owners found
//			result.rejectValue("lastName", "notFound", "not found");
//			return "owners/findOwners";
//		}
//		else if (results.size() == 1) {
//			// 1 owner found
//			owner = results.iterator().next();
//			return "redirect:/owners/" + owner.getId();
//		}
//		else {
//			// multiple owners found
//			model.put("selections", results);
//			return "owners/ownersList";
//		}
//	}
	
	
	@GetMapping(value = "/jugadores")
	public String showAllPlayers(Map<String, Object> model,Jugador jugador) {
		List<Jugador> lista=playerService.findAllJugadores();
		
		Collection<Jugador> results = this.playerService.findJugadorById(jugador.getId()));
				model.put("selections", results);
		return "Jugador/jugador";
	}
	
	
	
	@GetMapping("/owners/{jugadorId}")
	public ModelAndView showPlayer(@PathVariable("jugadorId") int id) {
		ModelAndView mav = new ModelAndView("jugadores/jugador");
		mav.addObject(this.playerService.findJugadorById(id));
		return mav;
	}

}
