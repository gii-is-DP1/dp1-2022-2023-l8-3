package org.springframework.samples.petclinic.match;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.partida.GameWinner;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class MatchServiceTests {
	
	@Autowired
	private MatchService matchService;
	
	private PlayerService playerService;
	
	@Autowired
	public MatchServiceTests(PlayerService playerService) {
		this.playerService = playerService;
	}
	
	@Test
	void testMatchWithoutPlayer2() {
		List<Match> matches = (List<Match>) matchService.getMatchesWithoutPlayer2();
		for (Match match : matches) {
			assertNull(match.getJugador2());
		}
	}
	
	@Test
	void testMatches() {
		List<Match> matches = (List<Match>) matchService.getMatches();
		for (Match match : matches) {
			assertNotNull(match);
		}
	}
	
	@Test
	void testMatchById() {
		Integer id = 1;
		assertNotNull(matchService.getMatchById(id));
	}
	
	@Test
	void testCreateMatchWithInitialData() {
		Match match = new Match(false, playerService.findJugadorById(1));
		matchService.saveMatch(match);
		Integer id = match.getId();
		assertNotNull(matchService.getMatchById(id));
	}
	
	@Test
	void tetsMatchesByGameWinner() {
		GameWinner gameWinner = GameWinner.FIRST_PLAYER;
		assertNotNull(matchService.getMatchesByGameWinner(gameWinner));
	}
		

}
