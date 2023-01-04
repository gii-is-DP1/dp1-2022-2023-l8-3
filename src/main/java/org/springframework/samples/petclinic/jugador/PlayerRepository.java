package org.springframework.samples.petclinic.jugador;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.statistics.Achievement;

public interface PlayerRepository extends JpaRepository<Jugador, Integer>{
	
	@Query("SELECT jugador FROM Jugador jugador")
	public List<Jugador> findAll() throws DataAccessException;
	
	@Query("SELECT jugador FROM Jugador jugador")
	public Page<Jugador> findAllPageable(Pageable pageable) throws DataAccessException;

	
	@Query("SELECT jugador FROM Jugador jugador WHERE jugador.id =:id")
	public Jugador findById(@Param("id") int id);
	

	@Query("SELECT DISTINCT jugador FROM Jugador jugador WHERE jugador.lastName LIKE :lastName")
	public Collection<Jugador> findByLastName(@Param("lastName") String lastName);
	
	@Query("SELECT DISTINCT jugador FROM Jugador jugador WHERE jugador.user.username LIKE :username")
    public Jugador findByUserName(@Param("username") String userName);

	@Query("SELECT j FROM Jugador j WHERE UPPER(j.user.username) LIKE %?1%")
	public List<Jugador> findByKeyword(String keyword);

	@Query("SELECT j.logros FROM Jugador j WHERE UPPER(j.user.username) LIKE %?1%")
	public Page<Achievement> findAchievementsOfUser(String keyword, Pageable pageable);

}
