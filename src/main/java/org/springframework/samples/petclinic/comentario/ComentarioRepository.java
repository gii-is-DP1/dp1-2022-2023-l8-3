package org.springframework.samples.petclinic.comentario;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends CrudRepository<Comentario, Integer>{
	@Query("SELECT c FROM Comentario c WHERE c.jugador.id = :playerId")
	public List<Comentario> findByPlayer(@Param("playerId") int playerId);
}
