package org.springframework.samples.petclinic.player;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
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
import org.springframework.samples.petclinic.invitacion.InvitationService;
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

@WebMvcTest(value = PlayerController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

public class PlayerControllerTests {

	private static final int TEST_PLAYER_ID = 1;

	@MockBean
	private MatchService matchService;

	@MockBean
	private PlayerService playerService;

	@MockBean
	private InvitationService invitacionService;

	@MockBean
	private UserService userService;

	@MockBean
	private FriendRequestService friendRequestService;

	@MockBean
	private MenuService menuService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void configureMock() {

		User user = new User("testUser1", "testUser1");
		user.setAuthorities(new HashSet<Authorities>());

		Optional<User> userr = Optional.of(user);

		given(this.menuService.findUser(any(String.class))).willReturn(userr);

		Jugador jugador = new Jugador("test1", "test1", new User("testUser1", "testUser1"), false);
		given(this.menuService.findPlayerByUsername(any(String.class))).willReturn(jugador);

		List<Invitacion> lista = new ArrayList<>();
		given(this.invitacionService.getInvitacionByInvitadoId(any(Integer.class))).willReturn(lista);

		List<Jugador> ls = new ArrayList<>();
		IntStream.range(0, 20).forEach(a -> ls.add(new Jugador()));
		Page<Jugador> page = new PageImpl<>(ls, PageRequest.of(0, 10), 20);
		given(this.playerService.findAllJugadoresPageable(any(Pageable.class))).willReturn(page);

	}

	void config() {
		User user= new User("testUser1", "testUser1");
		Set<Authorities> auth = new HashSet<Authorities>();
		Authorities authority = new Authorities();
		authority.setUser(user);
		authority.setAuthority("jugador");
		auth.add(authority);
		user.setAuthorities(auth);
		user.setId(1);
		user.setEmail("test1231312@gmail.com");

		Jugador jugador = new Jugador("test1", "test1", user, false);
		jugador.setId(TEST_PLAYER_ID);
		jugador.setGamesAsHost(new ArrayList<Match>());
		jugador.setGamesAsGuest(new ArrayList<Match>());
		jugador.setReceivedFriendRequests(new ArrayList<FriendRequest>());
		jugador.setSentFriendRequests(new ArrayList<FriendRequest>());
		Optional<User> userr = Optional.of(user);
		given(this.userService.findUser("testUser1")).willReturn(userr);
		given(this.playerService.findPlayerByUsername("testUser1")).willReturn(jugador);
		given(this.playerService.findJugadorById(TEST_PLAYER_ID)).willReturn(jugador);
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testShowAllPlayers() throws Exception {
		config();
		mockMvc.perform(get("/jugadores/list/{page}", 1)).andExpect(status().isOk())
				.andExpect(model().attributeExists("numberOfPages", "thisPage", "selections"))
				.andExpect(view().name("jugadores/listJugador"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testShowAllPlayers2() throws Exception {
		config();
		mockMvc.perform(get("/jugadores/list/{page}", -1)).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/jugadores/list/1"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testShowAllPlayers3() throws Exception {
		config();
		mockMvc.perform(get("/jugadores/list/{page}", 5)).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/jugadores/list/2"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testshowAllPlayersEmpty() throws Exception {
		config();
		List<Jugador> ls = new ArrayList<>();
		Page<Jugador> page = new PageImpl<>(ls, PageRequest.of(0, 10), 20);
		given(this.playerService.findAllJugadoresPageable(any(Pageable.class))).willReturn(page);

		mockMvc.perform(get("/jugadores/list/{page}", 1)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/jugadores/new"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testshowPerfil() throws Exception {
		config();

		mockMvc.perform(get("/perfil")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/jugadores/1"));

	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testShowPlayer() throws Exception {
		config();
		
		mockMvc.perform(get("/jugadores/{jugadorId}", TEST_PLAYER_ID)).andExpect(status().isOk())
				.andExpect(view().name("jugadores/showJugador"));

	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testDeletePlayer() throws Exception {
		config();

		mockMvc.perform(get("/jugadores/{jugadorId}/delete", TEST_PLAYER_ID)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/jugadores/list/1"));

	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testInitCreationForm() throws Exception {
		config();
		given(this.playerService.findAllJugadores()).willReturn(new ArrayList<>());
		mockMvc.perform(get("/jugadores/new")).andExpect(status().isOk()).andExpect(model().attributeExists("jugador"))
				.andExpect(view().name("jugadores/createOrUpdateJugadorFormAdmin"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		config();
		given(this.playerService.findAllJugadores()).willReturn(new ArrayList<>());
		given(this.playerService.saveJugador(any(Jugador.class))).willReturn(null);
		mockMvc.perform(post("/jugadores/new").param("user.username", "pepito").param("firstName", "Pepe")
				.param("lastName", "Ruiz").with(csrf()).param("user.email", "pepe@gmail.com")
				.param("user.password", "contrasenya123")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/jugadores/null"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		config();
		given(this.playerService.findAllJugadores()).willReturn(new ArrayList<>());
		given(this.playerService.saveJugador(any(Jugador.class))).willReturn(null);
		mockMvc.perform(post("/jugadores/new").param("user.username", "pepito").param("firstName", "Pepe")
				.param("lastName", "Ruiz").with(csrf()).param("user.email", "emailInvalido")  //email incorrecto
				.param("user.password", "contrasenya123"))
				.andExpect(status().isOk())
				.andExpect(view().name("jugadores/createOrUpdateJugadorFormAdmin"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testInitUpdateForm() throws Exception {
		config();
		mockMvc.perform(get("/jugadores/{jugadorId}/edit", TEST_PLAYER_ID)).andExpect(status().isOk())
				.andExpect(view().name("jugadores/createOrUpdateJugadorForm"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		config();
		given(this.playerService.findAllJugadores()).willReturn(new ArrayList<>());
		given(this.playerService.saveJugador(any(Jugador.class))).willReturn(null);
		mockMvc.perform(post("/jugadores/{jugadorId}/edit", TEST_PLAYER_ID).with(csrf())
				.param("user.username", "pepito").param("firstName", "Pepe")
				.param("lastName", "Ruiz").with(csrf()).param("user.email", "pepe@gmail.com")
				.param("user.password", "Contrasenya123456"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/jugadores/1"));

	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		config();
		mockMvc.perform(post("/jugadores/{jugadorId}/edit",TEST_PLAYER_ID).with(csrf()).param("user.username", "pepe").param("firstName", "Pepe")
				.param("lastName", "Ruiz").param("user.email", "emailInvalido")
				.param("user.password", "Contrasenya123456"))
				.andExpect(status().isOk())
				.andExpect(view().name("jugadores/createOrUpdateJugadorFormAdmin"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testShowMatchesOfAPlayer() throws Exception {
		config();
		User user= new User("testUser", "testUser");
		Jugador jugador = new Jugador("test", "test", user, false);
		jugador.setId(2);
		Match match = new Match(false, jugador);
		match.setName("GameName");
		match.setId(1);
		List<Match> lista=new ArrayList<Match>();
		lista.add(match);
		given(this.matchService.getMatchesByGameWinner(GameWinner.UNDEFINED)).willReturn(new ArrayList<>());
		given(this.matchService.getMatchesOfAPlayer(TEST_PLAYER_ID)).willReturn(lista);

		mockMvc.perform(get("/jugadores/{jugadorId}/playerMatches", TEST_PLAYER_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("playerMatches"))
				.andExpect(model().attributeExists("gamesPlayed"))
				.andExpect(model().attributeExists("gamesWon"))
				.andExpect(model().attributeExists("totalPlayingGame"))
				.andExpect(model().attributeExists("durationOfTheLongestGame"))
				.andExpect(view().name("/jugadores/playerMatches"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testShowMatchesOfAPlayerHasErrors() throws Exception {
		config();
		given(this.matchService.getMatchesOfAPlayer(TEST_PLAYER_ID)).willReturn(new ArrayList<>());
		
		mockMvc.perform(get("/jugadores/{jugadorId}/playerMatches", TEST_PLAYER_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("message")).andExpect(view().name("welcome"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testShowFriendsOfAPlayer() throws Exception {
		config();
		Jugador jugador1 = new Jugador("test1", "test1", new User("testUser1", "testUser1"), false);
		Jugador jugador2 = new Jugador("test1", "test1", new User("testUser1", "testUser1"), false);
		FriendRequest fr = new FriendRequest(jugador1, jugador2);
		fr.setResultado(true);
		jugador1.playerFriends().add(jugador2);

		mockMvc.perform(get("/jugadores/{jugadorId}/playerFriends", TEST_PLAYER_ID)).andExpect(status().isOk()).andExpect(model().attributeExists("playerFriends"))
				.andExpect(view().name("/jugadores/playerFriends"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testGetFriendRequests() throws Exception {
		config();
		mockMvc.perform(get("/jugadores/friendRequests"))
				.andExpect(status().isOk()).andExpect(model()
				.attributeExists("loggedPlayerId")).andExpect(model().attributeExists("listPlayers"))
				.andExpect(view().name("/jugadores/friendRequests"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testFriendRequests() throws Exception {
		config();
		User user = new User("testUser", "testUser");
		Jugador jugador2 = new Jugador("test", "test", user, false);
		jugador2.setId(2);
		jugador2.setGamesAsHost(new ArrayList<Match>());
		jugador2.setGamesAsGuest(new ArrayList<Match>());
		jugador2.setReceivedFriendRequests(new ArrayList<FriendRequest>());
		jugador2.setSentFriendRequests(new ArrayList<FriendRequest>());
		given(this.playerService.findJugadorById(2)).willReturn(jugador2);
		FriendRequest fr=new FriendRequest();
		given(this.friendRequestService.getNoReplyFriendRequestByPlayers(1, 2)).willReturn(fr);
		mockMvc.perform(get("/jugadores/friendRequests/{player1Id}/{player2Id}/{result}",1,2,true))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/jugadores/friendRequests"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testAddFriends() throws Exception {
		config();
		given(this.playerService.findPlayerByKeyword("")).willReturn(new ArrayList<Jugador>());
		mockMvc.perform(get("/jugadores/addFriends"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("loggedPlayerId"))
				.andExpect(model().attributeExists("listPlayers"))
				.andExpect(view().name("/jugadores/addFriends"));
	}

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testAddFriends2() throws Exception {
		config();
		User user = new User("testUser", "testUser");
		Jugador jugador2 = new Jugador("test", "test", user, false);
		jugador2.setId(2);
		jugador2.setGamesAsHost(new ArrayList<Match>());
		jugador2.setGamesAsGuest(new ArrayList<Match>());
		jugador2.setReceivedFriendRequests(new ArrayList<FriendRequest>());
		jugador2.setSentFriendRequests(new ArrayList<FriendRequest>());
		given(this.playerService.findJugadorById(2)).willReturn(jugador2);
		FriendRequest fr=new FriendRequest();
		given(this.friendRequestService.getNoReplyFriendRequestByPlayers(1, 2)).willReturn(fr);
		given(this.friendRequestService.getFriendshipByPlayers(1, 2)).willReturn(fr);
		
		mockMvc.perform(get("/jugadores/addFriends/{player1Id}/{player2Id}",1,2))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/jugadores/addFriends"));
	}	

	@WithMockUser(username = "testUser1", password = "testUser1")
	@Test
	void testDeleteFriends() throws Exception {
		config();
		FriendRequest fr=new FriendRequest();
		given(friendRequestService.getFriendRequestByPlayers(1, 2)).willReturn(fr);
		given(friendRequestService.getFriendRequestByPlayers(2, 1)).willReturn(fr);
		mockMvc.perform(get("/jugadores/{jugadorId1}/playerFriends/{jugadorId2}/delete",1,2))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/jugadores/{jugadorId1}/playerFriends"));

	}

}
