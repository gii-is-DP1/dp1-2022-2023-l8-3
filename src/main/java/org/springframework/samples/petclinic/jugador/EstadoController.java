package org.springframework.samples.petclinic.jugador;

import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EstadoController {
	
	private UserService userService;
	private PlayerService playerService;
	
	@PostMapping("/cambiarEstadoOnline")
	public String ponerOnline(@AuthenticationPrincipal Authentication auth) {
//		String loggedUser=auth.getName();
//		User usuario= userService.findUser(loggedUser).get();
//		if(usuario.getAuthorities().equals("jugador")) {
//			Jugador j=playerService.findJugadorByUsername(loggedUser);
//			j.setEstadoOnline(true);
//		}
		
		return "/welcome";
	}
	
	@GetMapping("/cambiarEstadoOffline")
	public String ponerOffline() {
		
		
		return "/welcome";
	}
	
	
}
