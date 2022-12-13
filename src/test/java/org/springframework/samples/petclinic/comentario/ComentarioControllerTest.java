package org.springframework.samples.petclinic.comentario;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.invitacion.InvitationService;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.menu.MenuService;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = ComentarioController.class,		
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)


public class ComentarioControllerTest {
	
	@MockBean
    private MatchService matchService;
	@MockBean
    private PlayerService playerService;
	@MockBean
    private ComentarioService comentarioService;
	@MockBean
    private InvitationService invitacionService;
	@MockBean
    private MenuService menuService;
	


    @Autowired
    private MockMvc mockMvc;

    
    @BeforeEach
    public void configureMock(){
	    Jugador jugador1 = new Jugador("test1", "test1", new User("testUser1","testUser1"), false);
	    Jugador jugador2 = new Jugador("test2", "test2", new User("testUser2","testUser2"), false);
	    jugador1.setId(5);
	    jugador2.setId(6);

        Match match = new Match(false, jugador1);
        match.setName("GameName");
        match.setJugador2(jugador2);
        match.setId(1);
        
		given(this.matchService.getMatchById(1)).willReturn(match);
		given(this.playerService.findPlayerByUsername("testUser1")).willReturn(jugador1);
		given(this.playerService.findPlayerByUsername("testUser2")).willReturn(jugador2);
		given(this.playerService.findJugadorById(5)).willReturn(jugador1);
		given(this.playerService.findJugadorById(6)).willReturn(jugador2);

    }
    
    
    
    @WithMockUser(username = "testUser1", password="testUser1")
    @Test
	void testPostCommentUser1() throws Exception {
    	
		mockMvc.perform(post("/chat/{idMatch}/postMsg", 1)
							.with(csrf())
							.param("msg", "ola vuenas"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/matches/{idMatch}/currentMatch"));
	}
    @WithMockUser(username = "testUser2", password="testUser2")
    @Test
	void testPostCommentUser2() throws Exception {
    	
		mockMvc.perform(post("/chat/{idMatch}/postMsg", 1)
							.with(csrf())
							.param("msg", "ola vuenas"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/matches/{idMatch}/currentMatch"));
	}

}
