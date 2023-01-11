package org.springframework.samples.petclinic.invitation;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
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
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.invitacion.InvitationController;
import org.springframework.samples.petclinic.invitacion.InvitationService;
import org.springframework.samples.petclinic.invitacion.resultadoInvitacion;
import org.springframework.samples.petclinic.invitacion.tipoInvitacion;
import org.springframework.samples.petclinic.jugador.FriendRequest;
import org.springframework.samples.petclinic.jugador.FriendRequestService;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerController;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.menu.MenuService;
import org.springframework.samples.petclinic.partida.GameWinner;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.samples.petclinic.user.Authorities;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = InvitationController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)


public class InvitationControllerTest {
	
	@MockBean
	private MatchService matchService;

	@MockBean
	private PlayerService playerService;

	@MockBean
	private InvitationService invitacionService;
	
	@MockBean
	private MenuService menuService;

	@Autowired
	private MockMvc mockMvc;
	
	
	public void config(Boolean invitacionComoJugador,Boolean partidaConJugador2) {

		User user= new User("testUser1", "testUser1");
		user.setUsername("testUser1");
		user.setPassword("testUser1");
		Set<Authorities> auth = new HashSet<Authorities>();
		Authorities authority = new Authorities();
		authority.setUser(user);
		authority.setAuthority("jugador");
		auth.add(authority);
		user.setAuthorities(auth);
		user.setId(1);
		user.setEmail("test111111@gmail.com");
		Optional<User> userr = Optional.of(user);
		
		User user2= new User("testUser2", "testUser2");
		user2.setAuthorities(auth);
		user2.setId(2);
		user2.setEmail("test22222@gmail.com");
		
		given(this.menuService.findUser(any(String.class))).willReturn(userr);
		
		Jugador jugador2 = new Jugador("test2", "test2", user2, false);
		jugador2.setId(2);
		jugador2.setAmigosInvitados(new ArrayList<Jugador>());
		jugador2.setGamesAsHost(new ArrayList<Match>());
		jugador2.setGamesAsGuest(new ArrayList<Match>());
		jugador2.setReceivedFriendRequests(new ArrayList<FriendRequest>());
		jugador2.setSentFriendRequests(new ArrayList<FriendRequest>());
		
		Jugador jugador = new Jugador("test1", "test1", user, false);
		jugador.setId(1);
		List<Jugador> listaAmigosInvitados=new ArrayList<>();
		listaAmigosInvitados.add(jugador2);
		jugador.setAmigosInvitados(listaAmigosInvitados);
		List<String> listaTipoInvitacionPartidaEnviada=new ArrayList<String>();
		listaTipoInvitacionPartidaEnviada.add("jugador");
		jugador.setTipoDeInvitacionPartidaEnviada(listaTipoInvitacionPartidaEnviada);
		jugador.setGamesAsHost(new ArrayList<Match>());
		jugador.setGamesAsGuest(new ArrayList<Match>());
		jugador.setReceivedFriendRequests(new ArrayList<FriendRequest>());
		jugador.setSentFriendRequests(new ArrayList<FriendRequest>());
		
		Match match=new Match(false,jugador);
		match.setId(1);
		if(partidaConJugador2) {
			match.setJugador2(jugador2);
		}
		
		given(this.menuService.findPlayerByUsername("testUser1")).willReturn(jugador);
		given(this.menuService.findPlayerByUsername("testUser2")).willReturn(jugador2);
		
		Invitacion i=new Invitacion(resultadoInvitacion.SIN_RESPONDER, tipoInvitacion.ESPECTADOR, match, jugador);
		i.setId(1);
		if(invitacionComoJugador) {
			i.setTipo(tipoInvitacion.JUGADOR);
		}
		given(this.invitacionService.getInvitacionById(1)).willReturn(i);
		
		List<Invitacion> lista = new ArrayList<>();
		given(this.invitacionService.getInvitacionByInvitadoId(any(Integer.class))).willReturn(lista);
		
		given(this.playerService.findPlayerByUsername("testUser1")).willReturn(jugador);
		given(this.playerService.findPlayerByUsername("testUser2")).willReturn(jugador2);
		given(this.playerService.findJugadorById(1)).willReturn(jugador);
		given(this.playerService.findJugadorById(2)).willReturn(jugador2);
		given(this.playerService.saveJugador(jugador)).willReturn(jugador);
		given(this.matchService.getMatches()).willReturn(new ArrayList<Match>());
		given(this.matchService.saveMatch(any(Match.class))).willReturn(match);
		given(this.matchService.canIplay(jugador)).willReturn(true);
		given(this.matchService.imPlaying(jugador)).willReturn(true);
	}
	
	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testInvitarAmigos() throws Exception {
		config(false,false);
		mockMvc.perform(get("/invitarAmigo/{id}/{tipoInvitacion}", 2,"jugador"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("players", "actualPlayer","amigosInvitados"))
				.andExpect(view().name("/matches/createMatch"));
	}
	
	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testCreateInvitation() throws Exception {
		config(false,false);
		mockMvc.perform(post("/invitarAmigo/{id}/{tipoInvitacion}", 2,"jugador")
				.param("nombre", "partidaTest").param("tipoPartida", "false").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/matches/1/waitForMatch"));
	}
	
	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testInvitacionesPendientes() throws Exception {
		config(false,false);
		mockMvc.perform(get("/invitacionesPendientes"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("invitaciones"))
				.andExpect(view().name("/invitaciones/invitacionesPendientes"));
	}
	
	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testRechazarInvitacion() throws Exception {
		config(false,false);
		mockMvc.perform(get("/rechazarInvitacion/{id}",1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/invitacionesPendientes"));
	}
	
	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testAceptarInvitacionJugadorSuccess() throws Exception {
		config(true,false);
		mockMvc.perform(get("/aceptarInvitacion/{id}",1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/matches/1/currentMatch"));
	}
	
	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testAceptarInvitacionJugadorHasErrors() throws Exception {
		config(true,true);
		mockMvc.perform(get("/aceptarInvitacion/{id}",1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("yaHayJugador",true))
				.andExpect(model().attributeExists("invitaciones"))
				.andExpect(view().name("/invitaciones/invitacionesPendientes"));
	}
	
	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testAceptarInvitacionEspectadorSuccess() throws Exception {
		config(false,true);
		mockMvc.perform(get("/aceptarInvitacion/{id}",1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/matches/1/currentMatchSpectated"));
	}
	
	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testAceptarInvitacionEspectadorHasErrors() throws Exception {
		config(false,false);
		mockMvc.perform(get("/aceptarInvitacion/{id}",1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("invitaciones"))
				.andExpect(view().name("/invitaciones/invitacionesPendientes"));
	}
	
	
	
	
	
	
	
	
	
}
