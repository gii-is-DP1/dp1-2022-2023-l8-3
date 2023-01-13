package org.springframework.samples.petclinic.statistics;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.menu.MenuService;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = AchievementController.class,		
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)


public class AchievementControllerTest {
	
	@MockBean
    private MatchService matchService;
	@MockBean
    private PlayerService playerService;
	@MockBean
    private UserService userService;
	@MockBean
    private InvitationService invitacionService;
	@MockBean
    private MenuService menuService;

	@MockBean
    private AchievementService achievementService;
	
    @Autowired
    private MockMvc mockMvc;

    
    public void configu(){

    	User user=new User("testUser1","testUser1");
    	
    	user.setAuthorities(new HashSet());
    	
    	Optional<User>userr=Optional.of(user);
    	
	    Jugador jugador1 = new Jugador("test1", "test1", new User("testUser1","testUser1"), false);
	    Jugador jugador2 = new Jugador("test2", "test2", new User("testUser2","testUser2"), false);
	    jugador1.setId(5);
	    jugador2.setId(6);

        Match match = new Match(false, jugador1);
        match.setName("GameName");
        match.setJugador2(jugador2);
        match.setId(1);
        
    }
        
    @BeforeEach
    public void configureMock(){
    	User user=new User("testUser1","testUser1");
    	
    	user.setAuthorities(new HashSet());
    	
    	Optional<User>userr=Optional.of(user);

    	given(this.menuService.findUser(any(String.class))).willReturn(userr);
        
    	Jugador jugador = new Jugador("test1", "test1", new User("testUser1","testUser1"), false);
    	given(this.menuService.findPlayerByUsername(any(String.class))).willReturn(jugador);
    	
    	List<Invitacion> lista=new ArrayList<>();
    	given(this.invitacionService.getInvitacionByInvitadoId(any(Integer.class))).willReturn(lista);

    	
    	List<Achievement> ls = new ArrayList<>();
    	IntStream.range(0, 20).forEach(a -> ls.add(new Achievement()));

    	Page<Achievement> page = new PageImpl<>(ls, PageRequest.of(0, 10), 20);
		given(this.achievementService.getPublicAchievementsPageable(any(Pageable.class))).willReturn(page);
   
		given(this.userService.findUser(any(String.class))).willReturn(userr);
		given(this.playerService.findPlayerByUsername(any(String.class))).willReturn(jugador);
		
		List<Match>matches = new ArrayList<>(); matches.add(new Match());
		given(this.matchService.getMatchesOfAPlayer(null)).willReturn(matches);
		
		given(this.playerService.findAchievementsOfUser(any(String.class), any(Pageable.class))).willReturn(page);
		
		given(this.achievementService.getAchievementsPageable(any(Pageable.class))).willReturn(page);

    }

    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testShowAchievements() throws Exception {
    	
		mockMvc.perform(get("/statistics/achievements/{page}", 1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("numberOfPages","thisPage","url","achievements"))
				.andExpect(view().name("/achievements/achievementsListing"));
	}

    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testShowAchievements2() throws Exception {
    	
		mockMvc.perform(get("/statistics/achievements/{page}", -1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/statistics/achievements/1"));
	}

    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testShowAchievements3() throws Exception {
    	
		mockMvc.perform(get("/statistics/achievements/{page}", 5))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/statistics/achievements/2"));
	}

    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testShowCurrentPlayerAchievements() throws Exception {
    	
		mockMvc.perform(get("/statistics/achievements/currentPlayer/{page}", 1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("numberOfPages","thisPage","url","achievements"))
				.andExpect(view().name("/achievements/achievementsListing"));
	}
	
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testShowCurrentPlayerAchievements2() throws Exception {
    	
		mockMvc.perform(get("/statistics/achievements/currentPlayer/{page}", -1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/statistics/achievements/currentPlayer/1"));
	}
    
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testShowCurrentPlayerAchievements3() throws Exception {
    	
		mockMvc.perform(get("/statistics/achievements/currentPlayer/{page}", 5))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/statistics/achievements/currentPlayer/2"));
	}

    
    @WithMockUser(username = "admin1", password="4dm1n")
    @Test
	void testShowAchievementsAdmin() throws Exception {
    	
		mockMvc.perform(get("/statistics/achievements/admin/{page}", 1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("numberOfPages","thisPage","achievements"))
				.andExpect(view().name("/achievements/admin/achievementsListing"));
	}
    
    @WithMockUser(username = "admin1", password="4dm1n")
    @Test
	void testShowAchievementsAdmin2() throws Exception {
    	
		mockMvc.perform(get("/statistics/achievements/admin/{page}", -1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/statistics/achievements/admin/1"));
	}
    
    @WithMockUser(username = "admin1", password="4dm1n")
    @Test
	void testShowAchievementsAdmin3() throws Exception {
    	
		mockMvc.perform(get("/statistics/achievements/admin/{page}", 5))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/statistics/achievements/admin/2"));
	}

    
    @WithMockUser(username = "admin1", password="4dm1n")
    @Test
	void testEditAchievement() throws Exception {
    	Achievement a = new Achievement();
    	a.setVisibility(Visibility.EN_BORRADOR);
		given(this.achievementService.getAchievementById(1)).willReturn(a);

		mockMvc.perform(get("/statistics/achievements/admin/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("achievement","metrics","difficulty","visibility"))
				.andExpect(view().name("/achievements/createOrUpdateAchievementForm"));
	}
	
    @WithMockUser(username = "admin1", password="4dm1n")
    @Test
	void testEditAchievement2() throws Exception {
    	Achievement a = new Achievement();
    	a.setVisibility(Visibility.PUBLICADO);
		given(this.achievementService.getAchievementById(1)).willReturn(a);

		mockMvc.perform(get("/statistics/achievements/admin/{id}/edit", 1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/statistics/achievements/admin/1"));
	}
	
    @WithMockUser(username = "admin1", password="4dm1n")
    @Test
	void testSaveEditAchievement() throws Exception {
    	Achievement a = new Achievement();
    	a.setVisibility(Visibility.PUBLICADO);
    	a.setId(1);
		given(this.achievementService.getAchievementById(1)).willReturn(a);

		mockMvc.perform(post("/statistics/achievements/admin/{id}/edit", 1)
						.with(csrf())
						.param("name", "nombre")
						.param("metrics", "AMIGOS")
						.param("threshold", "100")
						.param("difficulty", "ORO")
						.param("visibility", "PUBLICADO")
						.param("description", "descripcion"))
				.andExpect(status().isOk())
				.andExpect(view().name("/achievements/achievementsListing"));
	}
	
	@WithMockUser(username = "admin1", password="4dm1n")
    @Test
	void testSaveEditAchievementFails() throws Exception {
    	Achievement a = new Achievement();
    	a.setVisibility(Visibility.PUBLICADO);
    	a.setId(1);
		given(this.achievementService.getAchievementById(1)).willReturn(a);

		mockMvc.perform(post("/statistics/achievements/admin/{id}/edit", 1)
						.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(view().name("/achievements/createOrUpdateAchievementForm"));
	}

    
    @WithMockUser(username = "admin1", password="4dm1n")
    @Test
	void testNewAchievement() throws Exception {
		
		mockMvc.perform(get("/statistics/achievements/admin/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("metrics","difficulty","visibility","achievement"))
				.andExpect(view().name("/achievements/createOrUpdateAchievementForm"));
	}
  
    @WithMockUser(username = "admin1", password="4dm1n")
    @Test
	void testSaveAchievement() throws Exception {
		
		mockMvc.perform(post("/statistics/achievements/admin/new")
						.with(csrf())
						.param("id", "10")
						.param("name", "nombre")
						.param("metrics", "AMIGOS")
						.param("threshold", "100")
						.param("difficulty", "ORO")
						.param("visibility", "PUBLICADO")
						.param("description", "descripcion"))
				.andExpect(status().isOk())
				.andExpect(view().name("/achievements/achievementsListing"));
	}

    @WithMockUser(username = "admin1", password="4dm1n")
    @Test
	void testSaveAchievementFails() throws Exception {
		Achievement a= new Achievement();
		a.setId(10);
		a.setMetrics(Metrics.AMIGOS);
		a.setThreshold(100.0);
		Collection<Achievement> lista=new ArrayList<Achievement>();
		lista.add(a);
		given(achievementService.getAchievements()).willReturn(lista);
    	
		mockMvc.perform(post("/statistics/achievements/admin/new")
				.with(csrf())
				.param("id", "10")
				.param("name", "nombre")
				.param("metrics", "AMIGOS")
				.param("threshold", "100")
				.param("difficulty", "ORO")
				.param("visibility", "PUBLICADO")
				.param("description", "descripcion")) 
				.andExpect(status().isOk())
				.andExpect(view().name("/achievements/createOrUpdateAchievementForm"));
		// ya existe un logro con esa metrica y treshold asi que te devuelve a la pagina de creacion de logros
	}

    
    @WithMockUser(username = "admin1", password="4dm1n")
    @Test
	void testDeleteAchievement() throws Exception {
    	
    	Achievement a = new Achievement();
    	a.setVisibility(Visibility.PUBLICADO);
    	a.setId(1);
		given(this.achievementService.getAchievementById(1)).willReturn(a);

		mockMvc.perform(get("/statistics/achievements/admin/{id}/delete",1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("message"))
				.andExpect(view().name("/achievements/achievementsListing"));
	}


}
