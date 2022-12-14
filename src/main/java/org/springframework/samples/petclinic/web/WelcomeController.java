package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.invitacion.InvitationService;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.menu.MenuService;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.user.Authorities;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomeController {
	
	private InvitationService invitacionService;
	private MenuService menuService;
	private PlayerService playerService;
	
	@Autowired
	public WelcomeController(InvitationService invitacionService,MenuService menuService,PlayerService playerService) {
		this.playerService = playerService;
		this.menuService=menuService;
		this.invitacionService=invitacionService;
	}

	@GetMapping("/")
	  public ModelAndView welcome() {	    
		ModelAndView result=new ModelAndView("welcome");
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		System.out.println("error2");
		User user1=new User("testUser1","testUser1");
		Set<Authorities> conj=new HashSet<Authorities>();
		Authorities authority=new Authorities(); authority.setUser(user1);authority.setAuthority("admin");
		conj.add(authority);
    	user1.setAuthorities(conj);
		
		if(auth!=null) {
			System.out.println("error1");
			Boolean b=true;
			for(Authorities a:menuService.findUser(auth.getName()).orElse(user1).getAuthorities()) {
				System.out.println("error4");
				if(a.getAuthority().equals("admin")) {
					b=false;
				}
			}
			if(b) {
				System.out.println("error5");
				Jugador jugadorActual=menuService.findPlayerByUsername(auth.getName());
				List<Invitacion> lista=invitacionService.getInvitacionByInvitadoId(jugadorActual.getId());
				if(lista.isEmpty()) {
					result.addObject("tengoInvitaciones",false);
				}
				else {
				result.addObject("tengoInvitaciones",true);
				result.addObject("mensaje","Ha recibido una invitaci&oacute;n a una partida, acceda a trav&eacute;s de la campana del menu");
				}
			}
		}
		else {
			System.out.println("error3");
			result.addObject("tengoInvitaciones",false);
			result.addObject("mensaje","este mensaje no vale para nada");
		}
		return result;
	}
	
	@GetMapping(value = "/registerNewJugador")
	public String getRegisterNewPlayer(Map<String, Object> model, Map<String, Object> model2) {
		Jugador jugador = new Jugador();
		model.put("jugador", jugador);
		Collection<Jugador> c = this.playerService.findAllJugadores();
		if (c.isEmpty()) {
			model2.put("sinJugadores", true);
		}
		return "jugadores/createOrUpdateJugadorForm";
	}

	@PostMapping(value = "/registerNewJugador")
	public String postRegisterNewPlayer(@Valid Jugador jugador, BindingResult result, Map<String, Object> model) {
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

			return "redirect:/login";
		}
	}
}
