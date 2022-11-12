package org.springframework.samples.petclinic.statistics;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AchievementRepository extends CrudRepository<Achievement, Integer> {
	List<Achievement> findAll();
	// TODO: Consulta que obtiene todos los logros de un jugador
	@Query("SELECT a FROM Achievement a") // de prueba, para que salga algo en pantalla
	List<Achievement> findAchievementsOfAPlayer(Integer id);
}
