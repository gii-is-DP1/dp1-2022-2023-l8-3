package org.springframework.samples.petclinic.partida;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.stereotype.Repository;

@Repository

public interface MatchRepository extends CrudRepository<Match, Integer> {
	List<Match> findAll();
	
	@Query("SELECT match FROM Match match WHERE match.jugador2 IS NULL")
	public Collection<Match> findMatchesWhitoutP2() throws DataAccessException;
	
	@Query("SELECT match FROM Match match WHERE match.ganadorPartida =:gameWinner")
	public List<Match> findMatchesByGameWinner(@Param("gameWinner") GameWinner gameWinner) throws DataAccessException;
	
	@Query("SELECT match FROM Match match WHERE match.jugador1.id =:id")
	public List<Match> findMatchsWithIdPlayer1(@Param("id") Integer id) throws DataAccessException;
	
	@Query("SELECT match FROM Match match WHERE match.jugador2.id =:id")
	public List<Match> findMatchsWithIdPlayer2(@Param("id") Integer id) throws DataAccessException;

	@Query("SELECT m FROM Match m WHERE m.jugador1.id = :idJugador")
	public Collection<Match> findMatchesOfAPlayer(Integer idJugador);

	
}
