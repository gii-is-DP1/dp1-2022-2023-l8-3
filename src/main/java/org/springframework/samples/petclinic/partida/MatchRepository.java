package org.springframework.samples.petclinic.partida;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MatchRepository extends CrudRepository<Match, Integer> {
	List<Match> findAll();
}
