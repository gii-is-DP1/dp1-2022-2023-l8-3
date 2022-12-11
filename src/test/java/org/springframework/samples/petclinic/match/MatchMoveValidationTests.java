package org.springframework.samples.petclinic.match;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.user.User;

@DataJpaTest
public class MatchMoveValidationTests {
	private Match match;
	
	@BeforeEach
	void init() {
	    Jugador jugador = new Jugador("PlayerFirstName", "PlayerLastName", new User("PlayerUserName"), false);
	    Jugador jugador2 = new Jugador("PlayerFirstName2", "PlayerLastName2", new User("PlayerUserName2"), false);
	   
        match = new Match(false, jugador);
        match.setJugador1(jugador);
        match.setJugador2(jugador2);
        match.setName("GameName");
        match.setDisco1(0);
        match.setDisco2(0);
        match.setDisco3(0);
        match.setDisco4(0);
        match.setDisco5(0);
        match.setDisco6(0);
        match.setDisco7(0);
	}
    //Al mover bacteria de disco 1 a disco 2 queda 1 bacteria enemiga y aliada, incumple norma
	@Test
	void testMismoNumeroBacteriasEnDiscoDestino() {
        match.getDisco(0).annadirBacterias(0, 1);
        match.getDisco(1).annadirBacterias(1, 1);

        match.setDeDisco(Arrays.array(1));
        match.setDisco2(1);

        String msg = match.validateMove();
        assertEquals("Mismo numero de bacterias enemigas que aliadas en disco: 2",
        		msg);
    }
	
    //Al mover bacteria de disco 1 a disco 2 queda 1 bacteria enemiga y aliada, incumple norma
	@Test
	void testMismoNumeroBacteriasEnDiscoOrigen() {
        match.getDisco(0).annadirBacterias(0, 2);
        match.getDisco(0).annadirBacterias(1, 1);

        match.setDeDisco(Arrays.array(1));
        match.setDisco2(1);

        String msg = match.validateMove();
        assertEquals("Mismo numero de bacterias enemigas que aliadas en disco: 1",
        		msg);
    }
	
	@Test
	void testBacteriasNegativasEnOrigen() {
        match.getDisco(0).annadirBacterias(0, 1);

        match.setDeDisco(Arrays.array(1));
        match.setDisco2(2);

        String msg = match.validateMove();
        assertEquals("Bacterias negativas en origen:1",
        		msg);
	}
	@Test
	void testQuedanMasDe5BacteriasEnDestino() {
        match.getDisco(0).annadirBacterias(0, 4);
        match.getDisco(1).annadirBacterias(0, 4);

        match.setDeDisco(Arrays.array(1));
        match.setDisco2(4);

        String msg = match.validateMove();
        assertEquals("Mas de 5 bacterias en disco destino",
        		msg);

	}
	@ParameterizedTest
	@ValueSource(ints = { -10, -1 , 5, 10 })
	void testValorNoPermitido(int valor) {
        match.getDisco(0).annadirBacterias(0, 1);

        match.setDeDisco(Arrays.array(1));
        match.setDisco2(valor);

        String msg = match.validateMove();
        assertEquals("Valor de bacterias no permitido",
        		msg);
	}
	@Test
	void testNingunMovimiento() {
        match.getDisco(0).annadirBacterias(0, 4);
        match.setDeDisco(Arrays.array(1));

        String msg = match.validateMove();
        assertEquals("No se indicó ningun movimiento",
        		msg);
	}
	@Test
	void testMasDeUnDiscoOrigen() {
        match.getDisco(0).annadirBacterias(0, 4);
        match.setDeDisco(Arrays.array(1,2));

        String msg = match.validateMove();
        assertEquals("Más de un disco origen o ninguno",
        		msg);
	}
	@Test
	void testNingunDiscoOrigenSeleccionado() {
        match.getDisco(0).annadirBacterias(0, 4);

        String msg = match.validateMove();
        assertEquals("Más de un disco origen o ninguno",
        		msg);
	}

}
