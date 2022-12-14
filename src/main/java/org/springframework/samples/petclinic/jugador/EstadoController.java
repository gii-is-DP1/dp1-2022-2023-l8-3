package org.springframework.samples.petclinic.jugador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.user.Authorities;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EstadoController {
	
	private UserService userService;
	private PlayerService playerService;
	
	@Autowired
	public EstadoController(PlayerService playerService, UserService userService) {
		this.playerService = playerService;
		this.userService = userService;
	}
	
	Authentication auth=SecurityContextHolder.getContext().getAuthentication();
	
	@GetMapping("/cambiarEstadoOnline")
	public String ponerOnline() {
		auth=SecurityContextHolder.getContext().getAuthentication();
		User user=userService.findUser(auth.getName()).get();
		Jugador j=playerService.findPlayerByUsername(auth.getName());
		Boolean b=false;  //el boolean es necesario porque si se modifica el player dentro del for da error al loguearte 2 veces seguidas
		for(Authorities a:user.getAuthorities()) {
			if(a.getAuthority().equals("jugador")) {
				b=true;
			}
		}
		if(b) {
			j.setEstadoOnline(true);
			playerService.saveJugador(j);
		}
		return "redirect:/";
	}
	
	@GetMapping("/cambiarEstadoOffline")
	public String ponerOffline() {
		User user=userService.findUser(auth.getName()).get();
		Jugador j=playerService.findPlayerByUsername(auth.getName());
		Boolean b=false;  //el boolean es necesario porque si se modifica el player dentro del for da error al loguearte 2 veces seguidas
		for(Authorities a:user.getAuthorities()) {
			if(a.getAuthority().equals("jugador")) {
				b=true;
			}
		}
		if(b) {
			j.setEstadoOnline(false);
			playerService.saveJugador(j);
		}

		
		return "redirect:/";
	}
	
	
}
