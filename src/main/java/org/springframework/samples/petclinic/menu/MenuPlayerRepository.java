package org.springframework.samples.petclinic.menu;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.jugador.Jugador;

public interface MenuPlayerRepository extends CrudRepository<Jugador, Integer>{
	
	@Query("SELECT DISTINCT jugador FROM Jugador jugador WHERE jugador.user.username LIKE :username")
    public Jugador findByUserName(@Param("username") String userName);

}
