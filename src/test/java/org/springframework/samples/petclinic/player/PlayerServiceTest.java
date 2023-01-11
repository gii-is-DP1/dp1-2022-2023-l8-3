package org.springframework.samples.petclinic.player;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PlayerServiceTest {
	
	@Autowired
	private PlayerService playerService;
	
	@Test
	public void testCountWithInitialData() {
		int count = playerService.playerCount();
		assertEquals(6, count);;
	}
	
	@Test
	public void testPlayerById() {
		Integer id = 1;
		assertNotNull(playerService.findJugadorById(id));
	}
	
	@Test
	public void testPlayers() {
		List<Jugador> players = (List<Jugador>) playerService.findAllJugadores();
		for (Jugador player : players) {
			assertNotNull(player);
		}
	}
	
	@Test
	public void testPlayerByLastName() {
		String lastName = "Martin";
		assertNotNull(playerService.findPlayerByLastName(lastName));
	}
	
	@Test
	public void testPlayerByUsername() {
		String username = "davdancab";
		assertNotNull(playerService.findPlayerByUsername(username));
	}
	
	@Test
	public void testSavePlayer() {
		User user = new User("pepe33");
		user.setEmail("test@test.com");
		user.setPassword("test1");
		Jugador player = new Jugador("Pepe", "García", user, false);
		playerService.saveJugador(player);
		Integer id = player.getId();
		assertNotNull(playerService.findJugadorById(id));
	}
	
	@Test
	public void testDeletePlayer() {
		User user = new User("maria33");
		user.setEmail("test@test.com");
		user.setPassword("test1");
		Jugador player = new Jugador("María", "Oliva", user, false);
		playerService.saveJugador(player);
		Integer id = player.getId();
		try {
			playerService.deletePlayer(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThrows(DataAccessException.class,()->playerService.findJugadorById(id));
	}
	
}
