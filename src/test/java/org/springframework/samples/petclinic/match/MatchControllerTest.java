package org.springframework.samples.petclinic.match;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.invitacion.InvitationService;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.menu.MenuService;
import org.springframework.samples.petclinic.partida.GameWinner;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchController;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = MatchController.class,		
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)

public class MatchControllerTest {
	
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
    }

    
    
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testListingMatches() throws Exception {
		given(this.matchService.getMatchesWithoutPlayer2()).willReturn(new ArrayList<>());
		given(this.matchService.getMatches()).willReturn(new ArrayList<>());

		mockMvc.perform(get("/matches/matchesList"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("match_list","matches"))
				.andExpect(view().name("/matches/matchesList"));
	}
    
//    @WithMockUser(username = "testUser1", password="testUser1")
//    @Test
//	void testGetCreateMatch() throws Exception {
//		given(this.playerService.findAllJugadores()).willReturn(new ArrayList<>());
//		
//		mockMvc.perform(get("/matches/createMatch"))
//				.andExpect(status().isOk())
//				.andExpect(model().attributeExists("players"))
//				.andExpect(view().name("/matches/createMatch"));
//	}
    
    
  
    void config(Boolean conJugador2,Boolean finPartida) {
	    Jugador jugador1 = new Jugador("test1", "test1", new User("testUser1","testUser1"), false);
	    Jugador jugador2 = new Jugador("test2", "test2", new User("testUser2","testUser2"), false);
	    jugador1.setId(5);
	    jugador2.setId(6);

        Match match = new Match(false, jugador1);
        match.setName("GameName");
        
        if(conJugador2) {
	        match.setJugador2(jugador2);
			given(this.playerService.findPlayerByUsername("testUser2")).willReturn(jugador2);
        }
        if(finPartida) 
        	match.setGanadorPartida(GameWinner.FIRST_PLAYER);
                
        match.setId(1);
        
		given(this.matchService.getMatchById(1)).willReturn(match);
		given(this.playerService.findPlayerByUsername("testUser1")).willReturn(jugador1);
    }
    

    
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testGetShowWaitSiendoJugador1SinJugador2() throws Exception {
		config(false,false);
    	
		mockMvc.perform(get("/matches/{idMatch}/waitForMatch", 1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("match","EresJugador1"))
				.andExpect(view().name("/matches/waitForMatch"));
	}
    @WithMockUser(username = "testUser2", password="testUser2")
    @Test
	void testGetShowWaitSiendoJugador2SinJugador2() throws Exception {
		config(false,false);
    	
		mockMvc.perform(get("/matches/{idMatch}/waitForMatch", 1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("match"))
				.andExpect(view().name("/matches/waitForMatch"));
	}
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testGetShowWaitConJugador2() throws Exception {
		config(true,false);
    	
		mockMvc.perform(get("/matches/{idMatch}/waitForMatch", 1))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/matches/1/currentMatch"));
	}
    
    @WithMockUser(username = "testUser2", password="testUser2")
    @Test
	void testPostShowMatch() throws Exception {
		config(true,false);
		given(this.matchService.saveMatch(any(Match.class))).willReturn(null);

		mockMvc.perform(post("/matches/{idMatch}/waitForMatch", 1)
					.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/matches/1/currentMatch"));
	
    }
    
    @WithMockUser(username = "testUser2", password="testUser2")
    @Test
	void testShowCurrentMatch() throws Exception {
		config(true,false);
		given(this.matchService.saveMatch(any(Match.class))).willReturn(null);
		given(this.playerService.saveJugador(any(Jugador.class))).willReturn(null);

		mockMvc.perform(get("/matches/{idMatch}/currentMatch", 1)
					.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("idLoggedPlayer","idCurrentPlayer","isYourTurn","match"))
				.andExpect(view().name("/matches/currentMatch"));
	}
    
    @WithMockUser(username = "testUser2", password="testUser2")
    @Test
	void testShowCurrentMatchFinPartida() throws Exception {
		config(true,true);
		given(this.matchService.saveMatch(any(Match.class))).willReturn(null);
		given(this.playerService.saveJugador(any(Jugador.class))).willReturn(null);

		mockMvc.perform(get("/matches/{idMatch}/currentMatch", 1)
					.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("idLoggedPlayer","idCurrentPlayer","isYourTurn","match"))
				.andExpect(view().name("/matches/matchStatistics"));
	}
	
    void config2(Boolean conJugador2,Boolean finPartida, Integer fase) {
	    Jugador jugador1 = new Jugador("test1", "test1", new User("testUser1","testUser1"), false);
	    Jugador jugador2 = new Jugador("test2", "test2", new User("testUser2","testUser2"), false);
	    jugador1.setId(5);
	    jugador2.setId(6);

        Match match = new Match(false, jugador1);
        match.setName("GameName");
        
        if(conJugador2) {
	        match.setJugador2(jugador2);
			given(this.playerService.findPlayerByUsername("testUser2")).willReturn(jugador2);
        }
        if(finPartida) 
        	match.setGanadorPartida(GameWinner.FIRST_PLAYER);
        
        match.setTurn(fase);

        match.setId(1);
        
        match.setDisco1(0);
        match.setDisco2(0);
        match.setDisco3(0);
        match.setDisco4(0);
        match.setDisco5(0);
        match.setDisco6(0);
        match.setDisco7(0);
        match.setDeDisco(Arrays.array(1));
        
        match.getDisco(0).annadirBacterias(0, 1);
        match.getDisco(3).annadirBacterias(1, 1);

		given(this.matchService.getMatchById(1)).willReturn(match);
		given(this.playerService.findPlayerByUsername("testUser1")).willReturn(jugador1);
		given(this.matchService.saveMatch(any(Match.class))).willReturn(null);
		given(this.playerService.saveJugador(any(Jugador.class))).willReturn(null);
    }

    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testNextPhasePropagacion() throws Exception {
		config2(true,true,1);

		mockMvc.perform(post("/matches/{idMatch}/currentMatch", 1)
					.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("error"))
				.andExpect(view().name("/matches/currentMatch"));
	}
    
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testNextPhaseFin() throws Exception {
		config2(true,true,40);
		mockMvc.perform(post("/matches/{idMatch}/currentMatch", 1)
					.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(view().name("/matches/matchStatistics"));
	}

    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testAbandonedMatch() throws Exception {
		config(true,true);
		mockMvc.perform(get("/matches/{idMatch}/abandoned", 1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("winner","match"))
				.andExpect(view().name("/matches/abandoned"));
	}
    
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testMatchStatistics() throws Exception {
		config(true,true);
		mockMvc.perform(get("/matches/{idMatch}/statistics", 1))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("match"))
				.andExpect(view().name("/matches/matchStatistics"));
	}
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testShowMatchesInProgress() throws Exception {
		given(this.matchService.getMatchesByGameWinner(any(GameWinner.class))).willReturn(new ArrayList<>());

    	mockMvc.perform(get("/matches/InProgress"))
				.andExpect(status().isOk())
				.andExpect(view().name("matches/listMatchesInProgress"));
	}
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testShowMatchesFinished() throws Exception {
		given(this.matchService.getMatchesByGameWinner(any(GameWinner.class))).willReturn(new ArrayList<>());

    	mockMvc.perform(get("/matches/Finished"))
				.andExpect(status().isOk())
				.andExpect(view().name("matches/listMatchesFinished"));
	}

    
 

  

}
