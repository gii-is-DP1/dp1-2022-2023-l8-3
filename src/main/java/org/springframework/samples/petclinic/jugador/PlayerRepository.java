package org.springframework.samples.petclinic.jugador;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends CrudRepository<Jugador, Integer>{
	
	@Query("SELECT jugador FROM Jugador jugador")
	List<Jugador> findAll() throws DataAccessException;
	
	
	@Query("SELECT jugador FROM Jugador WHERE jugador.id = :id")
	Jugador findById(@Param("Id") int id); 

}
