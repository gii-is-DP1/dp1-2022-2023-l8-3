package org.springframework.samples.petclinic.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.comentario.ComentarioController;
import org.springframework.samples.petclinic.disco.DishController;
import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.invitacion.InvitationController;
import org.springframework.samples.petclinic.invitacion.InvitationService;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerController;
import org.springframework.samples.petclinic.partida.MatchController;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.samples.petclinic.statistics.AchievementController;
import org.springframework.samples.petclinic.user.Authorities;
import org.springframework.samples.petclinic.user.UserController;
import org.springframework.samples.petclinic.web.WelcomeController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = {ComentarioController.class,DishController.class,InvitationController.class,PlayerController.class,MatchController.class,AchievementController.class})
public class MenuController {
	
	private InvitationService invitacionService;
	private MenuService menuService;
	
	@Autowired
	public MenuController(InvitationService invitacionService,MenuService menuService) {
		this.menuService=menuService;
		this.invitacionService=invitacionService;

	}
	
	@ModelAttribute
	public void addAtributes(Model model,@AuthenticationPrincipal Authentication user) {
		if(user.isAuthenticated()) {
			Boolean b=true;
			for(Authorities a:menuService.findUser(user.getName()).get().getAuthorities()) {
				if(a.getAuthority().equals("admin")) {
					b=false;
				}
			}
			if(b) {
				Jugador jugadorActual=menuService.findPlayerByUsername(user.getName());
				List<Invitacion> lista=invitacionService.getInvitacionByInvitadoId(jugadorActual.getId());
				if(lista.isEmpty()) {
					model.addAttribute("tengoInvitaciones",false);
				}
				else {
				model.addAttribute("tengoInvitaciones",true);
				model.addAttribute("mensaje","Ha recibido una invitaci&oacute;n a una partida, acceda a trav&eacute;s de la campana del menu");
				}
			}
		}
	}

}
