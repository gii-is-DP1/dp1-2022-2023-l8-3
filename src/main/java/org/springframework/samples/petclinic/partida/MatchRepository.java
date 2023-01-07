package org.springframework.samples.petclinic.partida;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface MatchRepository extends JpaRepository<Match, Integer> {
	List<Match> findAll();
	
	@Query("SELECT match FROM Match match WHERE match.jugador2 IS NULL")
	public Collection<Match> findMatchesWhitoutPlayer2() throws DataAccessException;
	
	@Query("SELECT match FROM Match match WHERE match.ganadorPartida =:gameWinner")
	public Page<Match> findMatchesByGameWinnerPageable(@Param("gameWinner") GameWinner gameWinner, Pageable pageable) throws DataAccessException;
	
	@Query("SELECT match FROM Match match WHERE match.ganadorPartida =:gameWinner")
	public List<Match> findMatchesByGameWinner(@Param("gameWinner") GameWinner gameWinner) throws DataAccessException;

	@Query("SELECT match FROM Match match WHERE match.jugador1.id =:id")
	public List<Match> findMatchsWithIdPlayer1(@Param("id") Integer id) throws DataAccessException;
	
	@Query("SELECT match FROM Match match WHERE match.jugador2.id =:id")
	public List<Match> findMatchsWithIdPlayer2(@Param("id") Integer id) throws DataAccessException;

	@Query("SELECT m FROM Match m WHERE m.ganadorPartida != 'UNDEFINED'")
	public Collection<Match> findPlayedMatches();
	
	@Query("SELECT m FROM Match m WHERE m.jugador1.id = :idJugador OR m.jugador2.id = :idJugador ORDER BY m.finPartida")
	public Collection<Match> findMatchesOfAPlayer(Integer idJugador);
	
	@Query("SELECT m FROM Match m WHERE m.jugador1.id = :idJugador OR m.jugador2.id = :idJugador")
	public Page<Match> findMatchesOfAPlayerPageable(Integer idJugador, Pageable pageable);

	@Query("SELECT m FROM Match m WHERE m.ganadorPartida =:gm1 OR m.ganadorPartida =:gm2 OR m.ganadorPartida =:gm3")
	Page<Match> findMatchesFinishedPageable(Pageable pageable, GameWinner gm1, GameWinner gm2, GameWinner gm3);

	
}
