package org.springframework.samples.petclinic.disco;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DishRepository extends CrudRepository<Disco, Integer>{
	
	
	@Query("SELECT disco FROM Disco disco WHERE disco.match.id =:id")
	public List<Disco> findDiscosWithMatchId(@Param("id") Integer id) throws DataAccessException;

}
