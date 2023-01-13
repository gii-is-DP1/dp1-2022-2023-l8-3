package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.invitacion.InvitationService;
import org.springframework.samples.petclinic.invitacion.resultadoInvitacion;
import org.springframework.samples.petclinic.invitacion.tipoInvitacion;
import org.springframework.samples.petclinic.jugador.FriendRequest;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.menu.MenuService;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.samples.petclinic.statistics.AchievementService;
import org.springframework.samples.petclinic.user.Authorities;
import org.springframework.samples.petclinic.user.User;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = WelcomeController.class,		
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)

public class WelcomeControllerTest {
	
	@MockBean
    private MatchService matchService;
	
	@MockBean
    private InvitationService invitacionService;

	@MockBean
    private MenuService menuService;
	
	@MockBean
	private PlayerService playerService;
	
	@MockBean
	private AchievementService achievementService;

    @Autowired
    private MockMvc mockMvc;
    
    void config(Boolean eresInvitado,Boolean jugador2Unido) {
		User user= new User("testUser1", "testUser1");
		Set<Authorities> auth = new HashSet<Authorities>();
		Authorities authority = new Authorities();
		authority.setUser(user);
		authority.setAuthority("jugador");
		auth.add(authority);
		user.setAuthorities(auth);
		user.setId(1);
		user.setEmail("test11111111@gmail.com");
		
		User user2= new User("testUser2", "testUser2");
		user2.setAuthorities(auth);
		user2.setId(2);
		user2.setEmail("test2222222@gmail.com");

		Jugador jugador = new Jugador("test1", "test1", user, false);
		jugador.setId(1);
		jugador.setGamesAsHost(new ArrayList<Match>());
		jugador.setGamesAsGuest(new ArrayList<Match>());
		jugador.setReceivedFriendRequests(new ArrayList<FriendRequest>());
		jugador.setSentFriendRequests(new ArrayList<FriendRequest>());
		
		Jugador jugador2 = new Jugador("test2", "test2", user2, false);
		jugador2.setId(2);
		jugador2.setGamesAsHost(new ArrayList<Match>());
		jugador2.setGamesAsGuest(new ArrayList<Match>());
		jugador2.setReceivedFriendRequests(new ArrayList<FriendRequest>());
		jugador2.setSentFriendRequests(new ArrayList<FriendRequest>());
		
		Optional<User> userr = Optional.of(user);
		
		Match match=new Match(false,jugador);
		match.setId(1);
		match.setFinPartida(null);
		Collection<Match> coleccion=new ArrayList<Match>();
		
		List<Invitacion> li=new ArrayList<Invitacion>();
		if(eresInvitado) {
			match.setJugador1(jugador2);
			List<Jugador> lista=new ArrayList<Jugador>();
			lista.add(jugador);
			jugador2.setAmigosInvitados(lista);
			Invitacion i=new Invitacion(resultadoInvitacion.SIN_RESPONDER, tipoInvitacion.JUGADOR, match, jugador);
			li.add(i);
		}
		
		if(jugador2Unido) {
			match.setJugador2(jugador2);
			coleccion.add(match);
			given(this.matchService.getMatches()).willReturn(coleccion);
		}
		else {
			match.setJugador2(null);
			coleccion.add(match);
			given(this.matchService.getMatches()).willReturn(coleccion);
		}
		
		given(this.menuService.findUser("testUser1")).willReturn(userr);
		given(this.menuService.findPlayerByUsername("testUser1")).willReturn(jugador);
		given(this.invitacionService.getInvitacionByInvitadoId(jugador.getId())).willReturn(li);
		given(this.playerService.findAllJugadores()).willReturn(new ArrayList<>());
	}
    
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testWelcome1() throws Exception {
    	//jugador logeado es invitado
    	config(true, false);
    	mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("players"))
				.andExpect(model().attribute("tengoInvitaciones", true))
				.andExpect(model().attribute("mensaje","Ha recibido una invitaci&oacute;n a una partida, acceda a trav&eacute;s de la campana del menu"))
				.andExpect(view().name("welcome"));
    }
    
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testWelcome2() throws Exception {
    	//jugador ha creado partida y se encuentra esperando a que se una alguien
    	config(false, false);
    	mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("jugador2NoUnido", true))
				.andExpect(model().attribute("tengoInvitaciones", false))
				.andExpect(model().attribute("matchId",1))
				.andExpect(view().name("welcome"));
    }
    
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testWelcome3() throws Exception {
    	//jugador ha creado partida y otro jugador se une
    	config(false, true);
    	mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("partidaPendiente", true))
				.andExpect(model().attribute("tengoInvitaciones", false))
				.andExpect(model().attribute("matchId",1))
				.andExpect(view().name("welcome"));
    }
    
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testGetRegisterNewPlayer() throws Exception {
    	given(playerService.findAllJugadores()).willReturn(new ArrayList<Jugador>());
    	
    	mockMvc.perform(get("/registerNewJugador"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("sinJugadores"))
				.andExpect(view().name("jugadores/createOrUpdateJugadorForm"));
    }
    
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testPostRegisterNewPlayerSuccess() throws Exception {
    	given(playerService.findAllJugadores()).willReturn(new ArrayList<Jugador>());
    	Jugador j=new Jugador();
    	given(playerService.saveJugador(j)).willReturn(null);
    	
    	mockMvc.perform(post("/registerNewJugador").param("user.username", "pepito").param("firstName", "Pepe")
				.param("lastName", "Ruiz").with(csrf()).param("user.email", "pepe@gmail.com")
				.param("user.password", "contrasenya123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/login"));
    }
    
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testPostRegisterNewPlayerHasErrors() throws Exception {
    	given(playerService.findAllJugadores()).willReturn(new ArrayList<Jugador>());
    	Jugador j=new Jugador();
    	given(playerService.saveJugador(j)).willReturn(null);
    	
    	mockMvc.perform(post("/registerNewJugador").param("user.username", "pepe").param("firstName", "Pepe")
				.param("lastName", "Ruiz").with(csrf()).param("user.email", "emailIncorrecto") //emailIncorrecto
				.param("user.password", "contrasenya123"))
				.andExpect(status().isOk())
				.andExpect(view().name("jugadores/createOrUpdateJugadorForm"));
    }
}
